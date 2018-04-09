package com.agile.pool.factory;

import com.agile.bean.FtpConfig;
import com.agile.helper.FtpFileHelper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/8 16:51
 * @Modified By:
 */
public class FtpHelperFactory extends BasePooledObjectFactory<FtpFileHelper>{

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpHelperFactory.class);

    private FtpConfig ftpConfig;

    public FtpHelperFactory(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    /**
     * 新建对象
     * @return
     * @throws Exception
     */
    @Override
    public FtpFileHelper create() throws Exception {
        LOGGER.info("创建对象-----thread: " + Thread.currentThread().getName());
        return new FtpFileHelper(ftpConfig);
    }

    /**
     * 唤醒对象
     * @param ftpFileHelper
     * @return
     */
    @Override
    public PooledObject<FtpFileHelper> wrap(FtpFileHelper ftpFileHelper) {
        LOGGER.info("唤醒对象-----thread: " + Thread.currentThread().getName());
        return new DefaultPooledObject<FtpFileHelper>(ftpFileHelper);
    }

    /**
     * 销毁对象
     * @param p
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<FtpFileHelper> p) throws Exception {
        LOGGER.info("销毁对象-----thread: " + Thread.currentThread().getName());
        FtpFileHelper object = p.getObject();
        object.close();
        super.destroyObject(p);
    }

    /**
     * 验证对象
     * @param p
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<FtpFileHelper> p) {
        LOGGER.info("验证对象-----thread: " + Thread.currentThread().getName());
        return p.getObject().validate();
    }
}
