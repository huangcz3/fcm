package com.asiainfo.fcm.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    /**
     * * 上传文件（可供Action/Controller层使用） * @param hostname FTP服务器地址 * @param port
     * FTP服务器端口号 * @param username FTP登录帐号 * @param password FTP登录密码 * @param
     * pathname FTP服务器保存目录 * @param fileName 上传到FTP服务器后的文件名称 * @param inputStream
     * 输入文件流 * @return
     */
    public static boolean uploadFile(String hostname, int port,
                                     String username, String password, String pathname, String fileName,
                                     InputStream inputStream) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //每次链接数据前 开启一个新的端口上传数据
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            // 是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            } else {
                logger.info("Ftp connect success!");
            }

            boolean setFileTypeOK = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            logger.info("setFileTypeOK:{}", setFileTypeOK);

//          boolean makeDirectoryOK = ftpClient.makeDirectory(pathname);
//          System.out.println("makeDirectoryOK:" + makeDirectoryOK);
//			boolean changeWorkingDirectoryOK=ftpClient.changeWorkingDirectory(pathname);
//			System.out.println("changeWorkingDirectoryOK:"+changeWorkingDirectoryOK);

            boolean isChangeDirectory = ftpClient.changeWorkingDirectory(pathname);

            logger.info("isChangeDirectory  [{}]", isChangeDirectory);
            if (isChangeDirectory) {
                boolean storeFileOK = ftpClient.storeFile(fileName, inputStream);

                logger.info("storeFileOK:{}", storeFileOK);

                inputStream.close();
                ftpClient.logout();
                flag = true;
            } else {
                throw new Exception("ChangeDirectory  failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    logger.error("关闭ftp连接异常", e);
                }
            }
        }
        return flag;
    }

    /**
     * * 上传文件（可对文件进行重命名） * @param hostname FTP服务器地址 * @param port FTP服务器端口号 * @param
     * username FTP登录帐号 * @param password FTP登录密码 * @param pathname FTP服务器保存目录 * @param
     * filename 上传到FTP服务器后的文件名称 * @param originfilename 待上传文件的名称（绝对地址） * @return
     */
    public static boolean uploadFileFromProduction(String hostname, int port,
                                                   String username, String password, String pathname, String filename,
                                                   String originfilename) {
        boolean flag = false;
        try {
            InputStream inputStream = new FileInputStream(new File(
                    originfilename));
            /*flag = uploadFile(hostname, port, username, password, pathname,
                    filename, inputStream,originfilename);*/
            System.out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * * 上传文件（可供Action/Controller层使用） * @param hostname FTP服务器地址 * @param port
     * FTP服务器端口号 * @param username FTP登录帐号 * @param password FTP登录密码 * @param
     * pathname FTP服务器保存目录 * @param fileName 上传到FTP服务器后的文件名称 * @param inputStream
     * 输入文件流 * @return
     */
    public static List<Map<String, InputStream>> downloadFile(String hostname, int port,
                                                              String username, String password, String pathname) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        List<Map<String, InputStream>> list = new ArrayList<Map<String, InputStream>>();
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            // 是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("connect fail.");
                return null;
            } else {
                System.out.println("Ftp connect success!");
            }
            boolean setFileTypeOK = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            System.out.println("setFileTypeOK:" + setFileTypeOK);
            boolean changeWorkingDirectoryOK = ftpClient.changeWorkingDirectory(pathname);
            System.out.println("changeWorkingDirectoryOK:" + changeWorkingDirectoryOK);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] fs = ftpClient.listFiles(pathname);
            for (FTPFile f : fs) {
                Map<String, InputStream> mapIs = new HashMap<String, InputStream>();
                String name = f.getName();
                InputStream is = ftpClient.retrieveFileStream(name);
                ftpClient.completePendingCommand();
                if (is == null) {
                    System.out.println("ftp获取空文件:" + name);
                } else {
                    mapIs.put(name, is);
                    System.out.println("ftp获取文件:" + name);
                    list.add(mapIs);
                }
            }
            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 下载文件
     */
    public static boolean downloadFilebyFilename(String hostname, int port,
                                                 String username, String password, String pathname, String filename, String localFilepath, String localFilename) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        boolean success = false;
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            // 是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("connect fail.");
                return success;
            } else {
                System.out.println("Ftp connect success!");
            }
            boolean setFileTypeOK = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            System.out.println("setFileTypeOK:" + setFileTypeOK);
            boolean changeWorkingDirectoryOK = ftpClient.changeWorkingDirectory(pathname);
            System.out.println("changeWorkingDirectoryOK:" + changeWorkingDirectoryOK);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] fs = ftpClient.listFiles(pathname);
            for (FTPFile f : fs) {
                if (f.getName().equals(filename)) {
                    InputStream is = ftpClient.retrieveFileStream(f.getName());
                    FileUtil.copyStream(is, new FileOutputStream(localFilepath + File.separator + localFilename));
                    ftpClient.completePendingCommand();
                    if (is == null) {
                        System.out.println("ftp获取空文件:" + f.getName());
                    } else {
                        System.out.println("ftp获取文件:" + f.getName());
                    }
                }
            }
            ftpClient.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }

}
