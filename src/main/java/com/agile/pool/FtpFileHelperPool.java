package com.agile.pool;

import com.agile.bean.FtpConfig;
import com.agile.helper.ConfigHelper;
import com.agile.helper.FtpFileHelper;
import com.agile.pool.factory.FtpHelperFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @Author: WuYL
 * @Description: 提供Ftp 连接
 * @Date: Create in 2018/4/8 16:08
 * @Modified By:
 */
public class FtpFileHelperPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpFileHelperPool.class);

    private GenericObjectPool<FtpFileHelper> ftpFileHelperPool;

    public FtpFileHelperPool(InputStream resourceAsStream) {
        try {
            // 初始化对象池配置
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setBlockWhenExhausted(ConfigHelper.getBlockWhenExhausted());
            poolConfig.setMaxWaitMillis(ConfigHelper.getMaxWait());
            poolConfig.setMinIdle(ConfigHelper.getMinIdle());
            poolConfig.setMaxIdle(ConfigHelper.getMaxIdle());
            poolConfig.setMaxTotal(ConfigHelper.getMaxTotal());
            poolConfig.setTestOnBorrow(ConfigHelper.getTestOnBorrow());
            poolConfig.setTestOnReturn(ConfigHelper.getTestOnReturn());
            poolConfig.setTestOnCreate(ConfigHelper.getTestOnCreate());
            poolConfig.setTestWhileIdle(ConfigHelper.getTestWhileIdle());
            poolConfig.setLifo(ConfigHelper.getLifo());

            FtpConfig ftpConfig = new FtpConfig();
            ftpConfig.setHost(ConfigHelper.getHost());
            ftpConfig.setPort(ConfigHelper.getPort());
            ftpConfig.setUsername(ConfigHelper.getUsername());
            ftpConfig.setPassword(ConfigHelper.getPassword());
            ftpConfig.setClientTimeout(ConfigHelper.getClientTimeout());
            ftpConfig.setEncoding(ConfigHelper.getEncoding());
            ftpConfig.setWorkingDirectory(ConfigHelper.getWorkingDirectory());
            ftpConfig.setPassiveMode(ConfigHelper.getPassiveMode());
            ftpConfig.setRenameUploaded(ConfigHelper.getRenameUploaded());
            ftpConfig.setRetryTimes(ConfigHelper.getRetryTimes());
            ftpConfig.setTransferFileType(ConfigHelper.getTransferFileType());
            ftpConfig.setBufferSize(ConfigHelper.getBufferSize());
            ftpConfig.setOverwrite(ConfigHelper.getOverwrite());
            // 初始化对象池
            ftpFileHelperPool = new GenericObjectPool<FtpFileHelper>(new FtpHelperFactory(ftpConfig), poolConfig);
        } catch (Exception e) {
            LOGGER.error("连接池初始化失败", e);
            System.exit(-1);
        }

    }

    /**
     * 获取帮助连接
     * @return
     * @throws Exception
     */
    public FtpFileHelper getHelper() throws Exception {
        System.out.println("获取前");
        System.out.println("活动"+ftpFileHelperPool.getNumActive());
        System.out.println("等待"+ftpFileHelperPool.getNumWaiters());
        System.out.println("----------");
        return ftpFileHelperPool.borrowObject();
    }

    /**
     * 将帮助连接返回给连接池
     * @param helper
     */
    public void returnObject(FtpFileHelper helper){
        System.out.println("归还前");
        System.out.println("活动"+ftpFileHelperPool.getNumActive());
        System.out.println("等待"+ftpFileHelperPool.getNumWaiters());
        System.out.println("----------");
        ftpFileHelperPool.returnObject(helper);
        System.out.println("归还后");
        System.out.println("活动"+ftpFileHelperPool.getNumActive());
        System.out.println("等待"+ftpFileHelperPool.getNumWaiters());
        System.out.println("----------");
    }
}
