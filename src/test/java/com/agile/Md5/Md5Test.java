package com.agile.Md5;

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

    @Test
    public void testEquals(){
        String sourcePath = "F:\\testFtp\\2.pdf";
        String targetPath = "F:\\10000233955445.pdf";
        boolean equals = helper.equals(new File(sourcePath), new File(targetPath));
//        Assert.assertTrue(equals);
    }

}
