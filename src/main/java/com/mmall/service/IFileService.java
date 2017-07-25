package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-21:09
 */
public interface IFileService {

    //文件上传
    String upload(MultipartFile file, String path);
}
