package com.parent.o2o.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstantBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ConstantBean.class);
	// 配置文件路径
	private String propertiesFile;

	public static Map<String, String> propertiesMap =new HashMap<String, String>();
	
	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}
	
	public void init(){
		Properties prop = new Properties();
		InputStream istream = null;
		try {
			istream = getClass().getResourceAsStream("/" + propertiesFile);
			// 如果读取不到则按路径读取
			if (null == istream) {
				istream = new FileInputStream(propertiesFile);
			}
			prop.load(istream);
			Iterator<Object> iterator = prop.keySet().iterator();
			while(iterator.hasNext()){				
				String pName =(String)iterator.next();
				propertiesMap.put(pName, prop.getProperty(pName));
			}
			istream.close();
		} catch (FileNotFoundException e) {
			logger.error("ConstantBean初始化配置文件不存在", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("ConstantBean初始化IO异常", e);
			e.printStackTrace();
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String get(String propertyName){
		return propertiesMap.get(propertyName);
	}
}
