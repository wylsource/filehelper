package com.agile.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: WuYL
 * @Description: 封装文件的信息
 * @Date: Create in 2018/4/11 10:25
 * @Modified By:
 */
public class FileInfo implements Serializable{

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件所在路径
     */
    private String filePath;

    /**
     * 文件所在父路径
     */
    private String parentPath;

    /**
     * 文件所在根路径
     */
    private String rootPath;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 是否有执行权限
     */
    private Boolean executable;

    /**
     * 是否有读权限
     */
    private Boolean readable;

    /**
     * 是否有写权限
     */
    private Boolean writable;

    /**
     * 文件创建时间
     */
    private Date createDate;

    /**
     * 文件最后修改时间
     */
    private Date lastModifiedDate;

    /**
     * 文件最后访问时间
     */
    private Date lastAccessDate;

    /**
     * 文件所有者
     */
    private String ownerPerson;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setExecutable(Boolean executable) {
        this.executable = executable;
    }

    public void setReadable(Boolean readable) {
        this.readable = readable;
    }

    public void setWritable(Boolean writable) {
        this.writable = writable;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void setOwnerPerson(String ownerPerson) {
        this.ownerPerson = ownerPerson;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Boolean getExecutable() {
        return executable;
    }

    public Boolean getReadable() {
        return readable;
    }

    public Boolean getWritable() {
        return writable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public String getOwnerPerson() {
        return ownerPerson;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", rootPath='" + rootPath + '\'' +
                ", fileSize=" + fileSize +
                ", executable=" + executable +
                ", readable=" + readable +
                ", writable=" + writable +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastAccessDate=" + lastAccessDate +
                ", ownerPerson='" + ownerPerson + '\'' +
                '}';
    }
}
