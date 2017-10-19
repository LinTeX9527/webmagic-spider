

# 操作数据的SQL 语句 #
```SQL
SHOW DATABASES;

-- 删除数据库
DROP DATABASE sqltestdb;

-- 创建数据库 
CREATE DATABASE sqltestdb;


USE sqltestdb;

SHOW TABLES;


-- 创建表格 
CREATE TABLE emp (empno INT(4) PRIMARY KEY, ename VARCHAR(10), job VARCHAR(9), hiredate DATE, sal FLOAT(7,2)) CHARSET=utf8;

DESCRIBE emp;

-- 不用管终端显示乱码，经过Java 访问数据库的结果是正常的就行。 
LOAD DATA LOCAL INFILE 'F:\\temp\\mysql-test\\emp.txt' INTO TABLE emp LINES TERMINATED BY '\r\n';

SELECT * FROM emp;

-- 清空表格内容，但是表格依然存在 
DELETE FROM emp;

-- 删除表格，表格不存在 
DROP TABLE emp;
```

# 存放表格的文本 #
```
6060	李新华	经理	2001-09-16	2000.30
7369	张三	总监	2003-10-09 	1500.90
7698	王五	厂长	2005-03-12	800.00
7762	齐秦	书记	2005-03-09 	1000.00
7782	张刚	组长	2005-01-12 	2500.00
7839 	曹操	财务	2006-09-01 	2500.00
8964	李四	总裁	2003-10-01	3000.00
```