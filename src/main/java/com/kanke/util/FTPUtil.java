package com.kanke.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {
    public static Logger logger= LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp=PropertiesUtil.getProperties("ftp.server.ip");
    private static String ftpUser=PropertiesUtil.getProperties("ftp.user");
    private static String ftpPass=PropertiesUtil.getProperties("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil(String ip,int port,String user,String password){
        this.ip=ip;
        this.port=port;
        this.user=user;
        this.password=password;
    }

    /**
     * 直接上传
     * @param fileList
     * @return
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);

        logger.info("开始连接ftp服务器");

        boolean result=ftpUtil.uploadFile("img",fileList);

        logger.info("开始连接ftp服务器，结束上传，上传结果：{}");

        return result;
    }

    /**
     *当上面无法上传时，通过这个上传
     * @param remotePath
     * @param fileList
     * @return
     */
    private  boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded=true;//是否上传了
        FileInputStream fileInputStream=null;
        //连接ftp服务器
        if(connectServer(this.getIp(),this.getPort(),this.getUser(),this.getPassword())){
            try {
                //改变工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                //设置缓冲区
                ftpClient.setBufferSize(1024);
                //设置格式
                ftpClient.setControlEncoding("UTF-8");
                //设置文件类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //开始上传
                ftpClient.enterLocalPassiveMode();
                for (File fileItem:fileList){
                    fileInputStream= new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fileInputStream);
                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded =false;
                e.printStackTrace();
            }finally {

                    fileInputStream.close();
                    ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 连接服务器
     * @param ip
     * @param port
     * @param user
     * @param password
     * @return
     */
    private boolean connectServer(String ip,int port,String user,String password){

        boolean isSuccess=false;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess=ftpClient.login(user,password);
        } catch (IOException e) {
            logger.error("ftp登录异常",e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
