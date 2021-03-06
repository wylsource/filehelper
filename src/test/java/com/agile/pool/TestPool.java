package com.agile.pool;

import com.agile.helper.FtpFileHelper;
import org.junit.Test;

import java.util.Random;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/8 17:56
 * @Modified By:
 */
public class TestPool {

    private static FtpFileHelperPool ftpClientPool;

    static String localFile = "F:\\10000233955445.pdf";
    static String serverPath = "/home/testFtp";
    static String serverFileName = ".pdf";

    static String localPath = "F:\\testFtp";

    static{
//      ftpClientPool=new FTPClientPool(Thread.currentThread().getContextClassLoader().getResourceAsStream("ftpClient.properties"));
        ftpClientPool=new FtpFileHelperPool();
    }

//    @Test
    public void testPool(){
        final Random random = new Random(10);
        for(int i=0;i<10;i++){
            final String name = i + serverFileName;
//            Thread.sleep(500);
            Thread thread=new Thread(new Runnable() {

                public void run() {

                    try {
                        Thread.sleep(random.nextInt(1000));
                        FtpFileHelper helper = ftpClientPool.getHelper();
                        boolean b = helper.uploadFile(serverPath, name, localFile);
                        String replyMessage = helper.getReplyMessage();
                        System.out.println("replyMessage : " + replyMessage);
                        if (b){
                            ftpClientPool.returnObject(helper);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    @Test
    public void testDownload(){
        FtpFileHelper helper = null;
        try {
            helper = ftpClientPool.getHelper();
            boolean b = helper.downloadFile(serverPath, 3 + serverFileName, localPath);
            String replyMessage = helper.getReplyMessage();
            System.out.println("replyMessage : " + replyMessage);
            if (b){
                ftpClientPool.returnObject(helper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
