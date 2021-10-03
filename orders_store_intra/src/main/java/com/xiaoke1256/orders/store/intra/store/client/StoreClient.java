package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.api.StoreService;
import com.xiaoke1256.orders.product.dto.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory= StoreFallbackFactory.class,path="product")
public interface StoreClient {
    @RequestMapping(path="/store",method= RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void createStore(@RequestBody Store store, @RequestParam String leaderAccount);

    @RequestMapping(value="/store/{storeNo}",method=RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Store getStore(@PathVariable("storeNo") String storeNo);
}
