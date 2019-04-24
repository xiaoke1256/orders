package com.xiaoke1256.thirdpay.payplatform.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;

public interface ThirdPayOrderDao {
	public void save(ThirdPayOrder order) ;
	
	public void updateStatus (@Param("orderNo")String orderNo,@Param("orderStatus")String orderStatus,@Param("finishTime")Timestamp finishTime,@Param("updateTime")Timestamp updateTime) ;
	
	public ThirdPayOrder findByOrderNo (@Param("orderNo")String orderNo) ;
	
	public List<String> findOrderNosByLimitTime (@Param("orderStatus")String orderStatus,@Param("limitTime")Date limitTime) ;
}
