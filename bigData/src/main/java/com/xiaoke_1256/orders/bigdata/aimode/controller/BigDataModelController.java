package com.xiaoke_1256.orders.bigdata.aimode.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dto.BigDataModelDto;
import com.xiaoke_1256.orders.bigdata.aimode.dto.ModelSearchCondition;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModel;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import com.xiaoke_1256.orders.bigdata.aimode.service.BigDataModelService;
import com.xiaoke_1256.orders.bigdata.common.util.HdfsUtils;
import com.xiaoke_1256.orders.bigdata.common.util.ZIPUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/model")
@RestController
public class BigDataModelController {

    @Value("${big-data.tmp-div}")
    private String tmpDiv;

    @Autowired
    private BigDataModelService bigDataModelService;

    /**
     * 查询模型
     * @param condition
     * @return
     */
    @GetMapping("/search")
    public QueryResult<BigDataModel> search(ModelSearchCondition condition){
        return bigDataModelService.searchModel(condition);
    }

    /**
     * 保存模型
     * @param modelDto
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public Boolean saveModel(@RequestBody BigDataModelDto modelDto) throws IOException, URISyntaxException {
        String modelPath = modelDto.getModelPath();
        if(modelPath.startsWith("hdfs:")){
            String localPath = tmpDiv + File.separator +"models" + File.separator + UUID.randomUUID() ;
            new File(localPath).mkdirs();
            //先下载到本地
            HdfsUtils.downloadFiles(modelPath,localPath);
            modelPath = localPath;
        }
        String zipFileName = modelPath + ".zip";
        ZIPUtils.createZip(zipFileName,modelPath);
        File zipfile = new File(zipFileName);
        BigDataModelWithBLOBs model = new BigDataModelWithBLOBs();
        model.setModelName(modelDto.getModelName());
        model.setModelDesc(modelDto.getModelDesc());
        model.setModelType(modelDto.getModelType());
        model.setAlgorithmType(modelDto.getAlgorithmType());
        model.setTrainParam(JSON.toJSONString(modelDto.getTrainParam()));
        model.setClassDefine(JSON.toJSONString(modelDto.getClassDefine()) );
        model.setCreateBy("admin");//TODO 暂时写死
        byte[] zipContent = FileUtils.readFileToByteArray(zipfile);
        model.setFileContent(zipContent);
        bigDataModelService.saveModel(model);
        boolean result = zipfile.delete();
        FileUtils.deleteDirectory(new File(modelPath));
        return true;
    }

    /**
     * 查询全部模型
     * @return
     */
    @GetMapping("/findAll")
    public List<BigDataModel> findAll(){
        return bigDataModelService.findAll();
    }

    @GetMapping("/getClusterMap")
    public Map<String,String> getClusterMap(Long modelId){
        return bigDataModelService.getClusterDic(modelId);
    }

}
