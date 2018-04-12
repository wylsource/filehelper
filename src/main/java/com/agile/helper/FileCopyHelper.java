package com.agile.helper;

import com.agile.joggle.abstractimpl.AbstractFileHandle;
import com.agile.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: WuYL
 * @Description: 拷贝文件的操作类
 * @Date: Create in 2018/4/11 13:27
 * @Modified By:
 */
public class FileCopyHelper extends AbstractFileHandle{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileCopyHelper.class);

    /**
     * 使用 Stream 方式 copy 文件时每次读取的 byte 字节数组的大小
     */
    private Integer copySize;

    public FileCopyHelper(){
        this.copySize = 1024 * 16;
    }

    public FileCopyHelper(int copySize){
        this.copySize = copySize;
    }

    /**
     * 判断文件是否能够拷贝
     * @param sourceFile 原始文件
     * @param targetPath 目标路径
     * @return 返回是否能够拷贝
     */
    private static boolean canCopy(File sourceFile, String targetPath){
        if (sourceFile == null || !sourceFile.exists()){
            LOGGER.error("sourceFile is empty or not exists");
            return false;
        }

        if (StringUtil.isEmpty(targetPath)){
            LOGGER.error("targetPath is empty.");
            return false;
        }
        return true;
    }

    /**
     * 通过输入输出流拷贝文件
     * @param sourceFile 原始文件
     * @param targetPath 目标目录
     * @return 返回是否拷贝成功
     */
    public boolean copyFileStream(File sourceFile, String targetPath){
        if (!canCopy(sourceFile, targetPath)){
            return false;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(new File(targetPath));
            byte[] bytes = new byte[copySize];
            int l;
            while ((l = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, l);
            }
            return true;
        }catch (Exception e){
            LOGGER.error("use stream copy file [" + sourceFile.getName() + "] to [" + targetPath + "] is failed.", e);
        }finally {
            FileOperateHelper.closeInputStream(inputStream);
            FileOperateHelper.closeOutStream(outputStream);
        }
        return false;
    }

    /**
     * 通过通道（channel）拷贝文件
     * @param sourceFile 原始文件
     * @param targetPath 目标路径
     * @return 返回是否拷贝成功
     */
    public boolean copyFileChannel(File sourceFile, String targetPath){
        if (!canCopy(sourceFile, targetPath)){
            return false;
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(sourceFile).getChannel();
            outputChannel = new FileOutputStream(new File(targetPath)).getChannel();
            long size = inputChannel.size();
            long pos = 0L;
            long bytesCopied;
            for(long count = 0L; pos < size; pos += bytesCopied) {
                long remain = size - pos;
                count = remain > 31457280L ? 31457280L : remain;
                bytesCopied = outputChannel.transferFrom(inputChannel, pos, count);
                if (bytesCopied == 0L) {
                    break;
                }
            }
            return true;
        }catch (Exception e){
            LOGGER.error("use Channel copy file [" + sourceFile.getName() + "] to [" + targetPath + "] is failed.", e);
        }finally {
            closeChannel(inputChannel);
            closeChannel(outputChannel);
        }
        return false;
    }

    /**
     * 使用 jdk 提供的 NIO Files.copy()方法拷贝文件
     * @param sourceFile 原始文件
     * @param targetPath 目标路径
     * @return 返回是否拷贝成功
     */
    public boolean copyFileNio(File sourceFile, String targetPath){
        if (!canCopy(sourceFile, targetPath)){
            return false;
        }
        try {
            Path copy = Files.copy(Paths.get(sourceFile.getPath()), Paths.get(targetPath));
            if (copy != null){
                return true;
            }
        } catch (IOException e) {
            LOGGER.error("use nio copy file [" + sourceFile.getName() + "] to [" + targetPath + "] is failed.", e);
        }finally {

        }
        return false;
    }

    /**
     * 修改文件编码格式
     * @param sourceFile 源文件
     * @param targetPath 目标文件地址
     * @param sourceEncoding 源文件编码格式
     * @param targetEncoding 目标文件编码格式
     * @return 返回是否能够修改成功
     */
    public boolean changeEncoding(File sourceFile, String targetPath, String sourceEncoding, String targetEncoding){
        if (!canCopy(sourceFile, targetPath)){
            return false;
        }
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try{
            inputStream = new FileInputStream(sourceFile);
            inputStreamReader = new InputStreamReader(inputStream, sourceEncoding);
            outputStream = new FileOutputStream(new File(targetPath));
            outputStreamWriter = new OutputStreamWriter(outputStream, targetEncoding);
            char[] bytes = new char[1024];
            int l;
            while ((l = inputStreamReader.read(bytes)) != -1){
                outputStreamWriter.write(bytes, 0, l);
            }
            return true;
        }catch (Exception e){
            LOGGER.error("change encoding from [" + sourceEncoding + "] to [" + targetEncoding + "] is failed.", e);
        }finally {
            FileOperateHelper.closeInputStream(inputStream);
            FileOperateHelper.closeReader(inputStreamReader);
            FileOperateHelper.closeOutStream(outputStream);
            FileOperateHelper.closeWriter(outputStreamWriter);
        }
        return false;
    }



    /**
     * 关闭通道的方法
     * @param fileChannel 通道
     */
    public static void closeChannel(FileChannel fileChannel){
        if (null != fileChannel){
            try {
                fileChannel.close();
            } catch (IOException e) {
                LOGGER.error("close channel is failed.", e);
            }
        }
    }
}
