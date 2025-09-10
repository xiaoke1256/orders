package com.xiaoke1256.orders.product.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.product.assembler.StoreAssembler;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import com.xiaoke1256.orders.product.entity.StoreMemberEntity;
import com.xiaoke1256.orders.product.repository.IStoreMemberRepository;
import com.xiaoke1256.orders.product.repository.IStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.product.domain.Store;

@Service
@Transactional
public class StoreService {

	@Autowired
	private IStoreRepository storeRepository;

	@Autowired
	private IStoreMemberRepository storeMemberRepository;
	
	@Transactional(readOnly=true)
	public List<Store> queryAvailableStores() {
		List<StoreEntity> entities = storeRepository.list();
		return entities.stream().map((e)-> StoreAssembler.toDomain(e)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly=true)
	public Store getByStoreNo(String storeNo) {
		return StoreAssembler.toDomain(storeRepository.getByStoreNo(storeNo));
	}

	/**
	 * 创建一个店铺
	 */
	public void createStore(Store store,String userAccountNo){
		store.setStoreNo(genStoreNo());
		store.setInsertTime(new Timestamp(System.currentTimeMillis()));
		store.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		storeRepository.saveStore(StoreAssembler.toEntity(store));
		//店长
		StoreMemberEntity leader = new StoreMemberEntity();
		leader.setStoreNo(store.getStoreNo());
		leader.setAccountNo(userAccountNo);
		leader.setRole("1");//"1"代表店长
		StoreMemberEntity defaultStore = storeMemberRepository.getDefaultStore(userAccountNo);
		if(defaultStore==null){
			leader.setIsDefaultStore("1");
		}else{
			leader.setIsDefaultStore("0");
		}
		leader.setInsertTime(new Timestamp(System.currentTimeMillis()));
		leader.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		storeMemberRepository.saveStoreMember(leader);
	}

	/**
	 * 生成店铺号
	 * 店铺号规则:年月+4位流水号。
	 * @return
	 */
	private String genStoreNo() {
		String yearMonth = DateUtil.format(new Date(), "yyyyMM");
		StringBuilder storeNo = new StringBuilder();
		long nanoSecode = System.nanoTime();//这是纳秒。
		storeNo.append(yearMonth).append(nanoSecode%10000);
        return storeNo.toString();
	}

	/**
	 * 保存一个店铺
	 * @param store
	 */
	public void updateStore(Store store){
		store.setInsertTime(null);
		store.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		storeRepository.updateStore(StoreAssembler.toEntity(store));
	}
}
