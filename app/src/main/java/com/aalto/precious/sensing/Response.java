package com.aalto.precious.sensing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by fida on 6.11.2015.
 */
public class Response {
    private String stringResponse;

    public void setStringResponse(String response) {
        this.stringResponse = response;
    }

    public WeatherStation parseStringResponse(String stringResponse) throws ParserConfigurationException, SAXException, IOException {
        if (this.stringResponse == null) {
            if (this.stringResponse.length() == 0)
                System.err.print("No stringResponse Set");
            return null;
        }
        System.err.println(this.stringResponse);
        Document document = openDocument(this.stringResponse);
//		System.out.println("document opened");
        WeatherStation weatherStation = parseDocument(document);
        if (weatherStation == null) {
            System.out.println("Error parsing document");
            return null;
        }
        System.out.println("got precious node");
        return weatherStation;
    }


    public Document openDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    public WeatherStation parseDocument(Document document) {
        Element element = document.getDocumentElement();
        String location = element.getAttribute("location");
//		System.out.println("location is "+ location);
        if (location.length() == 0) {
            System.out.println("No location specified");
            element.setAttribute("location", "Unkown Node");
        }

        WeatherStation station = new WeatherStation(location);
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Sensor sensor = station.getNewSensor();
                Element elem = (Element) node;

                // Get the value of the sensor_type attribute.
                String name = node.getAttributes().getNamedItem("sensor_type").getNodeValue();
                sensor.setName(name);

                // Get the value of all sub-elements.
                Float currentValue = Float.parseFloat(elem.getElementsByTagName("Current_Value")
                        .item(0).getChildNodes().item(0).getNodeValue());
                sensor.setSensorValue(currentValue);

                Float minThreshold = Float.parseFloat(elem.getElementsByTagName("Minimum_Threshold")
                        .item(0).getChildNodes().item(0).getNodeValue());
                sensor.setMinThreshold(minThreshold);

                Float maxThreshold = Float.parseFloat(elem.getElementsByTagName("Maximum_Threshold").item(0)
                        .getChildNodes().item(0).getNodeValue());
                sensor.setMaxThreshold(maxThreshold);

                String sampleTime = elem.getElementsByTagName("Sample_Time")
                        .item(0).getChildNodes().item(0).getNodeValue();
                sensor.setSampleTime(sampleTime);
                //populate derived data
                sensor.updateSensorInformation();
                //add to the list of sensors in weatherstation
                station.addSensor(sensor);

//				System.out.println("Added sensor" +sensor.name);
//
//				System.out.println("name ==" + name +"sensorValue==" +sensorValue +" minThreshold"
//						+minThreshold +"maxThreshold == " +maxThreshold );
            }
        }
        return station;
    }

}
