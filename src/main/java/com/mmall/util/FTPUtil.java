package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * FTP上传工具类
 * Created by tony on 2017/6/21.
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");

    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");

    private static String ftpPwd = PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, int port, String user, String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    private String ip;

    private int port;

    private String user;

    private String pwd;

    private FTPClient ftpClient;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * 提供外部调用的上传文件方法
     * @param fileList
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPwd);
        logger.info("开始连接FTP服务器...");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接FTP服务器，结束上传，上传结果{}", result);
        return result;
    }

    /**
     * 上传文件到FTP服务器
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if(connectServer(this.ip, this.port, this.user, this.pwd)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);       //更改上传工作空间
                ftpClient.setBufferSize(1024);                      //设置缓冲区大小
                ftpClient.setControlEncoding("UTF-8");              //设置文件内容编码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  //文件为二进制编码
                ftpClient.enterLocalPassiveMode();                  //进入被动模式
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);   //存储文件
                }
            } catch (IOException e) {
                logger.error("文件上传异常", e);
                uploaded = false;
                e.printStackTrace();
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 连接FTP服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd){
        boolean isSuccess = true;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
            isSuccess = false;
        }
        return isSuccess;
    }
}
