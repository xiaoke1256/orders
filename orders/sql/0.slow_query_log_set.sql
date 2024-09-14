-- 慢查询日志设置
show variables like 'slow_query_log%'

set GLOBAL slow_query_log=1
set GLOBAL long_query_time=0.2

SHOW VARIABLES LIKE 'slow_query_log_file';

-- 或在配置文件中如下配置
-- [mysqld]
-- slow_query_log = 1
-- slow_query_log_file = /var/lib/mysql/12912429fbd4-slow.log
-- long_query_time = 0.2