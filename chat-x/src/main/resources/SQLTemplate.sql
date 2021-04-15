
# su postgres
# psql
# CREATE ROLE vlad WITH SUPERUSER LOGIN ENCRYPTED PASSWORD 'some_password' NOINHERIT VALID UNTIL 'infinity';
# CREATE DATABASE vlad WITH ENCODING='UTF8' OWNER=vlad;

CREATE TABLE users(id serial, nick VARCHAR(64) UNIQUE NOT NULL, first_name VARCHAR(64) NOT NULL, last_name VARCHAR(64) NOT NULL, email VARCHAR(64) UNIQUE NOT NULL, is_blocked INTEGER NOT NULL, pass VARCHAR(64) NOT NULL);
INSERT INTO users(nick, first_name, last_name, email, is_blocked, pass) VALUES('user', 'Vlad', 'Panin', 'some@email.ru', 0, 'user');
SELECT id, nick, first_name, last_name, email, is_blocked FROM users WHERE nick='%s' AND pass='%s';