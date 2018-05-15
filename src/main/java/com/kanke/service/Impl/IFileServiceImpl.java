package com.kanke.service.Impl;

import com.kanke.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service("iFileService")
@Slf4j
public class IFileServiceImpl implements IFileService{


    public String upload(MultipartFile file , String path){
        String fileName =file.getOriginalFilename();
        //获得文件的扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        //防止文件名重复
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("开始上传文件，上传文件的文件名：{}，上传文件的路径：{}，新文件名：{}",fileName,path,uploadFileName);
        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);//赋予权限
            fileDir.mkdirs();//创建临时目录
        }
        //创建文件
        File targetfile=new File(path,uploadFileName);

        try {
            file.transferTo(targetfile);
            //文件上传成功

//            //将文件上传到ftp
//            FTPUtil.uploadFile(Lists.newArrayList(targetfile));

            //将文件上传到upload
            targetfile.delete();

        } catch (IOException e) {
            log.error("文件上传异常",e);
            return null;
        }
        return targetfile.getName();
    }
}

