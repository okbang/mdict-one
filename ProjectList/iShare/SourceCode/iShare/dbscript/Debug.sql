SHOW VARIABLES WHERE Variable_name IN ('character_set_client', 'character_set_connection', 'character_set_results'); 


init-connect='SET NAMES utf8'
set character-set-server=utf8
collation-server=utf8_general_ci

drop user 'adminforum'@'localhost'
drop database jforum;

create database jforum default character set utf8;
CREATE USER 'adminforum'@'localhost' IDENTIFIED BY 'adminforum'; 
GRANT ALL PRIVILEGES ON jforum.* TO 'adminforum'@'localhost';        

set global character_set_server=utf8;
set global character_set_client=utf8;
set global character_set_connection=utf8;
set global character_set_results=utf8;
set global character_set_database=utf8;

SHOW VARIABLES LIKE 'character_set%';

