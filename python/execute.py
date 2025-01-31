import json
from datetime import datetime

import pika


class Status:
    OK = "OK"
    ERROR = "ERROR"


def check(user_code, check_code):
    """
    Функция для проверки правильности кода.
    Выполняет user_code (создает класс), затем выполняет check_code (проверки).
    """
    # Локальное пространство имен для выполнения кода
    user_namespace = {}
    check_namespace = {}

    try:
        # Выполняем user_code, чтобы создать класс
        exec(user_code, user_namespace, user_namespace)

        # Копируем созданные объекты (например, класс) в пространство имен для check_code
        check_namespace.update(user_namespace)


        timePrev = int(datetime.now().timestamp() * 1000)

        # Выполняем check_code, который использует класс из user_code
        exec(check_code, check_namespace, check_namespace)

        timeNext = int(datetime.now().timestamp() * 1000)

        # Если check_code выполнился без ошибок, считаем проверку успешной
        return Status.OK, "Проверка пройдена успешно.", timeNext - timePrev
    except Exception as e:
        return Status.ERROR, f"Ошибка при выполнении кода: {e}", 0


def send_result(channel, message, result_status, result_message, time):
    """
    Отправляет результат проверки в RabbitMQ.
    """
    response = {
        "status": result_status,
        "message": result_message,
        "userId": message.get("userId"),
        "taskId": message.get("taskId"),
        "time": time,  # Время в миллисекундах
        "attempt": message.get("attempt")
    }

    # Отправка результата в очередь
    channel.basic_publish(
        exchange='courses',
        routing_key='completed-code',  # Очередь для результатов
        body=json.dumps(response),
        properties=pika.BasicProperties(
            delivery_mode=2,  # Сделать сообщение persistent
        )
    )
    print(f"Результат отправлен: {response}")


def on_message_received(ch, method, properties, body):
    """
    Функция, которая вызывается при получении сообщения из RabbitMQ.
    """
    try:
        # Десериализация сообщения из JSON
        message = json.loads(body)

        # Извлечение userCode и checkCode
        user_code = message.get('userCode')
        check_code = message.get('checkCode')

        if not user_code or not check_code:
            print("Ошибка: userCode или checkCode отсутствуют в сообщении.")
            ch.basic_ack(delivery_tag=method.delivery_tag)
            return

        # Выполнение проверки
        status, messageRes, time = check(user_code, check_code)

        # Отправка результата
        send_result(ch, message, status, messageRes, time)

        # Подтверждение обработки сообщения
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except json.JSONDecodeError:
        print("Ошибка: сообщение не является валидным JSON.")
        ch.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        print(f"Ошибка при обработке сообщения: {e}")
        ch.basic_ack(delivery_tag=method.delivery_tag)


def start_consuming():
    """
    Функция для запуска потребителя RabbitMQ.
    """
    try:
        # Подключение к RabbitMQ
        connection = pika.BlockingConnection(pika.ConnectionParameters('rabbitmq', 5672))
        channel = connection.channel()

        # Настройка потребителя
        channel.basic_consume(queue='PYTHON', on_message_callback=on_message_received)

        # Начало потребления сообщений
        channel.start_consuming()

    except pika.exceptions.AMQPConnectionError:
        print("Ошибка: не удалось подключиться к RabbitMQ.")
    except KeyboardInterrupt:
        print("Сервис остановлен.")
    except Exception as e:
        print(f"Ошибка: {e}")


if __name__ == "__main__":
    start_consuming()
