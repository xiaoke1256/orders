package test.com.orders.client;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.OrdersApplication;
import com.xiaoke1256.orders.core.client.ProductQueryGrpcClient;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes= OrdersApplication.class)
public class ProductQueryGrpcClientTest {

    @Autowired
    private ProductQueryGrpcClient productQueryGrpcClient;

    @Test
    public void testGetProduct(){
        SimpleProduct product = productQueryGrpcClient.getSimpleProductByCode("0006100403");
        System.out.println("product:"+JSON.toJSON(product));
    }
}
