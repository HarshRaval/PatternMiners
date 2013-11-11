package com.patternminers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenerateProductCSV {

	public void generateCSVFromXML(String fileName, String csvFileName){
		HashSet<String> stopwords = new HashSet<String>();
		String stopwordFile = "xbox/stopwords.txt";
		stopwords = getStopwords(stopwords, stopwordFile);
		try{
			FileWriter fWriter = new FileWriter(csvFileName);
			PrintWriter pWriter = new PrintWriter(fWriter, false);
			pWriter.print("sku");
			pWriter.print(",");
			pWriter.print("productId");
			pWriter.print(",");
			pWriter.print("name");
			pWriter.print(",");
			pWriter.print("format");
			pWriter.print(",");
			pWriter.print("shortDescription");
			pWriter.print(",");
			pWriter.print("manufacturer");
			pWriter.print(",");
			pWriter.print("genre");
			pWriter.print(",");
			pWriter.print("active");
			pWriter.print(",");
			pWriter.print("customerReviewAverage");
			pWriter.print("\n");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileName);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("product");
			for(int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) nNode;
					pWriter.print(element.getElementsByTagName("sku").item(0).getTextContent().toString());
					pWriter.print(",");
					pWriter.print(element.getElementsByTagName("productId").item(0).getTextContent().toString());
					pWriter.print(",");
					
					String productName = replaceSpecialCharacters(element.getElementsByTagName("name").item(0).getTextContent().toString());
					productName = processStringValues(productName, stopwords);
					pWriter.print(productName);
					//pWriter.print(replaceSpecialCharacters(element.getElementsByTagName("name").item(0).getTextContent().toString()));
					pWriter.print(",");

					String formatvalue = processStringValues(element.getElementsByTagName("format").item(0).getTextContent().toString(), stopwords);
					formatvalue = replaceSpecialCharacters(formatvalue);
					pWriter.print(formatvalue);
					//pWriter.print(element.getElementsByTagName("format").item(0).getTextContent().toString());
					pWriter.print(",");

					String shortDesc = replaceSpecialCharacters(element.getElementsByTagName("shortDescription").item(0).getTextContent().toString());
					shortDesc = processStringValues(shortDesc, stopwords);
					pWriter.print(shortDesc);
					//pWriter.print(replaceSpecialCharacters(element.getElementsByTagName("shortDescription").item(0).getTextContent().toString()));
					pWriter.print(",");

					pWriter.print(checkNodeExistence(element, element.getElementsByTagName("manufacturer").getLength(), "manufacturer"));
					pWriter.print(",");

					pWriter.print(checkDetailsNode(element, element.getElementsByTagName("details").getLength(), "genre"));
					pWriter.print(",");
					pWriter.print(checkNodeExistence(element, element.getElementsByTagName("active").getLength(), "active"));
					pWriter.print(",");
					pWriter.print(checkNodeExistence(element, element.getElementsByTagName("customerReviewAverage").getLength(), "customerReviewAverage"));
					pWriter.print("\n");
				}
			}
			pWriter.flush();
			pWriter.close();
			fWriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * This function will process all the string values before writing them back to the product CSV file
	 */
	private String processStringValues(String rawValue, HashSet<String> stopHash){
		if(rawValue.equals("")){
			return rawValue;
		}
		StringBuilder sb = new StringBuilder();
		rawValue = rawValue.toLowerCase();
		String[] strArray = rawValue.split(" ");
		Arrays.sort(strArray);
		for(String str : strArray){
			if(stopHash.contains(str)){
				continue;
			}
			sb.append(str);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private HashSet<String> getStopwords(HashSet<String> stopHash, String file){
		try{
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);
			String line = null;
			while((line = bReader.readLine().trim()) == null){
				if(stopHash.contains(line)){
					continue;
				}
				stopHash.add(line);
			}
			bReader.close();
			fReader.close();
		}
		catch(Exception exp){
			exp.printStackTrace();
			System.exit(1);
		}
		return stopHash;
	}
	
	/*
	 * This function takes the individual product element, count of details tag and the XML tag whose value needs 
	 * to be returned back to the calling function
	 */
	private String checkDetailsNode(Element element, int nodeCount, String xmlTag){
		if(nodeCount == 0){
			//This value is returned when detail tag is not present
			return "";
		}
		String genre = "";
		NodeList detailsNode = element.getElementsByTagName("detail");
		for(int i = 0; i < detailsNode.getLength(); i++){
			Node tempNode = detailsNode.item(i);
			if(tempNode.getNodeType() == Node.ELEMENT_NODE){
				Element tempEl = (Element)tempNode;
				String str = tempEl.getElementsByTagName("name").item(0).getTextContent().toString();
				if(str.equals("Genre")){
					String genreValue = tempEl.getElementsByTagName("value").item(0).getTextContent().toString();
					genreValue = replaceSpecialCharacters(genreValue);
					//This value is returned when we have the detail tag as well as sub-tags with name-value pair 
					return genreValue;
				}
			}
		}
		//This value is returned we have a tag for detail but does not contain sub-tags showing Genre information
		return genre;
	}
	
	/*
	 * This function is used to check whether a particular node is present inside an element
	 * It takes the element, the node count count and the XML tag which needs to be checked and returns
	 * string value that is present within the tag
	 */
	private String checkNodeExistence(Element element, int nodeCount, String xmlTag){
		if(nodeCount == 0){
			return "";
		}
		String node = element.getElementsByTagName(xmlTag).item(0).getTextContent().toString();
		return node;
	}
	
	/*
	 * This function is used for replacing all the special characters found within a string by a space character
	 * It takes a string as an input and replaces all the special characters provided within the regular expression
	 * by a space and returns the string back
	 */
	private String replaceSpecialCharacters(String str){
		str = str.replaceAll("[()?:!.&,;-]+", "");
		return str;
	}
}
