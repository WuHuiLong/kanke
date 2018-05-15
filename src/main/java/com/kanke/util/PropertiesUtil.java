package com.kanke.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger= LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static{
        String filmName="kanke.properties";
        props=new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filmName)));
        } catch (IOException e) {
            logger.error("配置文件异常",e);
        }
    }

    public static String getProperties(String key){
        String value=props.getProperty(key);
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperties(String key,String defaultValue){
        String value=props.getProperty(key);
        if(StringUtils.isBlank(value)){
             value=defaultValue;
        }
        return value.trim();
    }

}
