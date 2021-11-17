package com.xiaoke1256.orders.common.mybatis;

import com.xiaoke1256.orders.common.page.BaseCondition;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

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
            for(Object obj:parameterObject.values()){
                if(obj.getClass().isAssignableFrom(BaseCondition.class)){
                    //处理sql
                    MetaObject metaStamtHandler = SystemMetaObject.forObject(stmtHandler);
                    while(metaStamtHandler.hasGetter("h")){
                        Object object = metaStamtHandler.getValue("h");
                        metaStamtHandler = SystemMetaObject.forObject(stmtHandler);
                    }
                    while(metaStamtHandler.hasGetter("target")){
                        Object object = metaStamtHandler.getValue("target");
                        metaStamtHandler = SystemMetaObject.forObject(stmtHandler);
                    }
                    String sql = (String)metaStamtHandler.getValue("delegate.boundSql.sql");
                    sql = sql.trim();
                    String countSql = "select count(*) from ("+sql+")";
                    //获取
                    String pageSql = sql+" limit #{firstIndex},#{pageSize}";
                    metaStamtHandler.setValue("delegate.boundSql.sql",pageSql);
                    return invocation.proceed();
                }
            }
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
