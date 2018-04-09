package com.agile.helper;

import com.agile.bean.FtpConfig;
import com.agile.bean.FtpConnection;
import com.agile.constant.SystemConstant;
import com.agile.joggle.abstractimpl.AbstractFileUploadHelper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;

/**
 * @Author: WuYL
 * @Description: FTP 方式操作文件实现
 * @Date: Create in 2018/4/8 11:47
 * @Modified By:
 */
public class FtpFileHelper extends AbstractFileUploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpFileHelper.class);

    private FTPClient ftpClient = null;

    private FtpConfig ftpConfig;

    private Integer replyCode;

    private String replyMessage;

    private FtpConnection ftpConnection = new FtpConnection(
            ConfigHelper.getHost(),
            ConfigHelper.getPort(),
            ConfigHelper.getUsername(),
            ConfigHelper.getPassword()
    );

    public FtpFileHelper(FtpConfig ftpConfig){
        super();
        this.ftpConfig = ftpConfig;
        initFtpClient(ftpConfig);
    }

    /**
     * 初始化ftp服务器
     */
    public void initFtpClient(FtpConfig ftpConfig) {
        ftpClient = new FTPClient();
        //设置编码
        ftpClient.setControlEncoding(ftpConfig.getEncoding());
        try {

            //连接ftp服务器
            ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
            //登录ftp服务器
            ftpClient.login(ftpConfig.getUsername(), ftpConfig.getPassword());

            //设置为2进制传输
            ftpClient.setFileType(ftpConfig.getTransferFileType());
            ftpClient.setBufferSize(ftpConfig.getBufferSize());

            if (ftpConfig.isPassiveMode()){
                //主动模式连接服务端（客户端随机端口连接服务端20端口, ）
                ftpClient.enterLocalActiveMode();
            }else{
                //被动模式连接服务端（服务端随机端口1024以上，告诉客户端，客户端连接）
                ftpClient.enterLocalPassiveMode();
            }
            ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
            //是否成功登录服务器
            setReply();
            if(!FTPReply.isPositiveCompletion(this.replyCode)){
                LOGGER.error("connect failed...ftp服务器 ["+ftpConfig.getHost()+":"+ftpConfig.getPort() + "]");
            }
            LOGGER.debug("connect successful...ftp服务器 ["+ftpConfig.getHost()+":"+ftpConfig.getPort() + "]");
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param pathName ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param originFileName 待上传文件的名称（绝对地址）
     * @return
     */
    public boolean uploadFile(String pathName, String fileName, String originFileName){
        boolean flag = false;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(originFileName));
            flag = uploadFileForStream(pathName, fileName, inputStream);
        }catch (FileNotFoundException e){
            LOGGER.error("file " + originFileName + " is not found.", e);
        }
        return flag;
    }

    /**
     * 上传文件
     * @param pathName ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 待上传文件流
     * @return
     */
    public boolean uploadFileForStream(String pathName, String fileName, InputStream inputStream){
        return this.uploadFileForStream(pathName, fileName, inputStream, ftpConfig.isOverwrite());
    }

    /**
     * 上传文件
     * @param pathName ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 待上传文件流
     * @return
     */
    private boolean uploadFileForStream(String pathName, String fileName, InputStream inputStream, boolean isOverwrite){
        boolean flag;
        try{
            if (!isOverwrite && existFile(pathName + SystemConstant.SEPARATOR + fileName)){
                LOGGER.debug("文件已经存在,不覆盖文件...");
                return true;
            }
            CreateDirecroty(pathName);
            ftpClient.makeDirectory(pathName);
            ftpClient.changeWorkingDirectory(pathName);
            flag = ftpClient.storeFile(fileName, inputStream);
        }catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }finally{
            setReply();
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 默认覆盖文件的方法（配置文件不好使）
     * @param pathName
     * @param fileName
     * @param inputStream
     * @return
     */
    public boolean defaultOverWriteUpload(String pathName, String fileName, InputStream inputStream){
        return this.uploadFileForStream(pathName, fileName, inputStream, true);
    }

    /**
     * 下载文件
     * @param pathName 要下载的文件路径
     * @param fileName 要下载的文件名称
     * @param localPath 下载到本地的哪个路径下
     * @return
     */
    public boolean downloadFile(String pathName, String fileName, String localPath){
        boolean flag = false;
        OutputStream os=null;
        try {
            //切换FTP目录
            File localFile = new File(localPath + SystemConstant.SEPARATOR + fileName);
            os = new FileOutputStream(localFile);
            flag = this.downloadFileForStream(pathName, fileName, os);
        } catch (Exception e) {
            LOGGER.error(" download file " + fileName + " is  failed", e);
        } finally{
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 下载文件
     * @param pathName
     * @param fileName
     * @param stream 输出流
     */
    private boolean downloadFileForStream(String pathName, String fileName, OutputStream stream){
        boolean flag = false;
        try {
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile file : ftpFiles) {
                    if (fileName.equalsIgnoreCase(file.getName())) {
                        flag = ftpClient.retrieveFile(file.getName(), stream);
                        stream.close();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(" download file " + fileName + " is  failed", e);
        } finally{
            setReply();
            if(null != stream){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     * @param remote
     * @return
     * @throws IOException
     */
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + SystemConstant.SEPARATOR;
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase(SystemConstant.SEPARATOR) && !ftpClient.changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith(SystemConstant.SEPARATOR)) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf(SystemConstant.SEPARATOR, start);
            StringBuilder path = new StringBuilder();
            StringBuilder paths = new StringBuilder();
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path.append(SystemConstant.SEPARATOR).append(subDirectory);
                if (!existFile(path.toString())) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        LOGGER.error("创建目录[\" + subDirectory + \"]失败");
                        ftpClient.changeWorkingDirectory(subDirectory);
                    }
                } else {
                    ftpClient.changeWorkingDirectory(subDirectory);
                }
                paths.append(SystemConstant.SEPARATOR).append(subDirectory);
//                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf(SystemConstant.SEPARATOR, start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    /**
     * 判断ftp服务器文件是否存在
     */
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    public void setReply(){
        this.replyCode = ftpClient.getReplyCode();
        this.replyMessage = ftpClient.getReplyString();
    }

    public Integer getReplyCode() {
        return replyCode;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    /**
     * 关闭的方法
     * @return
     */
    public boolean close(){
        boolean flag = false;
        try {
            if (this.ftpClient != null && this.ftpClient.isConnected()){
                flag = this.ftpClient.logout();
            }
        }catch (IOException e){
            LOGGER.error("ftpClient logout failed", e);
        }finally {
            if (flag){
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    flag = false;
                    LOGGER.error("close ftp connection is failed", e);
                }
            }
        }
        return flag;
    }

    /**
     * 验证的方法
     * @return
     */
    public boolean validate(){
        boolean connect = false;
        if (ftpClient != null){
            try {
                connect = ftpClient.sendNoOp();
//                if(connect){
//                    ftpClient.changeWorkingDirectory(ftpConfig.getWorkingDirectory());
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }

}
