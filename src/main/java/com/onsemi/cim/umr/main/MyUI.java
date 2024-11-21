package com.onsemi.cim.umr.main;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.onsemi.cim.umr.main.MyUI;
import com.onsemi.cim.umr.screen.ScreenMapConversionTable;
import com.onsemi.cim.umr.services.ServiceCommon;
import com.onsemi.cim.umr.services.ServiceConfiguration;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Title(value = "Map Conversion Table")
public class MyUI extends UI {

	private final TabSheet tabsheet = new TabSheet();
	private final VerticalLayout vLayoutMain = new VerticalLayout(); 
	private final Label headerLabel = new Label("Tarlac Wafer Map");
    
	@Override
    protected void init(VaadinRequest vaadinRequest) {
		
		try {
			ServiceCommon.loadProperties();
		} catch (Exception ex) {
			ServiceCommon.log(MyUI.class).error("Exeption : ", ex);
		}
		
		ServiceCommon.log(MyUI.class).info("Connected from [" + ServiceCommon.getIP() + "].");
		
        headerLabel.addStyleName(ValoTheme.LABEL_H1);

        tabsheet.addTab(new VerticalLayout(), "Home", VaadinIcons.HOME);
    	tabsheet.addTab(new ScreenMapConversionTable(), "Map Conversion Table", VaadinIcons.ARROW_CIRCLE_DOWN);
    	vLayoutMain.addComponents(headerLabel,tabsheet);
    	
        setContent(vLayoutMain);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
