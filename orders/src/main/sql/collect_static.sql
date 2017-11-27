exec dbms_stats.gather_table_stats('ORDERS','PAY_ORDER');
exec dbms_stats.gather_table_stats('ORDERS','SUB_ORDER');

exec dbms_stats.gather_index_stats('ORDERS','IDX_PAY_ORDER_PAYER_NO');
exec dbms_stats.gather_index_stats('ORDERS','IDX_SUB_ORDER_ORDER_ID');
--exec dbms_stats.gather_index_stats('ORDERS','IDX_SUB_ORDER_STORE_NO');