package com.agile.ftp;

import com.agile.helper.FtpFileHelper;
import com.agile.pool.FtpFileHelperPool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/8 13:38
 * @Modified By:
 */
public class TestFtp {


    private static FtpFileHelperPool pool = null;

    private static FtpFileHelper helper = null;

    @BeforeClass
    public static void init() throws Exception {
        pool = new FtpFileHelperPool();
        helper = pool.getHelper();
    }

    /**
     * 上传文件
     */
    @Before
    public void testUpload() throws Exception {
        String localFile = "F:\\10000233955445.pdf";
        String serverPath = "/home/testFtp";
        String serverFileName = "12345.pdf";
        boolean uploadFile = helper.defaultOverWriteUpload(serverPath, serverFileName, new FileInputStream(new File(localFile)));
        if (uploadFile){
            pool.returnObject(helper);
        }
        Assert.assertTrue(uploadFile);
    }

//    @Test
    public void testReName(){
        String serverPath = "/home/testFtp";
        String serverFileName = "12345.pdf";
        boolean renameFile = helper.renameFile(serverPath, serverFileName, "rename.pdf");
        if (renameFile){
            pool.returnObject(helper);
        }
        Assert.assertTrue(renameFile);
    }

//    @Test
    public void testDeleteFile() throws Exception {
        String serverPath = "/home/testFtp";
        String serverFileName = "rename.pdf";
        helper = pool.getHelper();
        boolean deleteFile = TestFtp.helper.deleteFile(serverPath, serverFileName);
        if (deleteFile){
            pool.returnObject(TestFtp.helper);
        }
        Assert.assertTrue(deleteFile);
    }

//    @Test
    public void testDownload() throws InterruptedException {
        String serverPath = "/home/testFtp";
        String serverFileName = "mv.mp4";
        String localFile = "F:\\电影";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    double downloadProcess = helper.getDownloadProcess();
                    System.out.println("---当前下载进度为: [" + downloadProcess + "%]");
                    if (downloadProcess == 100.0){
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        boolean downloadFile = helper.downloadFile(serverPath, serverFileName, localFile);

        thread.join();
        if (downloadFile && helper.getDownloadProcess() == 100){
//            pool.returnObject(helper);
        }
        Assert.assertTrue(downloadFile);
    }

    @Test
    public void testUploadResume(){
        String serverPath = "/home/testFtp";
        String serverFileName = "mv2.mp4";
        String localFile = "F:\\电影\\mv.mp4";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    double uploadProcess = helper.getUploadProcess();
                    System.out.println("---当前上传进度为: [" + uploadProcess + "%]");
                    if (uploadProcess == 100.0){
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        boolean uploadFile = helper.uploadFile(serverPath, serverFileName, localFile);
        Assert.assertTrue(uploadFile);
    }
}
