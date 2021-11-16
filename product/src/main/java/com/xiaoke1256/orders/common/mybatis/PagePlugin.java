package com.xiaoke1256.orders.common.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件
 */
@Intercepts({@Signature(type= StatementHandler.class,method = "prepare",args={Connection.class})})
public class PagePlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = (StatementHandler)invocation.getTarget();
        Object po = stmtHandler.getBoundSql().getParameterObject();
        if(po.getClass().isAssignableFrom(Map.class)){
            Map<String,Object> parameterObject = (Map<String,Object>)po;
            //每个value都拿出来看看是不是Page
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
