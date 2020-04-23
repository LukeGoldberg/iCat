package org.logan.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatProperties {
	
	private static final Logger logger = LoggerFactory.getLogger("CatProperties");
	
    private static final String PROPERTY_FILE_NAME = "/cat.properties";
	
	private static Properties properties = null;
	
	private void loadProperties() {
		properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream(PROPERTY_FILE_NAME));
		} catch (IOException ignore) {
			ignore.printStackTrace();
			logger.error("can not find properties file, please check it.");
		}
	}
	
	public String getProperty(String name) {
		if (properties == null) {
			loadProperties();
		}
		return properties.getProperty(name);
	}
	
}
