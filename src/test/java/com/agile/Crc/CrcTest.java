package com.agile.Crc;

import com.agile.helper.CrcFileCheckHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/11 13:01
 * @Modified By:
 */
public class CrcTest {

    private CrcFileCheckHelper crcFileCheckHelper;

    @Before
    public void init(){
        crcFileCheckHelper = new CrcFileCheckHelper();
    }

    @Test
    public void testEquals(){
        String oldPath = "F:\\testFtp\\2.pdf";
        String newPath = "F:\\testFtp\\2_1.pdf";
        int i = 0;
        long start = System.currentTimeMillis();
        //耗时约13469毫秒
        while (i++ <= 1000000){
            boolean equals = crcFileCheckHelper.equals(new File(oldPath), new File(newPath));
            System.out.println("---第 [" + i + "] 次验证=" + equals);
        }
        System.out.println("------------总耗时：" + (System.currentTimeMillis()-start) + "毫秒");
    }
}
