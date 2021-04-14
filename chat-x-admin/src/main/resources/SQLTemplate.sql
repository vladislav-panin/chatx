
# su postgres
# psql
# CREATE ROLE vlad WITH SUPERUSER LOGIN ENCRYPTED PASSWORD 'some_password' NOINHERIT VALID UNTIL 'infinity';
# CREATE DATABASE vlad WITH ENCODING='UTF8' OWNER=vlad;

CREATE TABLE admins(id serial, nick VARCHAR(64) UNIQUE NOT NULL, first_name VARCHAR(64) NOT NULL, last_name VARCHAR(64), email VARCHAR(64) NOT NULL, is_blocked INTEGER NOT NULL, pass VARCHAR(64));
INSERT INTO admins(nick, first_name, last_name, email, is_blocked, pass) VALUES('admin', 'Vlad', 'Panin', 'some@email.ru', 0, 'admin');
SELECT id, nick, first_name, last_name, email, is_blocked FROM admins WHERE nick='%s' AND pass='%s';