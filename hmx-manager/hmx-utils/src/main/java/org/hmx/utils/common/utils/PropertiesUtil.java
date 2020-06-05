package org.hmx.utils.common.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件读取工具类
 * Created by wangwei on 2017/4/21.
 */
public class PropertiesUtil {

    private Properties properties;

    private PropertiesUtil(Properties properties) {
        this.properties = properties;
    }

    /**
     * 加载资源文件
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static Properties load(String filePath) throws IOException {
        Properties properties = new Properties();
        File propertiesFile = ResourceUtils.getFile("classpath:" + filePath);
        InputStream inputStream = new FileInputStream(propertiesFile);
        if (inputStream != null) {
            properties.load(inputStream);
        }
        return properties;
    }
}
