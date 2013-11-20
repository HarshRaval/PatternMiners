package com.patternminers;

import java.util.HashSet;

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
 * 
 * - WordCleaner.java
 * 
 * - CheckQuerySimilarity.java
 * 
 * - QueryClustering.java
 */

public class BestBuyRecommendation {

	private String train, trainModified;
	private String test, testModified;
	private String productXML, productCSV;

	public BestBuyRecommendation(String train, String trainModified,
			String test, String testModfied, String productXML,
			String productCSV) {
		this.train = train;
		this.trainModified = trainModified;
		this.testModified = testModfied;
		this.test = test;
		this.productXML = productXML;
		this.productCSV = productCSV;
	}

	public void preProcessing() throws Exception {
		HashSet<String> prodWords = new HashSet<String>();

		/*
		 * Generating CSV file from XML file by selecting only the required
		 * information The Function returns back a list of all the words which
		 * are a part product This word list will help us in normalizing all the
		 * queries so that we can eliminate noise such as spelling mistakes and
		 * incomplete words that can be entered by user
		 */
		/*
		 * GenerateProductCSV prod = new GenerateProductCSV(); prodWords =
		 * prod.generateCSVFromXML(productXML, productCSV);
		 * System.out.println("Completed generating CSV for Product");
		 * 
		 * QueryPreProcessing qp = new QueryPreProcessing();
		 * qp.preProcessQuery(train, trainModified, 3, prodWords);
		 * System.out.println("Completed Processing Training Files");
		 * qp.preProcessQuery(test, testModified, 2, prodWords);
		 * System.out.println("Completed Processing Test Files");
		 */}

	public void performClustering() {
		Clustering qc = new Clustering();
		qc.performProductClustering(productCSV);

		CheckQuerySimilarity c = new CheckQuerySimilarity();
	}

	public void runRecommendation(String answers) {
		System.out.println("Solution File Generated");
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		String train = "xbox/train.csv";
		String trainModified = "xbox/train_modified.csv";
		String test = "xbox/test.csv";
		String testModified = "xbox/test_modified.csv";
		String answers = "xbox/answers.csv";
		String productXML = "xbox/small_product_data.xml";
		String productCSV = "xbox/product.csv";

		BestBuyRecommendation bbr = new BestBuyRecommendation(train,
				trainModified, test, testModified, productXML, productCSV);
		bbr.preProcessing();
		bbr.performClustering();
		bbr.runRecommendation(answers);
		long endTime = System.currentTimeMillis();
		System.out.println("Time of execution in seconds = "
				+ (endTime - startTime) / 1000);
	}
}
