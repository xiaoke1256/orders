package com.xiaoke_1256.orders.bigdata.product.service;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataModelDao;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult;
import com.xiaoke_1256.orders.bigdata.orders.dao.OrderItemDao;
import com.xiaoke_1256.orders.bigdata.product.dao.ProductDao;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductWithStatic;
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import com.xiaoke_1256.orders.bigdata.product.service.impl.ProductClusterServiceKmeansImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BigDataModelDao bigDataModelDao;

    @Value("${big-data.tmp-div}")
    private String tmpDiv;

    @Autowired
    private ProductClusterServiceKmeansImpl productClusterServiceKmeans;

    /**
     * 按条件查询商品
     * @param productCondition
     * @return
     */
    @Transactional(readOnly = true)
    public QueryResult<ProductWithStatic> searchByCondition(ProductCondition productCondition){
        List<Product> list = productDao.search(productCondition);
        List<ProductWithStatic> resultList = new ArrayList<>();
        for(Product p:list){
            int count = orderItemDao.countByProductCode(p.getProductCode());
            ProductWithStatic productWithStatic = new ProductWithStatic();
            BeanUtils.copyProperties(p,productWithStatic);
            productWithStatic.setOrderCount(count);
            resultList.add(productWithStatic);
        }
        return new QueryResult<>( productCondition.getPageNo(), productCondition.getPageSize(), productCondition.getTotal(), resultList);
    }

    /**
     * 聚集算法
     */
    @Transactional(readOnly = true)
    public String trainClusterModel(ProductCondition productCondition, int numClusters, int numIterator
            ,double productPriceCoefficient,double orderCountCoefficient) {
        productCondition.setPageNo(1);
        productCondition.setPageSize(Integer.MAX_VALUE);
        List<ProductWithStatic> productList =  this.searchByCondition(productCondition).getResultList();
        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> new SimpleProductStatic(p.getProductCode(),p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());

        String path = tmpDiv + File.separator +"models" ;
        File dev = new File(path);
        if(!dev.exists()){
            dev.mkdirs();
        }
        String modelFilePath = tmpDiv + File.separator +"models" + File.separator + UUID.randomUUID() ;//模型文件地址；
        productClusterServiceKmeans.trainModel(sampleList,numClusters,numIterator,modelFilePath,productPriceCoefficient,orderCountCoefficient);

        //模型
        return modelFilePath;
    }

    @Transactional(readOnly = true)
    public List<PredictResult<SimpleProductStatic>> predict(List<ProductWithStatic> productList , String modelPath
            ,double productPriceCoefficient,double orderCountCoefficient){
        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> new SimpleProductStatic(p.getProductCode(),p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());
        return productClusterServiceKmeans.predict(modelPath,sampleList,productPriceCoefficient,orderCountCoefficient);
    }

    public void predictAndSave(ProductCondition productCondition , Long modelId) {
        BigDataModelWithBLOBs model = bigDataModelDao.getDetail(modelId);
        //输出到指定文件然后解压。
        String modelFilePath = tmpDiv + File.separator + "models" + File.separator + UUID.randomUUID();
        String zipFilePath = modelFilePath + ".zip";
        try{
            File zipFile = new File(zipFilePath);
            boolean result = zipFile.createNewFile();
            OutputStream os = Files.newOutputStream(zipFile.toPath());
            os.write(model.getFileContent());
        }catch (IOException ex){

        }

//        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> new SimpleProductStatic(p.getProductCode(),p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());
//        return productClusterServiceKmeans.predict(modelPath,sampleList,productPriceCoefficient,orderCountCoefficient);
    }
}