create schema if not exists course1;
create schema if not exists course2;

CREATE TABLE course1.groups
(
    id   int PRIMARY KEY,
    name varchar(255)
);

INSERT INTO course1.groups
VALUES (1, 'test1'),
       (2, 'test2')