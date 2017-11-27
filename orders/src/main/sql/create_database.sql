create tablespace orders logging   
datafile 'I:\app\oracle\oradata\orclfrk\orders.dbf'  size 128m   
autoextend on   next 32m maxsize 2048m   extent management local; 

create user orders
  identified by oracle
  default tablespace orders
  temporary tablespace TEMP
  profile DEFAULT;
-- Grant/Revoke role privileges 
grant connect to orders;
grant dba to orders;
grant resource to orders;
-- Grant/Revoke system privileges 
grant unlimited tablespace to orders;

alter system set processes=1000 scope=spfile;  
alter system set sessions=1105 scope=spfile;