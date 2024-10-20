DROP DATABASE if EXISTS AGENT_DB;
CREATE DATABASE AGENT_DB;
USE AGENT_DB;


create table Agent(
agent_id INT primary key auto_increment,
agency_code VARCHAR(50) NOT NULL UNIQUE,
role VARCHAR(50) NOT NULL,
agent_name VARCHAR(50) NOT NULL,
password VARCHAR(100),
mpin VARCHAR(5)
);


INSERT INTO Agent values(1, 9009009001, "Agent", "Bob", "$2a$10$fsu.W4G3FwzPJx2PwM5Gae6cIikwwGzQSZ1nssL3AYIqf6h.QUk9k", 1234);


DROP DATABASE IF EXISTS CUSTOMER_DB;
CREATE DATABASE CUSTOMER_DB;
USE CUSTOMER_DB;

Create table Customer(
customer_id INT primary key auto_increment,
customer_name VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
dob DATE NOT NULL,
mobile_no VARCHAR(10) NOT NULL UNIQUE,
agent_id INT,
policy_id INT
);

INSERT INTO Customer values(1, "Susan", "susan123@ify.com", "1998-02-10", 9009009010, 1, 1);



DROP DATABASE if exists POLICY_DB;
CREATE DATABASE POLICY_DB;
USE POLICY_DB;

Create table Policy(
policy_id INT primary key auto_increment,
policy_type VARCHAR(50) NOT NULL,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
duration INT
);

INSERT INTO Policy VALUES(1, "REGULAR", "2023-10-10", "2024-10-10", 12);