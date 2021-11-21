package com.xiaoke1256.orders.common.mybatis;

import com.xiaoke1256.orders.common.page.BaseCondition;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件
 */
@Intercepts({@Signature(type= StatementHandler.class,method = "prepare",args={Connection.class,Integer.class})})
public class PagePlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = (StatementHandler)invocation.getTarget();
        Object parameterObject = stmtHandler.getBoundSql().getParameterObject();
        BaseCondition pageParamter = getPageParamter(parameterObject);
        if(pageParamter==null){
            return invocation.proceed();
        }

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
        if(sql.toLowerCase().indexOf("select")!=0){
            return invocation.proceed();
        }
        int total = 0;
        String countSql = "select count(*) as total from ("+sql+") $_paging_table";
        Connection conn = (Connection)invocation.getArgs()[0];
        MappedStatement mappedStatement = (MappedStatement) metaStamtHandler.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaStamtHandler.getValue("delegate.boundSql");
        Configuration configuration = mappedStatement.getConfiguration();
        PreparedStatement ps = conn.prepareStatement(countSql);
        BoundSql countBoundSql = new BoundSql(configuration, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        ParameterHandler handler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
        handler.setParameters(ps);
        //执行查询
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()){
            total = resultSet.getInt("total");
        }
        pageParamter.setTotal(total);
        //获取分页SQL
        int getPageNo = Math.max(pageParamter.getPageNo(),1);
        int pageSize = pageParamter.getPageSize();
        String pageSql = sql+" limit " +(getPageNo-1)*pageSize + "," + pageSize;
        metaStamtHandler.setValue("delegate.boundSql.sql",pageSql);
        return invocation.proceed();
    }

    private BaseCondition getPageParamter(Object parameterObject){
        if(parameterObject==null){
            return null;
        }else if(parameterObject instanceof BaseCondition){
            return (BaseCondition)parameterObject;
        } else if(parameterObject instanceof Map ){
            Map<String,Object> parameterMap = (Map<String,Object>)parameterObject;
            for(Object value:parameterMap.values()){
                if(value instanceof BaseCondition){
                    return (BaseCondition)value;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
