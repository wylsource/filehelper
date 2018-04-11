package com.agile.joggle.abstractimpl;

import java.io.File;

/**
 * @Author: WuYL
 * @Description: 校验文件抽象类
 * @Date: Create in 2018/4/8 11:50
 * @Modified By:
 */
public abstract class AbstractFileCheckHelper extends AbstractFileHandle {

    public abstract boolean equals(File sourceFile, File targetFile);
}
