package com.xiaoke_1256.orders.bigdata.product.controller;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.TrainInput;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductPredictInput;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductWithStatic;
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import com.xiaoke_1256.orders.bigdata.product.service.ProductClusterService;
import com.xiaoke_1256.orders.bigdata.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductClusterController {

    private static final Logger logger = LoggerFactory.getLogger(ProductClusterController.class);

    @Autowired
    private ProductClusterService productClusterService;

    @Autowired
    private ProductService productService;

    @GetMapping("helloWord")
    public String helloWord(){
        return productClusterService.hello();
    }

    @GetMapping("/searchProduct")
    public QueryResult<ProductWithStatic> searchProduct(ProductCondition productCondition){
        return productService.searchByCondition(productCondition);
    }

    /**
     * 训练聚类模型
     * @param trainInput 训练输入参数
     * @
     */
    @PostMapping("/cluster/train")
    public Map<String,String> trainCluster( @RequestBody TrainInput trainInput){
        logger.debug("trainInput:"+trainInput);
        String modelPath = productService.trainClusterModel(trainInput.getCondition(),
                trainInput.getNumClusters(),
                trainInput.getNumIterator(),
                trainInput.getProductPriceCoefficient(),
                trainInput.getOrderCountCoefficient());
        //把模型参数放到session里
        return new HashMap<String,String>(){{
            put("modelPath",modelPath);
        }};
    }

    @PostMapping("/cluster/predict")
    public List<PredictResult<SimpleProductStatic>> predict(@RequestBody ProductPredictInput predictInput ){
        predictInput.getCondition().setPageNo(1);
        predictInput.getCondition().setPageSize(Integer.MAX_VALUE);
        QueryResult<ProductWithStatic> products = productService.searchByCondition(predictInput.getCondition());
        return productService.predict(products.getResultList(),predictInput.getModelPath(),
                predictInput.getProductPriceCoefficient(),predictInput.getOrderCountCoefficient());
    }

    /**
     * 保存模型
     * @return
     */
    @PostMapping("/cluster/saveModel")
    public Boolean saveModel(){
        return true;
    }

}
