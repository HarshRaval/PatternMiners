package com.patternminers;
/*
 * This is the MainClass file which calls other helper files for performing the Recommendation Tasks
 * 
 * - GenerateProductCSV.java is called to parse the XML file and generate a CSV file containing 
 * 		only those information which may be required for our recommendation system
 * 
 * - QueryPreProcessing.java is called for processing the the queries present in the train and test file and 
 * 		we get a modified version of both the files which contains the processed queries
 * 
 * - Stemmer.java is used of stemming the words, it takes individual words and returns back the word
 * 		after performing the stemming information 
 */

public class BestBuyRecommendation {

	public static void main(String[] args) {
		GenerateProductCSV product = new GenerateProductCSV();
		product.generateCSVFromXML("xbox/small_product_data.xml", "xbox/product.csv");
		System.out.println("Completed generating CSV for Product");
		
		QueryPreProcessing qp = new QueryPreProcessing();
		//Pre-processing the Training File
		qp.preProcessQuery("xbox/train.csv", "xbox/train_modified.csv", 3);
		//Pre-processing the Test File
		qp.preProcessQuery("xbox/test.csv", "xbox/test_modified.csv", 2);
		System.out.println("Completed Query Preprocessing");
		
	}
}
