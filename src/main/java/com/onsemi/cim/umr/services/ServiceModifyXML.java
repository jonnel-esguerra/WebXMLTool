package com.onsemi.cim.umr.services;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.onsemi.cim.umr.model.Device;


public class ServiceModifyXML {
	
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	private String path;
	
	private final static Logger logger = Logger.getLogger(ServiceModifyXML.class);
	
	public ServiceModifyXML(String path) throws SAXException, IOException, ParserConfigurationException {
		this.path = path;
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();	
		doc = docBuilder.parse(path);
	}
	
	public void applyChanges () throws TransformerException {
		doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(this.path));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        ServiceCommon.log(ServiceModifyXML.class).info("XML file updated successfully");
       
	}
	
	public void deleteNode(String id) throws TransformerException {
		NodeList list = doc.getElementsByTagName("Product");
		int length = list.getLength();
		for (int i = 0; i < length; i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getAttribute("id").equals(String.valueOf(id))) {
					Node prev = node.getPreviousSibling();
					if (prev != null && prev.getNodeType() == Node.TEXT_NODE && prev.getNodeValue().trim().length() == 0) {
						doc.getDocumentElement().removeChild(prev);
					}
					doc.getDocumentElement().removeChild(element);
					break;
				}

			}
		}
		this.applyChanges();
	}
	
	public void updateElementValue(Device device) throws TransformerException {
		NodeList deviceNode = doc.getElementsByTagName("Product");
	    Element dev = null;
	    
	    for(int i=0; i<deviceNode.getLength();i++){
	       	dev = (Element) deviceNode.item(i);
	        Node devicename = dev.getElementsByTagName("ProductID").item(0).getFirstChild();
	            
	        if (devicename.getNodeValue().equals(device.getProductID())) {
	        	Node nodeDevName = dev.getElementsByTagName("Devicename").item(0).getFirstChild();
	        	device.setDeviceName(nodeDevName.getTextContent());
	        	
	        	Node nodeGoodBin = dev.getElementsByTagName("Goodbin").item(0).getFirstChild();
	        	nodeGoodBin.setTextContent(device.getGoodBin());
	        		
	        	Node nodeXdie = dev.getElementsByTagName("Xdie").item(0).getFirstChild();
	        	nodeXdie.setTextContent(device.getxDie());
	        		
	        	Node nodeYdie = dev.getElementsByTagName("Ydie").item(0).getFirstChild();
	        	nodeYdie.setTextContent(device.getyDie());
	        		
	        	Node nodeFlatOrientation = dev.getElementsByTagName("FlatOrientation").item(0).getFirstChild();
	        	nodeFlatOrientation.setTextContent(device.getFlatOrientation());
	        	
	        	Node nodeWaferSize = dev.getElementsByTagName("WaferSize").item(0).getFirstChild();
	        	nodeWaferSize.setTextContent(device.getWaferSize());
	        	
	        	Node nodeUser = dev.getElementsByTagName("User").item(0).getFirstChild();
	        	nodeUser.setTextContent(device.getUserID());
	        		
	        	Node nodeDate = dev.getElementsByTagName("Date").item(0).getFirstChild();
	        	nodeDate.setTextContent(device.getDate());
	        }
	    }

		this.applyChanges();
	}
	
	
	public Device getDeviceConversionTable(String deviceName) throws TransformerException {
		NodeList deviceNode = doc.getElementsByTagName("Product");
	    Element dev = null;
	    
	    Device device = new Device();
	    device.setProductID(deviceName);
	    
	    for(int i=0; i<deviceNode.getLength();i++){
	       	dev = (Element) deviceNode.item(i);
	        Node devicename = dev.getElementsByTagName("ProductID").item(0).getFirstChild();
	            
	        if (devicename.getNodeValue().equals(deviceName)) {
	        	Node nodeDevName = dev.getElementsByTagName("Devicename").item(0).getFirstChild();
	        	device.setDeviceName(nodeDevName.getTextContent());
	        	
	        	Node nodeGoodBin = dev.getElementsByTagName("Goodbin").item(0).getFirstChild();
	        	device.setGoodBin(nodeGoodBin.getTextContent());
	        		
	        	Node nodeXdie = dev.getElementsByTagName("Xdie").item(0).getFirstChild();
	        	device.setxDie(nodeXdie.getTextContent());
	        		
	        	Node nodeYdie = dev.getElementsByTagName("Ydie").item(0).getFirstChild();
	        	device.setyDie(nodeYdie.getTextContent());
	        		
	        	Node nodeFlatOrientation = dev.getElementsByTagName("FlatOrientation").item(0).getFirstChild();
	        	device.setFlatOrientation(nodeFlatOrientation.getTextContent());
	        	
	        	Node nodeWaferSize = dev.getElementsByTagName("WaferSize").item(0).getFirstChild();
	        	device.setWaferSize(nodeWaferSize.getTextContent());
	        		
	        }
	    }

		return device;
	}
	
	/**
	public void modify(String goodbin, String xdie, String ydie, String status) {
		Node conversion = doc.getFirstChild();

		Node device = doc.getElementsByTagName("device").item(0);

		NamedNodeMap attr = device.getAttributes();
		
		Node nodeGoodBin = attr.getNamedItem("goodbin");
		
		nodeGoodBin.setTextContent(goodbin);
		
		//Node nodeXdie = attr.getNamedItem("xdie");
		//nodeXdie.setTextContent(xdie);
		
		//Node nodeYdie = attr.getNamedItem("ydie");
		//nodeYdie.setTextContent(ydie);
		
		//Node nodeStatus = attr.getNamedItem("status");
		//nodeStatus.setTextContent(status);

	}
	 * @throws TransformerException 
	**/
	
	public void add(Device device) throws TransformerException {
		 Element root = doc.getDocumentElement();
		
		 Element newElement = doc.createElement("Product");
		 newElement.setAttribute("id",device.getProductID());
		 
         Element elementProductID = doc.createElement("ProductID");
         elementProductID.appendChild(doc.createTextNode(device.getProductID()));
         newElement.appendChild(elementProductID);
         
         Element elementDeviceName = doc.createElement("Devicename");
         elementDeviceName.appendChild(doc.createTextNode(device.getDeviceName()));
         newElement.appendChild(elementDeviceName);

         Element elementGoodbin = doc.createElement("Goodbin");
         elementGoodbin.appendChild(doc.createTextNode(device.getGoodBin()));
         newElement.appendChild(elementGoodbin);

         Element elementXdie = doc.createElement("Xdie");
         elementXdie.appendChild(doc.createTextNode(device.getxDie()));
         newElement.appendChild(elementXdie);
         
         Element elementYdie = doc.createElement("Ydie");
         elementYdie.appendChild(doc.createTextNode(device.getyDie()));
         newElement.appendChild(elementYdie);
            
         Element elementFlatOrientation = doc.createElement("FlatOrientation");
         elementFlatOrientation.appendChild(doc.createTextNode(device.getFlatOrientation()));
         newElement.appendChild(elementFlatOrientation);
         
         Element elementWaferSize = doc.createElement("WaferSize");
         elementWaferSize.appendChild(doc.createTextNode(device.getWaferSize()));
         newElement.appendChild(elementWaferSize);
      
         Element elementUser = doc.createElement("User");
         elementUser.appendChild(doc.createTextNode(device.getUserID()));
         newElement.appendChild(elementUser);
         
         Element elementDate = doc.createElement("Date");
         elementDate.appendChild(doc.createTextNode(device.getDate()));
         newElement.appendChild(elementDate);
         
         root.appendChild(newElement);
         
		 this.applyChanges();

	}


}
