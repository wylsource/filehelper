package com.agile.Md5;

import com.agile.cipher.helper.Md5Helper;
import com.agile.helper.Md5FileCheckHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/9 15:26
 * @Modified By:
 */
public class Md5Test {

    public static Md5FileCheckHelper helper = new Md5FileCheckHelper();

    Md5Helper md5Helper = new Md5Helper();
    @Test
    public void testEquals(){
        String oldPath = "F:\\testFtp\\2.pdf";
        String newPath = "F:\\testFtp\\2_1.pdf";
        int i = 0;
        String str = "tests";
        long start = System.currentTimeMillis();
        //耗时约21065毫秒
        while (i++ <= 1000000){
//            boolean equals = helper.equals(new File(oldPath), new File(newPath));
//            System.out.println("---第 [" + i + "] 次验证：" + equals);

            //5634毫秒
            String s = md5Helper.encryptByte(str.getBytes());
            System.out.println("---第 [" + i + "] 次验证：" + s);
        }
        System.out.println("------------总耗时：" + (System.currentTimeMillis()-start) + "毫秒");

    }

}
