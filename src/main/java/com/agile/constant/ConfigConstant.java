package com.agile.constant;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/8 12:06
 * @Modified By:
 */
public interface ConfigConstant {

    /**
     * 配置文件名称
     */
    String CONFIG_FILE = "filehelper.properties";

    String CONFIG_FILE_PROPERTIES = "properties/filehelper.properties";


    /* ------------------------- FTP连接池配置 --------------------------------- */

    /**
     * 最大数
     */
    String MAXTOTAL = "filehelper.ftpClient.maxTotal";
    /**
     * 最小空闲
     */
    String MINIDLE = "filehelper.ftpClient.minIdle";

    /**
     * 最大空闲
     */
    String MAXIDLE = "filehelper.ftpClient.maxIdle";

    /**
     * 最大等待时间
     */
    String MAXWAIT = "filehelper.ftpClient.maxWait";

    /**
     * 池对象耗尽之后是否阻塞,maxWait<0时一直等待
     */
    String BLOCKWHENEXHAUSTED = "filehelper.ftpClient.blockWhenExhausted";

    /**
     * 取对象时验证
     */
    String TESTONBORROW = "filehelper.ftpClient.testOnBorrow";

    /**
     * 回收验证
     */
    String TESTONRETURN = "filehelper.ftpClient.testOnReturn";

    /**
     * 创建时验证
     */
    String TESTONCREATE = "filehelper.ftpClient.testOnCreate";

    /**
     * 空闲验证
     */
    String TESTWHILEIDLE = "filehelper.ftpClient.testWhileIdle";

    /**
     * 后进先出
     */
    String LIFO = "filehelper.ftpClient.lifo";

    /* ----------------------------- FTP连接属性配置 ------------------------- */

    /**
     * ip
     */
    String HOST = "filehelper.ftpClient.host";

    /**
     * 端口
     */
    String PORT = "filehelper.ftpClient.port";
    Integer DEFAULTPORT = 21;

    /**
     * 登录名
     */
    String USERNAME = "filehelper.ftpClient.username";

    /**
     * 密码
     */
    String PASSWORD = "filehelper.ftpClient.password";

    /**
     * 连接是否为主动模式
     */
    String PASSIVEMODE = "filehelper.ftpClient.passiveMode";

    /**
     * 编码
     */
    String ENCODING = "filehelper.ftpClient.encoding";

    /**
     * 超时时间
     */
    String CLIENTTIMEOUT = "filehelper.ftpClient.clientTimeout";

    /**
     * 线程数
     */
    String THREADNUM = "filehelper.ftpClient.threadNum";

    /**
     * 文件传送类型 （目前只支持二进制）
     * 2=BINARY_FILE_TYPE（二进制文件）
     */

    String TRANSFERFILETYPE = "filehelper.ftpClient.transferFileType";

    /**
     * 是否重命名
     */
    String RENAMEUPLOADED = "filehelper.ftpClient.renameUploaded";

    /**
     * 重新连接时间
     */
    String RETRYTIMES = "filehelper.ftpClient.retryTimes";

    /**
     * 缓存大小
     */
    String BUFFERSIZE = "filehelper.ftpClient.bufferSize";

    /**
     * 默认进入的路径
     */
    String WORKINGDIRECTORY = "filehelper.ftpClient.workingDirectory";

    /**
     * 是否覆盖已有文件
     */
    String OVERWRITE = "filehelper.ftpClient.overwrite";

}
