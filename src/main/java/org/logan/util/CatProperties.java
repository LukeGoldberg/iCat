package org.logan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class CatProperties {
	
	private static final Logger logger = Logger.getLogger("CatProperties");
	
	private static final String CAT_HOME = "D:/gitFolder/iCat/src/main/java/org/logan/";
    private static final String PROPERTY_FILE_NAME = "cat.properties";
	
	private static Properties properties = null;
	
	static {
		loadProperties();
	}
	
	private static void loadProperties() {
		File propertyFile = new File(CAT_HOME + PROPERTY_FILE_NAME);
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (IOException ignore) {
			logger.severe("can not find properties file, please check it in CatProperties.java");
		}
	}
	
	public static String getProperty(String name) {
		return properties.getProperty(name);
	}
	
}
