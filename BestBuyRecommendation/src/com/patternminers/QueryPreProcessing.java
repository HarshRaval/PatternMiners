package com.patternminers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

public class QueryPreProcessing {
	WordCleaner wc = new WordCleaner();

	public void preProcessQuery(String fileName, String outFile, int queryCol,
			HashSet<String> prodWords) {
		// HashSet for storing all the stop-words
		HashSet<String> stopwordsSet = new HashSet<String>();
		String stopwordFile = "xbox/stopwords.txt";
		FileReader fReader;
		BufferedReader bReader;
		try {
			fReader = new FileReader(stopwordFile);
			bReader = new BufferedReader(fReader);
			String line = null;
			while ((line = bReader.readLine()) != null) {
				// Adding new stop-words into the HashSet
				if (stopwordsSet.contains(line)) {
					continue;
				}
				stopwordsSet.add(line);
			}
			bReader.close();
			fReader.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			System.exit(1);
		}

		// Performing all pre-processing activities after after passing the
		// HashSet of stop-words and stop-word file
		preProcessQueryHelper(fileName, stopwordsSet, outFile, queryCol,
				prodWords);
	}

	private void preProcessQueryHelper(String fileName,
			HashSet<String> stopwordsSet, String outFile, int queryCol,
			HashSet<String> prodWords) {
		try {
			FileReader qFileReader = new FileReader(fileName);
			BufferedReader qBuffReader = new BufferedReader(qFileReader);
			FileWriter fWriter = new FileWriter(outFile);
			PrintWriter pWriter = new PrintWriter(fWriter);
			String line = null;
			while ((line = qBuffReader.readLine()) != null) {
				// Split the query into words and store into string array
				String[] columns = line.split(",");

				// Query is present at the index 3 of the string array
				String temp = columns[queryCol].trim();
				// System.out.println(temp);

				// Calling helper function for removing special characters
				temp = removeSpecialCharacters(temp);

				// Converting the query into lower case
				temp = temp.toLowerCase();

				// Helper function for removing the stop-words and sorting all
				// words
				temp = removeStopWordsAndSort(temp, stopwordsSet);
				
				// Helper function for correcting the query words as per the
				// product XML file provided
				temp = wc.matchQueryWordToProduct(temp, prodWords);

				// System.out.println(temp);

				for (int i = 0; i < columns.length; i++) {
					if (i == queryCol) {
						pWriter.print("\""+ temp + "\"" + ",");
					} else if(i == columns.length-1) {
						pWriter.print(columns[i]);
					}
					else {
						pWriter.print(columns[i] + ",");
					}
				}
				pWriter.println();
			}
			pWriter.close();
			fWriter.close();
			qBuffReader.close();
			qFileReader.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			System.exit(1);
		}
	}

	private String removeStopWordsAndSort(String query,
			HashSet<String> stopwordsSet) {
		String[] splitQuery = query.split(" ");
		Arrays.sort(splitQuery);

		StringBuilder sb = new StringBuilder();
		for (int i=0; i < splitQuery.length; i++) {
			// Performing stemming of individual word
			/*
			 * Stemmer st = new Stemmer(); st.add(str.toCharArray(),
			 * str.length()); st.stem(); str = st.toString();
			 */
			if (stopwordsSet.contains(splitQuery[i]) == true) {
				continue;
			}
			sb.append(splitQuery[i]);
			if(i != splitQuery.length-1)
				sb.append(" ");
		}
		return sb.toString();
	}

	/*
	 * This function is used for replacing all the special characters found
	 * within a string by a space character It takes a string as an input and
	 * replaces all the special characters provided within the regular
	 * expression by a space and returns the string back
	 */
	private String removeSpecialCharacters(String temp) {
		temp = temp.replaceAll("[()\'?:!/\"._,;-]+", " ");
		//temp = temp.replaceAll("[0-9]", "");
		return temp.trim();
	}
}
