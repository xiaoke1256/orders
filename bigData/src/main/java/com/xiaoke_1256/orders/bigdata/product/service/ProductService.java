package com.xiaoke_1256.orders.bigdata.product.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataCalExecInfoDao;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataModelDao;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataClusterObjectMapDao;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfo;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterObjectMap;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult;
import com.xiaoke_1256.orders.bigdata.common.util.HdfsUtils;
import com.xiaoke_1256.orders.bigdata.common.util.ZIPUtils;
import com.xiaoke_1256.orders.bigdata.orders.dao.OrderItemDao;
import com.xiaoke_1256.orders.bigdata.product.dao.ProductDao;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductWithLabel;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductWithStatic;
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic;
import com.xiaoke_1256.orders.bigdata.product.model.Product;

import com.xiaoke_1256.orders.bigdata.product.service.spark.ProductClusterServiceBayesImpl;
import com.xiaoke_1256.orders.bigdata.product.service.spark.ProductClusterServiceKmeansImpl;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Value("${spark.hdfs.uri}")
    private String hdfsUri;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BigDataModelDao bigDataModelDao;

    @Autowired
    private BigDataCalExecInfoDao bigDataCalExecInfoDao;

    @Value("${big-data.tmp-div}")
    private String tmpDiv;

    @Autowired
    private ProductClusterServiceKmeansImpl productClusterServiceKmeans;

    @Autowired
    private ProductClusterServiceBayesImpl productClusterServiceBayes;

    @Autowired
    private BigDataClusterObjectMapDao bigDataClusterObjectMapDao;

    @Transactional(readOnly = true)
    public QueryResult<Product> searchProduct(ProductCondition productCondition){
        List<Product> list = productDao.search(productCondition);
        return new QueryResult<>( productCondition.getPageNo(), productCondition.getPageSize(), productCondition.getTotal(), list);
    }

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

    @Transactional(readOnly = true)
    public SimpleProductStatic productToSimpleProductStatic(Product p){
        int count = orderItemDao.countByProductCode(p.getProductCode());
        SimpleProductStatic simpleProductStatic = new SimpleProductStatic();
        BeanUtils.copyProperties(p,simpleProductStatic);
        simpleProductStatic.setOrderCount(count);
        return simpleProductStatic;
    }

    /**
     * 按条件查询商品
     * @param productCondition
     * @return
     */
    @Transactional(readOnly = true)
    public QueryResult<ProductWithLabel> searchByConditionWithLabel(ProductCondition productCondition, Long modelId){
        List<Product> list = productDao.search(productCondition);
        List<ProductWithLabel> resultList = new ArrayList<>();
        for(Product p:list){
            int count = orderItemDao.countByProductCode(p.getProductCode());
            ProductWithLabel productWithLabel = new ProductWithLabel();
            BeanUtils.copyProperties(p,productWithLabel);
            productWithLabel.setOrderCount(count);
            BigDataClusterObjectMap map = bigDataClusterObjectMapDao.selectByModelIdAndObjectId("01", p.getProductCode(), modelId);
            if(map!=null) {
                productWithLabel.setLabel(map.getLabel());
            }
            resultList.add(productWithLabel);
        }
        return new QueryResult<>( productCondition.getPageNo(), productCondition.getPageSize(), productCondition.getTotal(), resultList);
    }

    /**
     * 聚集算法(Kmeans)
     */
    @Transactional(readOnly = true)
    public String trainClusterKmeansModel(ProductCondition productCondition, int numClusters, int numIterator
            ,double productPriceCoefficient,double orderCountCoefficient) throws Exception {
        productCondition.setPageNo(1);
        productCondition.setPageSize(Integer.MAX_VALUE);
        List<ProductWithStatic> productList =  this.searchByCondition(productCondition).getResultList();
        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> new SimpleProductStatic(p.getProductCode(),p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());

        String path = tmpDiv + File.separator +"models" ;
        File dev = new File(path);
        if(!dev.exists()){
            dev.mkdirs();
        }
        path = tmpDiv + File.separator +"samples" ;
        dev = new File(path);
        if(!dev.exists()){
            dev.mkdirs();
        }

        String modelFilePath = tmpDiv + File.separator +"models" + File.separator + UUID.randomUUID() ;//模型文件目录；
        String sampleFilePath = tmpDiv + File.separator +"samples" + File.separator + UUID.randomUUID() + ".txt" ;//样本文件地址
        File sampleFile = new File(sampleFilePath);
        sampleFile.createNewFile();
        Writer write = new FileWriter(sampleFile);
        //写入样本数据
        for(int index=0;index < sampleList.size();index++){
            SimpleProductStatic product = sampleList.get(index);
            //index 1:price 2:count
            write.write((index+1)+" ");
            write.write("1:"+product.getProductPrice()+" ");
            write.write("2:"+product.getOrderCount());
            write.write("\r\n");
        }
        write.close();
        //上传至 hadoop
        String hdfsPath = sampleFilePath;
        if(hdfsPath.contains(":")){
            hdfsPath = hdfsPath.substring(hdfsPath.indexOf(":")+1);
        }
        hdfsPath = hdfsPath.replaceAll("\\\\","/");
        if(!hdfsPath.startsWith("/")){
            hdfsPath = "/"+hdfsPath;
        }
        HdfsUtils.upload(sampleFilePath,hdfsPath);
        logger.info("sampleFilePath:"+sampleFilePath);
        //模型文件的接收地址
        String hdfsModelFilePath = modelFilePath;
        if(hdfsModelFilePath.contains(":")){
            hdfsModelFilePath = hdfsModelFilePath.substring(hdfsModelFilePath.indexOf(":")+1);
        }
        hdfsModelFilePath = hdfsModelFilePath.replaceAll("\\\\","/");
        if(!hdfsModelFilePath.startsWith("/")){
            hdfsModelFilePath = "/"+hdfsModelFilePath;
        }
        productClusterServiceKmeans.trainModel(hdfsUri+"/"+hdfsPath,numClusters,numIterator,hdfsUri+"/"+hdfsModelFilePath,productPriceCoefficient,orderCountCoefficient);
        //训练完成后删除样本文件
        sampleFile.delete();
        //TODO 删除hdfs文件
        //模型地址
        return hdfsUri+hdfsModelFilePath;
    }

    /**
     * 聚集算法(Bayes)
     * @return
     */
    @Transactional(readOnly = true)
    public String trainClusterBayesModel(List<SimpleProductStatic> products) {
        String path = tmpDiv + File.separator +"models" ;
        File dev = new File(path);
        if(!dev.exists()){
            dev.mkdirs();
        }
        String modelFilePath = tmpDiv + File.separator +"models" + File.separator + UUID.randomUUID() ;
        productClusterServiceBayes.train(products,modelFilePath);
        return modelFilePath;
    }

    @Transactional(readOnly = true)
    public List<PredictResult<SimpleProductStatic>> predictKmeans(List<ProductWithStatic> productList , String modelPath
            , double productPriceCoefficient, double orderCountCoefficient) throws IOException, URISyntaxException {
        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> new SimpleProductStatic(p.getProductCode(),p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());
        String path = tmpDiv + File.separator +"samples" ;
        File dev = new File(path);
        if(!dev.exists()){
            dev.mkdirs();
        }

        String sampleFilePath = tmpDiv + File.separator +"samples" + File.separator + UUID.randomUUID() + ".txt" ;//样本文件地址
        String resultFilePath = tmpDiv + File.separator +"predict_result" + File.separator + UUID.randomUUID() + ".txt" ;//预测结果文件地址
        File sampleFile = new File(sampleFilePath);
        sampleFile.createNewFile();
        try(Writer write = new FileWriter(sampleFilePath)){
            //写入样本文件
            for(int i=0;i < sampleList.size();i++){
                SimpleProductStatic product = sampleList.get(i);
                //index 1:price 2:count
                write.write(i+" ");
                write.write("1:"+product.getProductPrice()+" ");
                write.write("2:"+product.getOrderCount());
                write.write("\r\n");
            }
        }
        //把样本文件上传至hdfs
        String hdfsPath = sampleFilePath;
        if(hdfsPath.contains(":")){
            hdfsPath = hdfsPath.substring(hdfsPath.indexOf(":")+1);
        }
        hdfsPath = hdfsPath.replaceAll("\\\\","/");
        if(!hdfsPath.startsWith("/")){
            hdfsPath = "/"+hdfsPath;
        }
        HdfsUtils.upload(sampleFilePath,hdfsPath);

        //结果文件的hdfs地址
        String hdfsResultPath = resultFilePath;
        if(hdfsResultPath.contains(":")){
            hdfsResultPath = hdfsResultPath.substring(hdfsResultPath.indexOf(":")+1);
        }
        hdfsResultPath = hdfsResultPath.replaceAll("\\\\","/");
        if(!hdfsResultPath.startsWith("/")){
            hdfsResultPath = "/"+hdfsResultPath;
        }

        productClusterServiceKmeans.predict(modelPath,hdfsUri+hdfsPath, hdfsUri+hdfsResultPath,productPriceCoefficient,orderCountCoefficient);
        //下载到本地
        HdfsUtils.download(false,hdfsUri+hdfsResultPath,resultFilePath);
        try(BufferedReader reader = new BufferedReader(new FileReader(resultFilePath))){
            List<PredictResult<SimpleProductStatic>> resultList = new ArrayList<>();
            String line = reader.readLine();
            while(line!=null){
                String[] perperties = line.split(" ");
                int index = (int)Double.parseDouble(perperties[0]);
                SimpleProductStatic product = sampleList.get(index);
                //约定把label放在最后一个字段。
                String label = perperties[perperties.length - 1];
                resultList.add(new PredictResult<SimpleProductStatic>(product,Integer.parseInt(label)));
                line = reader.readLine();
            }
            return resultList;
        }finally {
            //删除sample文件（本地）
            try{
                new File(sampleFilePath).delete();
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
            //删除sample文件（hdfs）
            //。。。。
            //删除resualt文件
            try{
                //new File(resultFilePath).delete();
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PredictResult<SimpleProductStatic>> predictBayes(List<Product> productList , String modelPath){
        List<SimpleProductStatic> sampleList = productList.stream().map((p) -> {
            SimpleProductStatic sample = new SimpleProductStatic();
            BeanUtils.copyProperties(p, sample);
            sample.setProductPrice(p.getProductPrice().doubleValue());
            int count = orderItemDao.countByProductCode(p.getProductCode());
            sample.setOrderCount(count);
            return sample;
        }).collect(Collectors.toList());
        return productClusterServiceBayes.predict(modelPath,sampleList);
    }

    @Transactional
    public void predictAndSave(ProductCondition productCondition , Long modelId) {
        BigDataModelWithBLOBs model = bigDataModelDao.getDetail(modelId);
        //输出到指定文件然后解压。
        String modelFilePath = tmpDiv + File.separator + "models" + File.separator + UUID.randomUUID();
        String zipFilePath = modelFilePath + ".zip";
        File zipFile = null;
        try{
            zipFile = new File(zipFilePath);
            boolean result = zipFile.createNewFile();
            OutputStream os = Files.newOutputStream(zipFile.toPath());
            os.write(model.getFileContent());
            ZIPUtils.unzip(zipFilePath,modelFilePath);
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }

        try {
            //先清理老数据
            List<BigDataCalExecInfo> oldExecs = bigDataCalExecInfoDao.queryByModelId(modelId);
            for(BigDataCalExecInfo oldExec:oldExecs){
                bigDataClusterObjectMapDao.deleteByExecId(oldExec.getExecId());
            }
            bigDataCalExecInfoDao.deleteByModelId(modelId);


            //用模型预测
            productCondition.setPageNo(1);
            productCondition.setPageSize(Integer.MAX_VALUE);
            List<ProductWithStatic> productList = this.searchByCondition(productCondition).getResultList();
            String trainParamJson = model.getTrainParam();
            JSONObject trainParamJsonObject = JSON.parseObject(trainParamJson);
            int productPriceCoefficient = trainParamJsonObject.getInteger("productPriceCoefficient");
            int orderCountCoefficient = trainParamJsonObject.getInteger("orderCountCoefficient");
            List<SimpleProductStatic> sampleList = productList.stream().map(p -> new SimpleProductStatic(p.getProductCode(), p.getProductName(), p.getProductPrice().doubleValue(), p.getOrderCount())).collect(Collectors.toList());
            List<PredictResult<SimpleProductStatic>> predictResults = this.predictKmeans(productList,modelFilePath,productPriceCoefficient,orderCountCoefficient);

            //保存到数据库
            //执行记录
            BigDataCalExecInfo execInfo = new BigDataCalExecInfo();
            execInfo.setModelId(model.getModelId());
            execInfo.setAlgorithmType(model.getAlgorithmType());
            execInfo.setObjectType("01");
            execInfo.setExecuteTime(new Date());
            bigDataCalExecInfoDao.save(execInfo);

            for(PredictResult<SimpleProductStatic> r:predictResults){
                BigDataClusterObjectMap clusterObjectMap = new BigDataClusterObjectMap();
                clusterObjectMap.setObjectType(execInfo.getObjectType());
                SimpleProductStatic p = r.getSample();
                clusterObjectMap.setObjectId(p.getProductCode());
                clusterObjectMap.setExecId(execInfo.getExecId());
                clusterObjectMap.setInsertTime(new Date());
                clusterObjectMap.setLabel(r.getLabel().toString());
                bigDataClusterObjectMapDao.save(clusterObjectMap);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } finally{
            //预测完后删除模型文件
            try {
                boolean result = zipFile.delete();
            }catch (Exception e){}
            try {
                FileUtils.deleteDirectory(new File(modelFilePath));
            }catch (Exception e){}
        }
    }
}
