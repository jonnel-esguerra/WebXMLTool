package com.onsemi.cim.umr.screen;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

import com.onsemi.cim.umr.model.Device;
import com.onsemi.cim.umr.services.ServiceCommon;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ScreenLogin extends Window {

	private final Panel panel = new Panel();
	private final FormLayout form = new FormLayout();
	private final HorizontalLayout hLayoutButtons = new HorizontalLayout();
	
	private final Button btnSubmit = new Button("Submit");
	private final Button btnCancel = new Button("Cancel");	
	private final TextField userName = new TextField("User Name");
	private final PasswordField  password = new PasswordField ("Password");
	
	private final Label message = new Label("null");
	
	private final static Logger logger = Logger.getLogger(ScreenLogin.class);
	
	private boolean isLogin = false;
	private String userID = "";
	
	public ScreenLogin() {
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
	
	private void mapConversionFieldsLogin() {
		form.addComponents(
				message,
				userName,
				password,
				hLayoutButtons
				);	
	}
	
	public ScreenLogin(Boolean isLogin) {
		this();
		mapConversionFieldsLogin();
		this.isLogin = isLogin;
		
		
		panel.setCaption("Login Screen");
		message.setValue("Login Using AD Account");
		
		btnSubmit.focus();
				
        btnSubmit.addClickListener(e -> submit());
	}
	
	private void submit() {
		 if (validateAccount(userName.getValue(),password.getValue(),"MapConversionTableUsers")) {
			 this.isLogin = true;
			 			 
			 String userID = new String(userName.getValue());
			 this.userID = userID;
			 close();
		 }		
	}
	
	public boolean isLogin () {
		return isLogin;
	}
	
	public String userID () {
		return userID;
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
	
}
