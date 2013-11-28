package com.patternminers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;

public class KMeanClustering {

	private static void generateQueryFiles(String file) {
		HashSet<String> hs = new HashSet<String>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String line = bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				String[] lineArray = line.split(",");
				if (lineArray == null) {
					continue;
				}
				hs.add(String.valueOf(lineArray[3].trim()));
			}
			bReader.close();
			for (String s : hs) {
				if (s != null && s.trim().length() != 0) {
					BufferedWriter bWriter = new BufferedWriter(new FileWriter(
							"xbox/files1/" + s));
					bWriter.write(s.toString().trim());
					bWriter.close();
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		System.out.println(hs.size());
	}

	public void kMeanClustering() throws IOException {
		String file = "xbox/train_modified.csv";
		generateQueryFiles(file);

		String[] callAndArgs = { "python", "xbox/py-kmean.py", "500" };
		Process p = Runtime.getRuntime().exec(callAndArgs);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
		System.out.println("done");
		String clusterFile = "xbox/cluster_train.txt";
		readClusterFile(clusterFile, file);
		System.out.println("completed");
	}

	private static void readClusterFile(String clusterFile, String file) {
		ArrayList<String> queries = new ArrayList<String>();
		ArrayList<Integer> SKU = new ArrayList<Integer>();

		try {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String line = bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				String[] lineArray = line.split(",");
				if (lineArray.length < 1 || lineArray == null
						|| Float.parseFloat(lineArray[1]) > Integer.MAX_VALUE) {
					continue;
				}
				queries.add(String.valueOf(lineArray[3].trim()));
				SKU.add(Integer.valueOf(lineArray[1]));
			}
			bReader.close();
			// System.out.println(queries.size());
			// System.out.println(SKU.size());

			BufferedReader bReader1 = new BufferedReader(new FileReader(
					clusterFile));
			BufferedWriter bWriter = new BufferedWriter(new FileWriter("xbox/kmeans_train.txt"));
			String line1 = bReader1.readLine();
			while ((line1 = bReader1.readLine()) != null) {
				String[] splitColon = line1.split(":");
				if (splitColon.length < 2 || splitColon[1].split(",") == null) {
					continue;
				}
				String[] splitComma = splitColon[1].split(",");
				// System.out.println(Arrays.toString(splitComma));
				String centroidQuery1 = findCentroidEditDistance(splitComma);
				//String centroidQuery2 = findCentroidWordCount(splitComma);
				bWriter.write(centroidQuery1);
				bWriter.newLine();
				bWriter.flush();
			}
			bReader1.close();
			bWriter.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public static String findCentroidEditDistance(String[] queries) {
		HashMap<String, Float> hm = new HashMap<String, Float>();
		float max = Float.MIN_VALUE;
		String centroidQuery = null;
		for (int i = 0; i < queries.length; i++) {
			String query = queries[i];
			float[] distances = new float[queries.length];
			for (int j = 0; j < queries.length; j++) {
				float val = new EuclideanDistance().getSimilarity(query,
						queries[j]);
				distances[j] = val;
			}
			float total = Float.MIN_VALUE;
			for (int k = 0; k < distances.length; k++) {
				total += distances[k];
			}
			// System.out.println(query.toString()+ " " +total);
			if (total > max) {
				max = total;
				centroidQuery = query;
			}
			hm.put(query, total);
		}
		//System.out.println("Minimum " + max + " " + centroidQuery);
		StringBuilder sb = new StringBuilder();
		sb.append(centroidQuery);
		sb.append(":");
		for(String s : queries){
			sb.append(s);
			sb.append(",");
		}
		//System.out.println("query " + Arrays.toString(queries) + " " + centroidQuery);
		return sb.toString();
	}

}
