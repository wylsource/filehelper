package com.agile.ftp;

import com.agile.helper.FtpFileHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/8 13:38
 * @Modified By:
 */
public class TestFtp {


    private FtpFileHelper helper = null;

    @Before
    public void init(){
//        helper = new FtpFileHelper();
//        helper.initFtpClient();
    }

    /**
     * 上传文件
     */
    @Test
    public void testUpload(){
        String localFile = "F:\\10000233955445.pdf";
        String serverPath = "/home/testFtp";
        String serverFileName = "12345.pdf";
        boolean uploadFile = helper.uploadFile(serverPath, serverFileName, localFile);
        Assert.assertTrue(uploadFile);
    }
}
