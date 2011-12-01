create database jforum default character set utf8;
CREATE USER 'adminforum'@'localhost' IDENTIFIED BY 'adminforum'; 
GRANT ALL PRIVILEGES ON jforum.* TO 'adminforum'@'localhost';        