package com.agile.helper;


import com.agile.constant.ConfigConstant;
import com.agile.util.PropsUtil;

import java.util.Properties;

/**
 * @Author: WuYL
 * @Description: 加载属性文件助手类
 * @Date: Created on 2017/6/17
 * @Modified By: 2018/4/8
 */
public final class ConfigHelper {

    private static Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);


    /* ----------------------------- FTP连接属性配置 ------------------------- */

    /**
     * 获取服务器 IP(host)
     */
    public static String getHost(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.HOST);
    }


    /**
     * 获取服务器 PORT
     */
    public static Integer getPort(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.PORT, ConfigConstant.DEFAULTPORT);
    }


    /**
     * 获取服务器 用户名
     */
    public static String getUsername(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.USERNAME);
    }


    /**
     * 获取服务器 密码
     */
    public static String getPassword(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.PASSWORD);
    }

    /**
     * 获取连接是否为主动模式
     */
    public static boolean getPassiveMode(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.PASSIVEMODE);
    }

    /**
     * 编码
     */
    public static String getEncoding(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.ENCODING);
    }

    /**
     * 超时时间
     */
    public static int getClientTimeout(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.CLIENTTIMEOUT);
    }

    /**
     * 线程数
     */
    public static Integer getThreadNum(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.THREADNUM);
    }

    /**
     * 文件传送类型
     */
    public static Integer getTransferFileType(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.TRANSFERFILETYPE);
    }

    /**
     * 是否重命名
     */
    public static boolean getRenameUploaded(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.RENAMEUPLOADED);
    }

    /**
     * 重新连接时间
     */
    public static int getRetryTimes(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.RETRYTIMES);
    }

    /**
     * 缓存大小
     */
    public static int getBufferSize(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.BUFFERSIZE);
    }

    /**
     * 默认进入的路径
     */
    public static String getWorkingDirectory(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.WORKINGDIRECTORY);
    }


    /* ------------------------- FTP连接池配置 --------------------------------- */

    /**
     * 最大数
     */
    public static int getMaxTotal(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.MAXTOTAL);
    }

    /**
     * 最小空闲
     */
    public static int getMinIdle(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.MINIDLE);
    }

    /**
     * 最大空闲
     */
    public static int getMaxIdle(){
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.MAXIDLE);
    }

    /**
     * 最大等待时间
     */
    public static long getMaxWait(){
        return PropsUtil.getLong(CONFIG_PROPS, ConfigConstant.MAXWAIT);
    }

    /**
     * 池对象耗尽之后是否阻塞,maxWait<0时一直等待
     */
    public static boolean getBlockWhenExhausted(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.BLOCKWHENEXHAUSTED);
    }

    /**
     * 创建对象时验证
     */
    public static boolean getTestOnCreate(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.TESTONCREATE);
    }

    /**
     * 取对象时验证
     */
    public static boolean getTestOnBorrow(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.TESTONBORROW);
    }
    /**
     * 回收验证
     */
    public static boolean getTestOnReturn(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.TESTONRETURN);
    }
    /**
     * 空闲验证
     */
    public static boolean getTestWhileIdle(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.TESTWHILEIDLE);
    }
    /**
     * 后进先出
     */
    public static boolean getLifo(){
        return PropsUtil.getBoolean(CONFIG_PROPS, ConfigConstant.LIFO);
    }

}
