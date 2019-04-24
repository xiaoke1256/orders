package com.xiaoke1256.thirdpay.payplatform.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;

public interface ThirdPayOrderDao {
	public void save(ThirdPayOrder order) ;
	
	public void updateStatus (String orderNo,String orderStatus,Timestamp finishTime,Timestamp updateTime) ;
	
	public ThirdPayOrder findByOrderNo (@Param("orderNo")String orderNo) ;
	
	public List<String> findOrderNosByLimitTime (@Param("orderStatus")String orderStatus,@Param("limitTime")Date limitTime) ;
}
