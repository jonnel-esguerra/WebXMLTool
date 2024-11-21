package com.onsemi.cim.umr.model;

public class Device {

	private String id;
	private String productID; //Family or Product Name
	private String deviceName; //Map Device Name
	private String goodBin;
	private String xDie;
	private String yDie;
	//private String status;
	private String flatOrientation;
	private String waferSize;
	private String userID;
	private String date;
		
	public Device () {
		
	}
	
	public Device (String id, String productID, String deviceName, String goodBin, String xDie, String yDie, String flatOrientation, String waferSize) {
		this();
		this.id = id;
		this.productID = productID;
		this.deviceName = deviceName;
		this.goodBin = goodBin;
		this.xDie = xDie;
		this.yDie = yDie;
		this.flatOrientation = flatOrientation;
		this.waferSize = waferSize;
	}
	
	public Device (String id, String productID, String deviceName, String goodBin, String xDie, String yDie, String flatOrientation,  String waferSize, String userID, String date) {
		this();
		this.id = id;
		this.productID = productID;
		this.deviceName = deviceName;
		this.goodBin = goodBin;
		this.xDie = xDie;
		this.yDie = yDie;
		this.flatOrientation = flatOrientation;
		this.waferSize = waferSize;
		this.userID = userID;
		this.date = date;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}
	
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getGoodBin() {
		return goodBin;
	}

	public void setGoodBin(String goodBin) {
		this.goodBin = goodBin;
	}

	public String getxDie() {
		return xDie;
	}

	public void setxDie(String xDie) {
		this.xDie = xDie;
	}

	public String getyDie() {
		return yDie;
	}

	public void setyDie(String yDie) {
		this.yDie = yDie;
	}

	public String getFlatOrientation() {
		return flatOrientation;
	}

	public void setFlatOrientation(String flatOrientation) {
		this.flatOrientation = flatOrientation;
	}
		
	public String getWaferSize() {
		return waferSize;
	}

	public void setWaferSize(String waferSize) {
		this.waferSize = waferSize;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString() {
		return "ProductID:" + this.productID + ", DeviceName:" + this.deviceName + ", GoodBin:" +  this.goodBin + ", XDie:" + this.xDie + ", YDie" + this.yDie + ", FlatOrientation:" + this.flatOrientation + ", WaferSize:" + this.waferSize + ", UserID:" + this.userID + ", Date:" + this.date;
	}
	
	public String toString2() {
		return this.getProductID() + "," +
			this.getDeviceName() + "," +
			this.getGoodBin() + "," +
			this.getxDie() + "," +
			this.getyDie() + "," +
			this.getFlatOrientation() + "," +
			this.getWaferSize() + "," +
			this.getUserID() + "," +
			this.getDate();
	}
	
}
