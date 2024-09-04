package com.xiaoke_1256.orders.bigdata.aimode.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoke_1256.orders.bigdata.aimode.dto.BigDataModelDto;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import com.xiaoke_1256.orders.bigdata.aimode.service.BigDataModelService;
import com.xiaoke_1256.orders.bigdata.common.util.ZIPUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RequestMapping("/model")
@RestController
public class BigDataModelController {

    @Autowired
    private BigDataModelService bigDataModelService;

    @PostMapping("/save")
    public Boolean saveModel(@RequestBody BigDataModelDto modelDto) throws IOException {
        String modelPath = modelDto.getModelPath();
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

}
