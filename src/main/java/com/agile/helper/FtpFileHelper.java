package com.agile.helper;

import com.agile.DownloadStatus;
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

    private FTPClient ftpClient;

    private FtpConfig ftpConfig;

    private Integer replyCode;

    private String replyMessage;

    private long downloadProcess;

    public FtpFileHelper(FtpConfig ftpConfig){
        super();
        this.ftpConfig = ftpConfig;
        initFtpClient(ftpConfig);
    }

    /**
     * 初始化ftp客户端
     */
    private void initFtpClient(FtpConfig ftpConfig) {
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
            if (!ftpConfig.isPassiveMode()){
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
                LOGGER.debug("文件 [" + fileName + "] 已经存在,不覆盖文件...");
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
            if(DownloadStatus.REMOTE_BIGGER_LOCAL.getCode().equals(compareLocalAndRemote(pathName, fileName, localFile).getCode())){
                //需要断点下载
                os = new FileOutputStream(localFile, true);
                flag = downloadFileForStream(pathName, fileName, localFile, os);
            }else{
                //不需要断点下载
                os = new FileOutputStream(localFile);
                flag = this.downloadFileForStream(pathName, fileName, os);
            }
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
            FTPFile ftpFile = havingFile(pathName, fileName);

            if (ftpFile != null){
                flag = ftpClient.retrieveFile(ftpFile.getName(), stream);
                stream.close();
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
     * 下载文件（支持断点续下载）,同时支持查看进度
     * @param pathName
     * @param fileName
     * @param localFile
     * @param stream 输出流
     */
    private boolean downloadFileForStream(String pathName, String fileName, File localFile, OutputStream stream){
        boolean flag = false;
        InputStream inputStream = null;
        try {
            FTPFile ftpFile = havingFile(pathName, fileName);
            long localSize = localFile.length();
            ftpClient.setRestartOffset(localSize);
            if (ftpFile != null){
                inputStream = ftpClient.retrieveFileStream(ftpFile.getName());
                byte[] bytes = new byte[1024];
                long step = ftpFile.getSize() / 100;
                this.downloadProcess = localSize / step;
                int c;
                while ((c = inputStream.read(bytes))!= -1){
                    stream.write(bytes, 0 , c);
                    localSize += c;
                    long newProcess = localSize / step;
                    if (newProcess > this.downloadProcess){
                        this.downloadProcess = newProcess;
                    }
                }
                flag = ftpClient.completePendingCommand();
                inputStream.close();
                stream.close();
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
     * 判断下载是否完成，用于支持断点下载
     * @param remotePath 服务器路径
     * @param remoteName 服务器文件
     * @param localFile 本地文件
     * @return 返回下载文件状态
     */
    private DownloadStatus compareLocalAndRemote(String remotePath,String remoteName,File localFile){
        FTPFile ftpFile = havingFile(remotePath, remoteName);
        if (ftpFile != null){
            if (localFile == null || !localFile.exists()){
                return DownloadStatus.REMOTE_BIGGER_LOCAL;
            }
            long ftpFileSize = ftpFile.getSize();
            long localSize = localFile.length();
            if (ftpFileSize > localSize){
                return DownloadStatus.REMOTE_BIGGER_LOCAL;
            }
        }
        return DownloadStatus.LOCAL_BIGGER_REMOTE;
    }

    /**
     * 删除文件
     * @param pathName 要删除的文件路径
     * @param fileName 要删除的文件名称
     * @return
     */
    public boolean deleteFile(String pathName, String fileName){
        boolean flag = false;
        try {
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathName);
            if (ftpClient.dele(fileName) > 0){
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error("delete file [" + fileName + "] failed.", e);
        }finally {
            setReply();
        }
        return flag;
    }

    /**
     * 修改文件名
     * @param pathName
     * @param oldFileName
     * @param newFileName
     * @return
     */
    public boolean renameFile(String pathName, String oldFileName, String newFileName){
        boolean flag = false;
        try {
            FTPFile ftpFile = havingFile(pathName, oldFileName);
            if (ftpFile != null){
                flag = ftpClient.rename(ftpFile.getName(), newFileName);
            }
        } catch (Exception e) {
            LOGGER.error(" rename file " + oldFileName + " is  failed", e);
        } finally{
            setReply();
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
        setReply();
        return success;
    }

    /**
     * 判断ftp服务器路径是否存在
     * @param path 文件路径
     * @return
     */
    public boolean existFile(String path) {
        boolean flag = false;
        try {
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
            if (ftpFileArr.length > 0) {
                flag = true;
            }
        }catch (IOException e){
            LOGGER.error("search path [" + path + "] failed.", e);
        }finally {
            setReply();
        }
        return flag;
    }

    /**
     * 判断指定文件在服务器是否存在
     * @param filePath 文件所在服务器路径
     * @param fileName 文件名称
     * @return 返回文件是否存在
     */
    public boolean existFile(String filePath, String fileName){
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile file : ftpFiles) {
                    if (fileName.equalsIgnoreCase(file.getName())) {
                        flag = true;
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("search file [" + fileName + "] 失败.", e);
        }finally {
            setReply();
        }
        return flag;
    }

    /**
     * 获取服务器指定文件信息
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return 返回文件信息的封装
     */
    public FTPFile havingFile(String filePath, String fileName){
        try {
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile file : ftpFiles) {
                    if (fileName.equalsIgnoreCase(file.getName())) {
                        return file;
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("search file [" + fileName + "] 失败.", e);
        }finally {
            setReply();
        }
        return null;
    }

    private void setReply(){
        this.replyCode = ftpClient.getReplyCode();
        this.replyMessage = ftpClient.getReplyString();
    }

    public Integer getReplyCode() {
        return replyCode;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public long getDownloadProcess() {
        return downloadProcess;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }
}
