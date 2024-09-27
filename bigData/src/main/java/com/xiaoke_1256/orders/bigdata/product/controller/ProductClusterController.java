package com.xiaoke_1256.orders.bigdata.product.controller;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.TrainInput;
import com.xiaoke_1256.orders.bigdata.product.dto.*;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import com.xiaoke_1256.orders.bigdata.product.service.spark.ProductClusterService;
import com.xiaoke_1256.orders.bigdata.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/searchProductWithLabel")
    public QueryResult<ProductWithLabel> searchProductWithLabel(ProductCondition productCondition,Long modelId){
        return productService.searchByConditionWithLabel(productCondition,modelId);
    }

    /**
     * 训练聚类模型
     * @param trainInput 训练输入参数
     * @
     */
    @PostMapping("/cluster/kmeans/train")
    public Map<String,String> trainCluster( @RequestBody TrainInput trainInput) throws Exception {
        logger.debug("trainInput:"+trainInput);
        String modelPath = productService.trainClusterKmeansModel(trainInput.getCondition(),
                trainInput.getNumClusters(),
                trainInput.getNumIterator(),
                trainInput.getProductPriceCoefficient(),
                trainInput.getOrderCountCoefficient());
        //把模型参数放到session里
        return new HashMap<String,String>(){{
            put("modelPath",modelPath);
        }};
    }

    /**
     * 训练聚类模型
     * @param trainInput 训练输入参数
     * @
     */
    @PostMapping("/cluster/bayes/train")
    public Map<String,String> trainBayesCluster( @RequestBody TrainInput trainInput){
        logger.debug("trainInput:"+trainInput);
        trainInput.getCondition().setPageNo(1);
        trainInput.getCondition().setPageSize(Integer.MAX_VALUE);
        List<ProductWithLabel> products = productService.searchByConditionWithLabel(trainInput.getCondition(),trainInput.getClusterFormulaModelId())
                .getResultList();
        List<SimpleProductStatic> samples = products.stream().map(p -> new SimpleProductStatic(p.getLabel(), p.getProductCode(), p.getProductName(), p.getBrand(), p.getStoreNo())).collect(Collectors.toList());
        String modelPath = productService.trainClusterBayesModel(samples);
        //把模型参数放到session里
        return new HashMap<String,String>(){{
            put("modelPath",modelPath);
        }};
    }

    @PostMapping("/cluster/kmeans/predict")
    public List<PredictResult<SimpleProductStatic>> predict(@RequestBody ProductPredictInput predictInput ) throws IOException, URISyntaxException {
        predictInput.getCondition().setPageNo(1);
        predictInput.getCondition().setPageSize(Integer.MAX_VALUE);
        QueryResult<ProductWithStatic> products = productService.searchByCondition(predictInput.getCondition());
        return productService.predictKmeans(products.getResultList(),predictInput.getModelPath(),
                predictInput.getProductPriceCoefficient(),predictInput.getOrderCountCoefficient());
    }

    @PostMapping("/cluster/bayes/predict")
    public List<PredictResult<SimpleProductStatic>> predictBayes(@RequestBody ProductPredictInput predictInput ){
        predictInput.getCondition().setPageNo(1);
        predictInput.getCondition().setPageSize(Integer.MAX_VALUE);
        QueryResult<Product> products = productService.searchProduct(predictInput.getCondition());
//        List<SimpleProductStatic> samples = products.getResultList().stream().map(p -> {
//            SimpleProductStatic sample = new SimpleProductStatic(null, p.getProductCode(), p.getProductName(), p.getBrand(), p.getStoreNo());
//            sample.setProductPrice(p.getProductPrice().doubleValue());
//            sample.setOrderCount();
//        }).collect(Collectors.toList());
        List<PredictResult<SimpleProductStatic>> predictList = productService.predictBayes(products.getResultList(), predictInput.getModelPath());
        return predictList;
    }

    @PostMapping("/cluster/predictAndSave")
    public Boolean predictAndSave(@RequestBody ProductPredictInput predictInput ){
        productService.predictAndSave(predictInput.getCondition(),predictInput.getModelId());
        return Boolean.TRUE;
    }

}
