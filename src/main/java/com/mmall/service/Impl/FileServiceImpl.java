package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-21:10
 */
@Service
public class FileServiceImpl implements IFileService{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        //原始文件名
        String fileName = file.getOriginalFilename();
        //扩展名 jpg,不含点
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        LOGGER.info("开始上传文件,上传文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件上传成功

            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器
            targetFile.delete();
        } catch (IOException e) {
            LOGGER.error("上传文件异常",e);
            return null;
        }

        return targetFile.getName();
    }


}
