package com.xiaoke1256.orders.common.mybatis.repository.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke1256.orders.common.mybatis.repository.IRepository;

public class CrudRepository<Mapper extends BaseMapper<Entity>,Entity> extends ServiceImpl<Mapper, Entity> implements IRepository<Entity> {
}
