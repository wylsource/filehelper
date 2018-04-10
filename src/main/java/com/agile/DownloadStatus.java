package com.agile;

/**
 * @Author: WuYL
 * @Description: 封装下载时的文件状态
 * @Date: Create in 2018/4/10 10:20
 * @Modified By:
 */
public enum DownloadStatus {

    DOWNLOAD_SUCCESS(0, "下载完成"),
    LOCAL_BIGGER_REMOTE(1, "本地文件大于服务器文件"),
    REMOTE_BIGGER_LOCAL(2, "服务器文件大于本地文件")
    ;

    private Integer code;
    private String message;
    private DownloadStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
