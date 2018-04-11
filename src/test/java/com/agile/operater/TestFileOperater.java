package com.agile.operater;

import com.agile.bean.FileInfo;
import com.agile.helper.FileOperateHelper;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @Author: WuYL
 * @Description:
 * @Date: Create in 2018/4/11 10:43
 * @Modified By:
 */
public class TestFileOperater {

    private FileOperateHelper fileOperateHelper;
    @Before
    public void init(){
        fileOperateHelper = new FileOperateHelper();
    }

    @Test
    public void testGetFileInfo(){
        String filePath = "F:\\test-2018-4-11.txt";
        String httpPath = "http://img5.imgtn.bdimg.com/it/u=2460833846,3970004509&fm=27&gp=0.jpg";
        URI uri = URI.create(httpPath);
        FileInfo fileInfo = fileOperateHelper.getFileInfo(filePath);
        System.out.println(fileInfo.toString());
    }

}
