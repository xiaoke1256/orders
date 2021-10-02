package com.xiaoke1256.orders.product.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.product.bo.StoreMember;
import com.xiaoke1256.orders.product.dao.StoreMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.product.bo.Store;
import com.xiaoke1256.orders.product.dao.StoreDao;

@Service
@Transactional
public class StoreService {
	@Autowired
	private StoreDao storeDao;

	@Autowired
	private StoreMemberDao storeMemberDao;
	
	@Transactional(readOnly=true)
	public List<Store> queryAvailableStores() {
		return storeDao.queryAllStores();
		
	}
	
	@Transactional(readOnly=true)
	public Store getByStoreNo(String storeNo) {
		return storeDao.getByStoreNo(storeNo);
	}

	/**
	 * 创建一个店铺
	 */
	public void createStore(Store store,String userAccountNo){
		store.setStoreNo(genStoreNo());
		store.setInsertTime(new Timestamp(System.currentTimeMillis()));
		store.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		storeDao.saveStore(store);
		//店长
		StoreMember leader = new StoreMember();
		leader.setStoreNo(store.getStoreNo());
		leader.setAccountNo(userAccountNo);
		leader.setRole("1");//"1"代表店长
		StoreMember defaultStore = storeMemberDao.getDefaultStore(userAccountNo);
		if(defaultStore==null){
			leader.setIsDefaultStore("1");
		}else{
			leader.setIsDefaultStore("0");
		}
		leader.setInsertTime(new Timestamp(System.currentTimeMillis()));
		leader.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		storeMemberDao.saveStoreMember(leader);
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
}
