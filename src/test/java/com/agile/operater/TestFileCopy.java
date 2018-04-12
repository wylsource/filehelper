package com.agile.operater;

import com.agile.helper.FileCopyHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/12 14:26
 * @Modified By:
 */
public class TestFileCopy {

    private FileCopyHelper fileCopyHelper;

    @Before
    public void init(){
        fileCopyHelper = new FileCopyHelper();
    }

    @Test
    public void testCopy(){
        String sourceFile = "F:\\电影\\mv.mp4";
        String targetPath = "F:\\电影\\mv-2.mp4";
        long start = System.currentTimeMillis();
        //stream 3476 毫秒
        //channel 2452 毫秒
        //nio 3470 毫秒
        boolean copyFileStream = fileCopyHelper.copyFileStream(new File(sourceFile), targetPath);
        System.out.println("copy file = " + (System.currentTimeMillis() - start));
        Assert.assertTrue(copyFileStream);
    }
}
