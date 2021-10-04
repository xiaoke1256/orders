package com.xiaoke1256.orders.product.api;

import com.xiaoke1256.orders.product.dto.Store;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 与商铺有关的业务
 * @author Administrator
 *
 */
public interface StoreService {
    /**
     * 获取有用的（未关闭的）商铺
     * */
    public List<Store> queryAvailableStore();

    /**
     * 根据商铺号查询商铺
     * @param storeNo
     * @return
     */
    public Store getStore(String storeNo);

    /**
     * 创建一个店铺
     * @param store
     * @param leaderAccount
     */
    public void createStore(Store store, String leaderAccount);

    /**
     * 修改一个店铺
     * @param store
     */
    public void updateStore(Store store);
}
