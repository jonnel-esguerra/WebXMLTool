package com.onsemi.cim.umr.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.onsemi.cim.umr.main.MyUI;
import com.onsemi.cim.umr.model.Device;
import com.onsemi.cim.umr.services.ServiceCommon;
import com.onsemi.cim.umr.services.ServieParseXML;
import com.vaadin.annotations.Title;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ScreenMapConversionTable extends VerticalLayout {
	private final Panel panel = new Panel("");
	private final VerticalLayout main = new VerticalLayout();
	
	private final HorizontalLayout hLayoutInput = new HorizontalLayout(); 
	private final HorizontalLayout hLayoutUpdate = new HorizontalLayout();
    
	private final TextField txtProductID = new TextField("Product ID");
	private final TextField txtDeviceName = new TextField("Device Name");
	private final ComboBox cmbGoodBin = new ComboBox("Good Bin");
	private final TextField txtXDie = new TextField("X Die Size");
	private final TextField txtYDie = new TextField("Y Die Size");
	//private final ComboBox cmbStatus = new ComboBox("Status");
	private final ComboBox cmbFlatOrientation = new ComboBox("Flat Orientation");
	private final ComboBox cmbWaferSize = new ComboBox("Wafer Size");
	private final Button btnAdd = new Button();
	private final Button btnDelete = new Button("Delete");
	private final Button btnEdit = new Button("Update");
	
	private final Grid<Device> gridDevices = new Grid<>(Device.class);
	private final TextField txtSearch = new TextField();
	private final Button btnClear = new Button();
	
	private List<Device> mapConversionTable = new ArrayList<>();
    private Device selectedRecord; 
    private Boolean isLogin = false;
    private String userID = "";
	
    public ScreenMapConversionTable () {
        txtXDie.setPlaceholder("0");
        txtYDie.setPlaceholder("0");
        btnAdd.setIcon(VaadinIcons.PLUS_CIRCLE);
        btnAdd.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnEdit.setIcon(VaadinIcons.CHECK_CIRCLE);
        btnEdit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnDelete.setIcon(VaadinIcons.MINUS_CIRCLE);
        btnDelete.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        
        gridDevices.setSizeFull();
        gridDevices.setColumnOrder("productID","deviceName","goodBin","xDie","yDie","flatOrientation","waferSize", "userID","date");
        gridDevices.removeColumn("id");		
        
        txtSearch.setValueChangeMode(ValueChangeMode.LAZY);
        txtSearch.setPlaceholder("Enter keyword");
        txtSearch.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtSearch.setIcon(VaadinIcons.SEARCH);
  
        btnClear.setIcon(VaadinIcons.CLOSE);
        btnClear.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnClear.setDescription("Clear the current filter");
        
        hLayoutInput.addComponents(
        		txtProductID,
        		txtDeviceName,
        		cmbGoodBin,
        		txtXDie,
        		txtYDie,
        		cmbFlatOrientation,
        		cmbWaferSize,
        		btnAdd,
        		txtSearch,
        		btnClear
        		);
        
        hLayoutInput.setComponentAlignment(btnAdd,Alignment.BOTTOM_LEFT);
        hLayoutInput.setComponentAlignment(txtSearch,Alignment.BOTTOM_RIGHT);
        hLayoutInput.setComponentAlignment(btnClear,Alignment.BOTTOM_RIGHT);
        
        hLayoutUpdate.addComponents(btnEdit,btnDelete);
        hLayoutUpdate.setComponentAlignment(btnEdit,Alignment.BOTTOM_RIGHT);
        hLayoutUpdate.setComponentAlignment(btnDelete,Alignment.BOTTOM_RIGHT);
        
        
        main.addComponents(hLayoutInput,gridDevices,hLayoutUpdate);
        main.setComponentAlignment(hLayoutUpdate,Alignment.BOTTOM_RIGHT);
        
        panel.setContent(main);
        this.addComponent(panel);
        
        cmbGoodBin.setItems(ServiceCommon.getGoodBin());
		cmbGoodBin.setEmptySelectionAllowed(false);
		cmbFlatOrientation.setItems(ServiceCommon.getFlatOrientation());
		cmbFlatOrientation.setEmptySelectionAllowed(false);
		cmbWaferSize.setItems(ServiceCommon.getWaferSize());
		cmbWaferSize.setEmptySelectionAllowed(false);
		
		cmbGoodBin.setWidth("100px");
		txtXDie.setWidth("120px");
		txtYDie.setWidth("120px");
		cmbFlatOrientation.setWidth("120px");
		cmbWaferSize.setWidth("120px");
		
        this.mapConversionTable = ServiceCommon.getXMLList(txtSearch.getValue());
		gridDevices.setItems(mapConversionTable);
        
        //EVENT LISTENERS
		btnClear.addClickListener(e -> {txtSearch.clear(); txtSearch.focus();});
		txtSearch.addValueChangeListener(e -> updateGrid());
        btnAdd.addClickListener(e -> add());
        btnEdit.addClickListener(e -> login());
        btnDelete.addClickListener(e -> delete());
        gridDevices.asSingleSelect().addValueChangeListener( e -> selectedRow(e));
        txtProductID.addValueChangeListener(e -> ServiceCommon.isEmpty(txtProductID));
        txtDeviceName.addValueChangeListener(e -> ServiceCommon.isEmpty(txtDeviceName));
        cmbGoodBin.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbGoodBin));
        txtXDie.addValueChangeListener(e -> ServiceCommon.isEmpty(txtXDie,1,6000));
        txtYDie.addValueChangeListener(e -> ServiceCommon.isEmpty(txtYDie,1,6000));
        cmbFlatOrientation.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbFlatOrientation));
        cmbWaferSize.addValueChangeListener(e -> ServiceCommon.isEmpty(cmbWaferSize));
	}
    
    private void updateGrid() {
		this.mapConversionTable = ServiceCommon.getXMLList(txtSearch.getValue());
		gridDevices.setItems(mapConversionTable);
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

	private void selectedRow(ValueChangeEvent<Device> device) {
		if (device.getValue() != null) {
			this.selectedRecord = device.getValue();
    	}
	}
	
	private void add() {
		if (isValid()) {
			Device device = new Device(
					txtProductID.getValue().toUpperCase(),
					txtProductID.getValue().toUpperCase(),
					txtDeviceName.getValue().toUpperCase(),
					cmbGoodBin.getValue().toString(),
					txtXDie.getValue(),
					txtYDie.getValue(),
					cmbFlatOrientation.getValue().toString(),
					cmbWaferSize.getValue().toString()
					);
			
			if (ServiceCommon.findDuplicate(device.getDeviceName(),mapConversionTable)) {
				ServiceCommon.warningMessage("Duplicate","Duplicate Wafer Device Name is not allowed!");
			}
			else {
			
				ScreenConfirmUpdate screenConfirmUpdate = new ScreenConfirmUpdate(device,mapConversionTable);
				UI.getCurrent().addWindow(screenConfirmUpdate);
				screenConfirmUpdate.addCloseListener(closeEvent -> {
					gridDevices.getDataProvider().refreshAll();
					if (screenConfirmUpdate.isSuccessful()) {
						gridDevices.select(device);
					} 
				});
			}
		}
		else {
			ServiceCommon.warningMessage("Invalid Values", "Please enter valid values!");
		}
	}
	
	private void login() {
		if (this.selectedRecord == null) {
			ServiceCommon.warningMessage("No Record","Please select a record from the grid!");
		}
		else {
				ScreenLogin screenLogin = new ScreenLogin(isLogin);
				UI.getCurrent().addWindow(screenLogin);
				screenLogin.addCloseListener(closeEvent -> {
					gridDevices.getDataProvider().refreshAll();
					if (screenLogin.isLogin()) {
						update(screenLogin.userID());
					}
				});				
		}
	}
	
	private void update(String userID) {
		if (this.selectedRecord == null) {
			ServiceCommon.warningMessage("No Record","Please select a record from the grid!");
		}
		else {
				this.userID = userID;
				ScreenConfirmUpdate screenConfirmUpdate = new ScreenConfirmUpdate(this.selectedRecord, this.userID);
				UI.getCurrent().addWindow(screenConfirmUpdate);
				screenConfirmUpdate.addCloseListener(closeEvent -> {
					gridDevices.getDataProvider().refreshAll();
					if (screenConfirmUpdate.isSuccessful()) {
						gridDevices.select(this.selectedRecord);
					}
				});				
		}
	}
	    
	private void delete() {
		if (this.selectedRecord == null) {
			ServiceCommon.warningMessage("No Record","Please select a record from the grid!");
		}
		else {
			ScreenConfirmUpdate screenConfirmUpdate = new ScreenConfirmUpdate(mapConversionTable,this.selectedRecord);
			UI.getCurrent().addWindow(screenConfirmUpdate);
			screenConfirmUpdate.addCloseListener(closeEvent -> {
				gridDevices.getDataProvider().refreshAll();
				if (screenConfirmUpdate.isSuccessful()) {
					this.selectedRecord = null;
				}
			});
		}
	}
	
	
		
}
