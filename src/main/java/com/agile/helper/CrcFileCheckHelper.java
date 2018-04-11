package com.agile.helper;

import com.agile.cipher.helper.CrcHelper;
import com.agile.joggle.abstractimpl.AbstractFileCheckHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author: WuYL
 * @Description: 使用 crc 算法校验文件
 * @Date: Create in 2018/4/11 9:38
 * @Modified By:
 */
public class CrcFileCheckHelper extends AbstractFileCheckHelper{

    private static final Logger LOGGER = LoggerFactory.getLogger(CrcFileCheckHelper.class);

    private static FileOperateHelper operateHelper;

    public CrcFileCheckHelper() {
        operateHelper = new FileOperateHelper();
    }

    /**
     * 通过 CRC 算法比较文件一致性
     * @param sourceFile
     * @param targetFile
     * @return
     */
    @Override
    public boolean equals(File sourceFile, File targetFile){
        if (!operateHelper.compareFileSize(sourceFile, targetFile)){
            return false;
        }
        CrcHelper crcHelper = new CrcHelper();
        String sourceFileCrc = crcHelper.encryptFile(sourceFile);
        String targetFileCrc = crcHelper.encryptFile(targetFile);
        if (sourceFileCrc != null && sourceFileCrc.equals(targetFileCrc)){
            return true;
        }
        return false;
    }


}
