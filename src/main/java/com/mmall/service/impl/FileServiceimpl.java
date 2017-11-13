package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceimpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceimpl.class);
    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
       logger.info("upload file name{},path:{},new file name:{}",fileName,path,uploadFileName);
        File filelDir = new File(path);
        if(!filelDir.exists()){
             filelDir.setWritable(true);
            filelDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try{
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        }catch(IOException e){
            logger.error("upload exception",e);
            return null;
        }
        return targetFile.getName();
    }
}