package com.agile.helper;

import com.agile.bean.FileInfo;
import com.agile.joggle.abstractimpl.AbstractFileHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * @Author: WuYL
 * @Description: 文件常用操作助手类
 * @Date: Create in 2018/4/8 11:51
 * @Modified By:
 */
public class FileOperateHelper extends AbstractFileHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperateHelper.class);

    /**
     * 比较两个文件大小是否是一样的
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @return 返回大小是否一致
     */
    public boolean compareFileSize(File sourceFile, File targetFile){
        if (sourceFile == null){
            LOGGER.error("sourceFile is  null");
            return false;
        }

        if (targetFile == null){
            LOGGER.error("targetFile is  null");
            return false;
        }
        if (sourceFile.length() != targetFile.length()){
            return false;
        }
        return true;
    }

    /**
     * 获取文件信息
     * @param filePath 文件路径
     */
    public FileInfo getFileInfo(String filePath){
        //获取文件 path
        Path path = Paths.get(filePath);
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(path.getFileName().toString());
            fileInfo.setFilePath(path.toString());
            fileInfo.setExecutable(Files.isExecutable(path));
            fileInfo.setReadable(Files.isReadable(path));
            fileInfo.setWritable(Files.isWritable(path));
            fileInfo.setParentPath(path.getParent().toString());
            fileInfo.setRootPath(path.getRoot().toString());
            BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class);
            BasicFileAttributes basicFileAttributes = basicView.readAttributes();
            fileInfo.setCreateDate(new Date(basicFileAttributes.creationTime().toMillis()));
            fileInfo.setLastAccessDate(new Date(basicFileAttributes.lastAccessTime().toMillis()));
            fileInfo.setLastModifiedDate(new Date(basicFileAttributes.lastModifiedTime().toMillis()));
            fileInfo.setFileSize(Files.size(path));
            fileInfo.setOwnerPerson(Files.getOwner(path).toString());
            return fileInfo;
        }catch (Exception e){
            LOGGER.error("Get fileinfo from [" + filePath + "] is failed.", e);
        }finally {

        }
        return null;
    }
}
