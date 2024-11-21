package com.onsemi.cim.umr.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.onsemi.cim.umr.services.ServiceCommon;
import com.onsemi.cim.umr.services.ServiceConfiguration;
import com.onsemi.cim.umr.services.ServieParseXML;
import com.onsemi.cim.umr.main.MyUI;
import com.onsemi.cim.umr.model.Device;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class ServiceCommon {

	public static ServiceConfiguration config = new ServiceConfiguration();
	public static Logger logger;
	
	public ServiceCommon() {
		
	}
	
	public static void loadProperties() {
		
		try {
			config.loadProperties();
		} catch (Exception ex) {
			log(ServiceCommon.class).error("Exeption : ", ex);
		}
	}
	
	public static Logger log(Class c) {
		logger = Logger.getLogger(c);
		return logger;
	}
	
	public static List<String> getGoodBin() {
		List<String> goodBin = new ArrayList<>();
		goodBin.add("8");
		goodBin.add("1");
		return goodBin;
	}
	
	public static List<String> getStatus() {
		List<String> status = new ArrayList<>();
		status.add("1ST");
		status.add("2ND");
		status.add("PIC");
		return status;
	}
	
	public static List<String> getDevice() {
		List<Device> mapConversionTable = new ArrayList<>();
		List<String> device = new ArrayList<>();
		mapConversionTable = getXMLList(null);
		
		Iterator<Device> iterator = mapConversionTable.iterator();
		while (iterator.hasNext()) {
			Device d = iterator.next();
			device.add(d.getProductID());
		}
		return device;
	}
	
	public static List<String> getTargetProcess() {
		List<String> list = new ArrayList<>();
		list.add("dice");
		list.add("tnr-in");
		list.add("tnr-out");
		list.add("test-1");
		list.add("test-2");
		list.add("ebr");
		return list;
	}
	
	public static List<String> getFlatOrientation() {
		List<String> list = new ArrayList<>();
		list.add("0");
		list.add("0R");
		list.add("180");
		list.add("270");
		return list;
	}
	
	public static List<String> getWaferSize() {
		List<String> list = new ArrayList<>();
		list.add("150");
		list.add("200");
		list.add("300");
		return list;
	}
	
	public static boolean findDuplicate(String newDevice, List<Device> mapConversionTable) {
		Iterator<Device> iterator = mapConversionTable.iterator();
		while (iterator.hasNext()) {
			Device device = iterator.next();
			if (device.getDeviceName().toUpperCase().equals(newDevice.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String getIP() {
		WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
		return webBrowser.getAddress();
	}
	
    public static void errorMessage (String title,String msg) {
    	Notification.show(title, msg, Notification.Type.ERROR_MESSAGE);
    }

    public static void notificationMessage (String title,String msg) {
    	Notification.show(title, msg, Notification.Type.TRAY_NOTIFICATION);
    }
    
    public static void warningMessage (String title,String msg) {
    	Notification.show(title, msg, Notification.Type.WARNING_MESSAGE);
    } 
    /*
    public static List<Device> getXMLList() {
    	List<Device> mapConversionTable = new ArrayList<>();
    	File xmlFile = new File(config.getProperty("XMLFile"));
		ServiceParseXML parser = new ServiceParseXML();
 
		try {
			mapConversionTable = parser.parseXml(new FileInputStream(xmlFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return mapConversionTable;
    }
    */
    public static List<Device> getXMLList(String filter) {
    	List<Device> mapConversionTable = new ArrayList<>();
    	List<Device> clone = new ArrayList<>();
    	File xmlFile = new File(config.getProperty("XMLFile"));
    	ServieParseXML parser = new ServieParseXML();
    	     	
		try {
			clone = parser.parseXml(new FileInputStream(xmlFile));
			for (Device d : clone) {
				boolean passesFilter = (filter == null || filter.isEmpty() || d.toString2().toLowerCase().contains(filter.toLowerCase()));
				if (passesFilter) {
					mapConversionTable.add(d);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
		return mapConversionTable;
    }
    
	public static Boolean isRegisteredUser (String userID, String configProperty) {
		userID = userID.toLowerCase();
		
		String string = config.getProperty(configProperty);
		List<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
		
		for (String s : list) {
			if(s.trim().toLowerCase().contains(userID)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(TextField obj) {
		if (obj.getValue().isEmpty()){
			obj.setComponentError(new UserError("Empty value not allowed"));
		} else {
		    obj.setComponentError(null);
		    return false;
		}
		return true;
	}
	
	public static boolean isEmpty(TextField obj, int min, int max) {
		try {
			if ((obj.getValue().isEmpty()) || (Integer.parseInt(obj.getValue()) < min) || (Integer.parseInt(obj.getValue()) > max)) {
				obj.setComponentError(new UserError("Value must be an integer from " + min + " to " + max));
		    } else {
		    	obj.setComponentError(null);
		    	return false;
			}
		}
		catch (NumberFormatException e) {
			obj.setComponentError(new UserError("Value must be an integer from " + min + " to " + max));
		}
		return true;
		
	}
	public static boolean isEmpty(ComboBox obj) {
		if (obj.getValue() == null) {
			obj.setComponentError(new UserError("Empty value not allowed"));
			
		} else {
        	obj.setComponentError(null);
        	return false;
        }
		return true;
	}
	
}
