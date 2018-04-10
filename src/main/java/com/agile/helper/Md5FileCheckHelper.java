package com.agile.helper;

import com.agile.cipher.helper.Md5Helper;
import com.agile.joggle.abstractimpl.AbstractFileCheckHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author: WuYL
 * @Description: MD5 方式校验文件实现
 * @Date: Create in 2018/4/8 11:50
 * @Modified By:
 */
public class Md5FileCheckHelper extends AbstractFileCheckHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5FileCheckHelper.class);

    /**
     * 比较两个文件是否相同
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @return
     */
    public boolean equals(File sourceFile, File targetFile){
        if (sourceFile == null){
            LOGGER.error("sourceFile is not be null");
            return false;
        }

        if (targetFile == null){
            LOGGER.error("targetFile is not be null");
            return false;
        }
        if (sourceFile.length() != targetFile.length()){
            return false;
        }

        Md5Helper helper = new Md5Helper();
        String source = helper.encryptFile(sourceFile);
        String target = helper.encryptFile(targetFile);
        if (target != null && target.equals(source)){
            return true;
        }
        return false;
    }

}
