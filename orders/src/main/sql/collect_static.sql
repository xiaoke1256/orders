exec dbms_stats.gather_table_stats('TRANS_TEST','PAY_ORDER');
exec dbms_stats.gather_table_stats('TRANS_TEST','SUB_ORDER');

exec dbms_stats.gather_index_stats('TRANS_TEST','IDX_PAY_ORDER_PAYER_NO');
exec dbms_stats.gather_index_stats('TRANS_TEST','IDX_SUB_ORDER_ORDER_NO');
exec dbms_stats.gather_index_stats('TRANS_TEST','IDX_SUB_ORDER_STORE_NO');