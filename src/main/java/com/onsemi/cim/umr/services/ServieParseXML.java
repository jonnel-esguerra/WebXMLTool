package com.onsemi.cim.umr.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.onsemi.cim.umr.model.Device;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ServieParseXML {
	
	public ArrayList parseXml(InputStream in)
    {
        //Create a empty link of users initially
        ArrayList<Device> devices = new ArrayList<Device>();
        try
        {
            //Create default handler instance
            XMLParserHandler handler = new XMLParserHandler();
 
            //Create parser from factory
            XMLReader parser = XMLReaderFactory.createXMLReader();
 
            //Register handler with parser
            parser.setContentHandler(handler);
 
            //Create an input source from the XML input stream
            InputSource source = new InputSource(in);
 
            //parse the document
            parser.parse(source);
 
            //populate the parsed users list in above created empty list; You can return from here also.
            devices = handler.getDevices();
 
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
 
        }
        return devices;
    }

}

class XMLParserHandler extends DefaultHandler{

	//This is the list which shall be populated while parsing the XML.
    private ArrayList deviceList = new ArrayList();
 
    //As we read any XML element we will push that in this stack
    private Stack elementStack = new Stack();
 
    //As we complete one user block in XML, we will push the User instance in userList
    private Stack objectStack = new Stack();
 
    public void startDocument() throws SAXException
    {
        //System.out.println("start of the document   : ");
    }
 
    public void endDocument() throws SAXException
    {
        //System.out.println("end of the document document     : ");
    }
 
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        //Push it in element stack
        this.elementStack.push(qName);
 
        //If this is start of 'user' element then prepare a new User instance and push it in object stack
        if ("Product".equals(qName))
        {
            //New User instance
        	Device device = new Device();
 
            //Set all required attributes in any XML element here itself
            if(attributes != null && attributes.getLength() == 1)
            {
            	device.setId(attributes.getValue(0));
            }
            this.objectStack.push(device);
        }
    }
 
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        //Remove last added  element
        this.elementStack.pop();
 
        //User instance has been constructed so pop it from object stack and push in userList
        if ("Product".equals(qName))
        {
        	Device object = (Device) this.objectStack.pop();
            this.deviceList.add(object);
        }
    }
 
    /**
     * This will be called everytime parser encounter a value node
     * */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        String value = new String(ch, start, length).trim();
 
        if (value.length() == 0)
        {
            return; // ignore white space
        }
 
        //handle the value based on to which element it belongs
        if ("ProductID".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setProductID(value);
        	device.setId(value);
        }
        else if ("Devicename".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setDeviceName(value);
        }
        else if ("Goodbin".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setGoodBin(value);
        }
        else if ("Xdie".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setxDie(value);
        }
        else if ("Ydie".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setyDie(value);
        }
        else if ("FlatOrientation".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setFlatOrientation(value);
        }
        else if ("WaferSize".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setWaferSize(value);
        }
        else if ("User".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setUserID(value);
        }
        else if ("Date".equals(currentElement()))
        {
        	Device device = (Device) this.objectStack.peek();
        	device.setDate(value);
        }
    }
 
    /**
     * Utility method for getting the current element in processing
     * */
    private String currentElement()
    {
        return (String) this.elementStack.peek();
    }
 
    //Accessor for userList object
    public ArrayList getDevices()
    {
        return deviceList;
    }
	
}
