package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.api.StoreService;
import com.xiaoke1256.orders.product.dto.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory= StoreFallbackFactory.class,path="product")
public interface StoreClient {
    @RequestMapping(path="/store",method= RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void createStore(@RequestBody Store store, @RequestParam String leaderAccount);
}
