package com.xiaoke1256.orders.search.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDaoImpl {
	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate; 
}
