package com.onsemi.cim.umr.screen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.onsemi.cim.umr.main.MyUI;
import com.onsemi.cim.umr.model.Device;
import com.onsemi.cim.umr.services.ServiceCommon;
import com.onsemi.cim.umr.services.ServiceModifyXML;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ScreenConfirmUpdate extends Window {
	
	private final Panel panel = new Panel();
	private final FormLayout form = new FormLayout();
	private final HorizontalLayout hLayoutButtons = new HorizontalLayout();
	
	private final Button btnSubmit = new Button("Submit");
	private final Button btnCancel = new Button("Cancel");	
	private final TextField userName = new TextField("User Name");
	private final PasswordField  password = new PasswordField ("Password");
	private final TextField txtProductID = new TextField("Product ID");
	private final TextField txtDeviceName = new TextField("Device Name");
	private final ComboBox cmbGoodBin = new ComboBox("Good Bin");
	@SuppressWarnings("rawtypes")
	private final ComboBox cmbFlatOrientation = new ComboBox("Flat Orientation");
	private final TextField txtXDie = new TextField("X Die Size");
	private final TextField txtYDie = new TextField("Y Die Size");
	private final ComboBox cmbWaferSize= new ComboBox("Wafer Size");
	
	private final Label message = new Label("null");
	
	private Device device;
	private List<Device> mapConversionTable;
	
	private ServiceModifyXML modifyXMLFile = null;
	
	private final static Logger logger = Logger.getLogger(ScreenConfirmUpdate.class);
	
	private boolean isSuccessful = false;
	
	public ScreenConfirmUpdate() {
		this.center();
		this.setResizable(false);
		this.setModal(true);
		
		panel.setSizeUndefined(); 
		
		btnSubmit.setIcon(VaadinIcons.THUMBS_UP);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel.setIcon(VaadinIcons.CLOSE);
		btnCancel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel.addClickListener(e -> close());
		
		form.setSizeUndefined(); 
		form.setMargin(true);
		
		hLayoutButtons.addComponents(btnSubmit,btnCancel);
		
		
		panel.setContent(form);
		this.setContent(panel);
		
		userName.addValueChangeListener(e -> ServiceCommon.isEmpty(userName));
		password.addValueChangeListener(e -> ServiceCommon.isEmpty(password));
		

	}
	
	private void mapConversionFields() {
		form.addComponents(
				message, 
				txtProductID,
				txtDeviceName,
				cmbGoodBin,
				txtXDie,
				txtYDie,
				cmbFlatOrientation,
				cmbWaferSize,
				hLayoutButtons
				);

	}
	
	private void mapConversionFieldsLogin() {
		form.addComponents(
				message,
				userName,
				password,
				hLayoutButtons
				);
		
	}
	
	public ScreenConfirmUpdate(File file) {
		this();
		panel.setCaption("Confirm Upload");
		message.setValue("Enter your username and password to proceed.");
		form.addComponents(
				message,
				userName,
				password,
				hLayoutButtons
				);
		userName.focus();
		
		btnSubmit.addClickListener(e -> upload("EBRUsers"));
	}

	public ScreenConfirmUpdate(Device device,List<Device> mapConversionTable) {
		this();
		mapConversionFieldsLogin();
		this.device = device;
		this.mapConversionTable = mapConversionTable;

		panel.setCaption("Add New Device Record");
		message.setValue("Are you sure you want to add this new record?");
				
		txtProductID.setReadOnly(true);
		txtDeviceName.setReadOnly(true);
		cmbGoodBin.setReadOnly(true);
		txtXDie.setReadOnly(true);
		txtYDie.setReadOnly(true);
		cmbFlatOrientation.setReadOnly(true);
		cmbWaferSize.setReadOnly(true);
		
		userName.focus();
		
		setTxtValues();
		
		btnSubmit.addClickListener(e -> add());
	}
	
	public ScreenConfirmUpdate(Device device, String userID) {
		this();
		//mapConversionFieldsLogin();
		mapConversionFields();
		this.device = device;
		
		panel.setCaption("Update Existing Device Record");
		message.setValue("Are you sure you want to update this existing record?");
		
		txtProductID.setReadOnly(true);
		txtDeviceName.setReadOnly(true);
		
		setTxtValues();
		
		cmbGoodBin.focus();
		
		cmbGoodBin.setItems(ServiceCommon.getGoodBin());
		cmbGoodBin.setEmptySelectionAllowed(false);
        cmbFlatOrientation.setItems(ServiceCommon.getFlatOrientation());
		cmbFlatOrientation.setEmptySelectionAllowed(false);
		cmbWaferSize.setItems(ServiceCommon.getWaferSize());
		cmbWaferSize.setEmptySelectionAllowed(false);
		
        btnSubmit.addClickListener(e -> update(userID));
        txtProductID.addValueChangeListener(e -> ServiceCommon.isEmpty(txtProductID));
        txtDeviceName.addValueChangeListener(e -> ServiceCommon.isEmpty(txtDeviceName));
        cmbGoodBin.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbGoodBin));
        txtXDie.addValueChangeListener(e -> ServiceCommon.isEmpty(txtXDie,1,6000));
        txtYDie.addValueChangeListener(e -> ServiceCommon.isEmpty(txtYDie,1,6000));
        cmbFlatOrientation.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbFlatOrientation));
        cmbWaferSize.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbWaferSize));
	}
	
	public ScreenConfirmUpdate(List<Device> mapConversionTable, Device device) {
		this();
		mapConversionFieldsLogin();
		this.device = device;
		this.mapConversionTable = mapConversionTable;

		panel.setCaption("Delete Existing Device Record");
		message.setValue("Are you sure you want to delete this record?");
		
		txtProductID.setReadOnly(true);
		txtDeviceName.setReadOnly(true);
		cmbGoodBin.setReadOnly(true);
		txtXDie.setReadOnly(true);
		txtYDie.setReadOnly(true);
		cmbFlatOrientation.setReadOnly(true);
		cmbWaferSize.setReadOnly(true);
		
		userName.focus();
		
		setTxtValues();
		
		btnSubmit.addClickListener(e -> delete());
	}


	public boolean isSuccessful () {
		return isSuccessful;
	}
	
	private void setTxtValues () {
		txtProductID.setValue(this.device.getProductID());
		txtDeviceName.setValue(this.device.getDeviceName());
		cmbGoodBin.setValue(this.device.getGoodBin());
		txtXDie.setValue(this.device.getxDie());
		txtYDie.setValue(this.device.getyDie());
		cmbFlatOrientation.setValue(this.device.getFlatOrientation());
		cmbWaferSize.setValue(this.device.getWaferSize());
	}
	
	private void update(String userID) {
		if (isValid()) {
			 Device device = new Device(
					txtProductID.getValue().toUpperCase(),
					txtProductID.getValue().toUpperCase(),
					txtDeviceName.getValue().toUpperCase(),
					cmbGoodBin.getValue().toString(),
					txtXDie.getValue(),
					txtYDie.getValue(),
					cmbFlatOrientation.getValue().toString(),
					cmbWaferSize.getValue().toString(),
					userID,
					(new Date()).toString()
					);
			 
			 //if (validateAccount(userName.getValue(),password.getValue(),"MapConversionTableUsers")) {
				 if (xmlModifyRecord(device)) {
					 this.device.setGoodBin(device.getGoodBin());
					 this.device.setxDie(device.getxDie());
					 this.device.setyDie(device.getyDie());
					 this.device.setFlatOrientation(device.getFlatOrientation());
					 this.device.setWaferSize(device.getWaferSize());
					 this.device.setUserID(userID);
					 this.device.setDate(device.getDate());
					 this.isSuccessful = true;
					 close();
					 ServiceCommon.notificationMessage("Success","Existing record was updated successfully!");
				 }
				 else {
					 ServiceCommon.errorMessage("Error","Update existing record failed! ");
				}
			//}
		}
		else {
			ServiceCommon.warningMessage("Invalid Values", "Please enter valid values!");
		}
	}
	
	private void transfer(String type) {
		if (validateAccount(userName.getValue(),password.getValue(),type)) {		
			this.isSuccessful = true;
			ServiceCommon.notificationMessage("Processing Complete","Transfer processing complete. Please check the status!");
			close();
		}
	}
	
	private void upload(String type) {
		if (validateAccount(userName.getValue(),password.getValue(),type)) {		
			this.isSuccessful = true;
			close();
		}
	}
	
	private void add() {
		Device device = new Device(
				this.device.getProductID().toUpperCase(),
				this.device.getProductID().toUpperCase(),
				this.device.getDeviceName().toUpperCase(),
				this.device.getGoodBin(),
				this.device.getxDie(),
				this.device.getyDie(),
				this.device.getFlatOrientation(),
				this.device.getWaferSize(),
				userName.getValue(),
				(new Date()).toString()
				);
		
		if (validateAccount(userName.getValue(),password.getValue(),"MapConversionTableUsers")) {
			if (xmlAddRecord(device)) {
				
				this.device.setUserID(device.getUserID());
				this.device.setDate(device.getDate());
				
				this.mapConversionTable.add(this.device);
				this.isSuccessful = true;
				close();
				ServiceCommon.notificationMessage("Success","New record was added successfully!");
			}
			else {
				ServiceCommon.errorMessage("Error","Add new record failed! ");
			}
		}
	
	}

	private void delete() {
		if (validateAccount(userName.getValue(),password.getValue(),"MapConversionTableUsers")) {
			//if ((ServiceCommon.get , "MapConversionTableDeleteUsers")) {
				if (xmlDeleteRecord(this.device)) {
					this.mapConversionTable.remove(this.device);
					this.isSuccessful = true;
					close();
					ServiceCommon.notificationMessage("Success","Selected record was deleted successfully!");
				}
				else {
					ServiceCommon.errorMessage("Error","Delete selected record failed! ");
				}
			//}
		}
	}
	
	private boolean isValid() {
		boolean status = true;

		if (ServiceCommon.isEmpty(txtProductID)) {
			status = false;
		}
		
		if (ServiceCommon.isEmpty(txtDeviceName)) {
			status = false;
		}
		
		if (ServiceCommon.isEmpty(cmbGoodBin)) {
			status = false;
		}
				
		if (ServiceCommon.isEmpty(cmbFlatOrientation)) {
			status = false;
		}
		
		if (ServiceCommon.isEmpty(cmbWaferSize)) {
			status = false;
		}
			
		if (ServiceCommon.isEmpty(txtXDie,1,6000)) {
			status = false;
		}
    	
		if (ServiceCommon.isEmpty(txtYDie,1,6000)) {
			status = false;
		}

        return status;
	}
	
	private boolean validateAccount (String uname, String pword, String configProperty) {
		boolean isValid = true;
    	Hashtable env = new Hashtable();
    	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    	env.put(Context.PROVIDER_URL, "ldap://ad.onsemi.com:389");
    	
    	env.put(Context.SECURITY_AUTHENTICATION, "simple");
    	env.put(Context.SECURITY_PRINCIPAL, uname + "@ad.onsemi.com");
    	env.put(Context.SECURITY_CREDENTIALS, pword);
    	
    	if (ServiceCommon.isEmpty(userName)) {
    		isValid = false;
    		userName.focus();
    	}
    	if (ServiceCommon.isEmpty(password)) {
    		isValid = false;
    		password.focus();
    	}

		if (isValid) {		
			if (ServiceCommon.isRegisteredUser(uname,configProperty)) {
		    	try {
		    		logger.info("Validating [" + uname + "]");
		    	    DirContext ctx = new InitialDirContext(env);
		    	    ctx.close();
		    	    return true;
		    	} catch (AuthenticationNotSupportedException ex) {
		    		ServiceCommon.errorMessage("Login Error","The authentication is not supported by the server!");
		    	    logger.error("The authentication is not supported by the server!: ", ex);
		    	} catch (AuthenticationException ex) {
		    		userName.focus();
		    		ServiceCommon.warningMessage("Login Error","Incorrect password or username!");
		    		logger.warn("Incorrect password or username for [" + uname + "]!");
		    	} catch (NamingException ex) {
		    		userName.focus();
		    		ServiceCommon.errorMessage("Login Error","Error when trying to create the context!");
		    		logger.error("Error when trying to create the context!: ", ex);
		    	}
			}
			else {
				userName.focus();
				ServiceCommon.warningMessage("Login Error","You are not a registered user!");
				logger.warn("[" + uname + "] is not a registered user!");
			}
		}
		else {
			ServiceCommon.warningMessage("Login Error","Username or password cannot be empty!");
			logger.warn("Username or password cannot be empty!");
		}
		
		return false;
    }
	
	private boolean xmlAddRecord(Device device) {
		try {
			modifyXMLFile = new ServiceModifyXML(ServiceCommon.config.getProperty("XMLFile"));
			modifyXMLFile.add(device);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean xmlModifyRecord (Device device) {
		
		try {
			modifyXMLFile = new ServiceModifyXML(ServiceCommon.config.getProperty("XMLFile"));
			modifyXMLFile.updateElementValue(device);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean xmlDeleteRecord(Device device) {
		
			try {
				modifyXMLFile = new ServiceModifyXML(ServiceCommon.config.getProperty("XMLFile"));
				modifyXMLFile.deleteNode(device.getId());
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
				return false;
			} catch (TransformerException e) {
				e.printStackTrace();
				return false;
			}
		return true;
	}

}
