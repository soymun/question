# Используем официальный образ Python
FROM python:3.9-slim

# Устанавливаем рабочую директорию
WORKDIR /

# Устанавливаем зависимости
RUN pip install --no-cache-dir pika

# Копируем код сервиса
COPY . .

# Запуск сервиса
CMD ["python", "execute.py"]