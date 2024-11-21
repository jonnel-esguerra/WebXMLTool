package com.onsemi.cim.umr.services;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class ServiceConfiguration {
	private static final Properties props = new Properties();

	public void loadProperties() throws Exception {
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
			PropertyConfigurator.configure(props);
		} catch (IOException e) {
				System.out.println("Error: Failed to load PHM-Tarlac Wafer Map property file.");
				e.printStackTrace();
		}
	}
	
	public static String getProperty (String property) {
		return (String) props.get(property);
	}

}
