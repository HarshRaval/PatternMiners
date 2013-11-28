package com.patternminers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class Mapping {

	static ArrayList<String> popSkus;

	public void QueryAlgo(String[] args) {

		String clusFile = "xbox/kmeans_train.txt";
		String trainFile = args[0];
		String testFile = args[1];

		HashMap<String, Integer> skuMap = new HashMap<String, Integer>();
		HashMap<String, ArrayList<String>> qSkuMap = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> cluster = new HashMap<String, ArrayList<String>>();
		popSkus = new ArrayList<String>();
		popSkus.add("9854804");
		popSkus.add("2107458");
		popSkus.add("2541184");
		popSkus.add("2670133");
		popSkus.add("2173065");

		// make map of sku and count
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(
					trainFile));
			String line = bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				String[] lineArray = line.split(",");
				String sku = lineArray[1].trim();
				String query = lineArray[3].trim();
				skuMap.put(sku, (skuMap.get(sku) == null) ? 1
						: skuMap.get(sku) + 1);
				ArrayList<String> list = qSkuMap.get(query);
				if (list == null) {
					list = new ArrayList<String>();
					list.add(sku);
				} else {
					if (!list.contains(sku)) {
						list.add(sku);
					}
				}
				qSkuMap.put(query, list);
			}
			bReader.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		// make map of cluster
		try {
			BufferedReader bReader = new BufferedReader(
					new FileReader(clusFile));
			String line = bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				String[] lineArray = line.split(":");
				String centroid = lineArray[0].trim();
				String[] queries = lineArray[1].split(",");
				ArrayList<String> list = new ArrayList<String>();
				for (String q : queries) {
					list.add(q);
				}
				cluster.put(centroid, list);
			}
			bReader.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		try {
			BufferedReader bReader = new BufferedReader(
					new FileReader(testFile));
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(args[2]));
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] lineArray = line.split(",");
				String query = lineArray[2].trim();
				String centroid = getCentroid(query, cluster);
				ArrayList<String> finalList = new ArrayList<String>();
				if (centroid == null) {
					finalList = getPopularSkus(5, finalList);
				} else {
					ArrayList<String> sList = getSkuList(centroid, cluster,
							qSkuMap);
					HashMap<String, Integer> countMap = getSkuCount(sList,
							skuMap);
					Map<String, Integer> sortedMapDesc = sortByComparator(
							countMap, false);
					int size = countMap.size();
					if (size > 5) {
						int i = 0;
						for (String s : sortedMapDesc.keySet()) {
							finalList.add(s);
							i++;
							if (i == 5)
								break;
						}
					} else if (size < 5) {
						for (String s : sortedMapDesc.keySet()) {
							finalList.add(s);
						}
						finalList = getPopularSkus(5 - size, finalList);

					} else {
						for (String s : sortedMapDesc.keySet()) {
							finalList.add(s);
						}
					}
				}
				for (String s : finalList) {
					bWriter.write(s);
					bWriter.write(" ");
				}
				bWriter.write("\n");
				bWriter.flush();
			}
			bWriter.close();
			bReader.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private static HashMap<String, Integer> getSkuCount(
			ArrayList<String> sList, HashMap<String, Integer> skuMap) {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for (String s : sList) {
			Integer count = skuMap.get(s);
			countMap.put(s, count);
		}
		return countMap;
	}

	private static ArrayList<String> getSkuList(String centroid,
			HashMap<String, ArrayList<String>> cluster,
			HashMap<String, ArrayList<String>> qSkuMap) {
		ArrayList<String> sList = new ArrayList<String>();
		ArrayList<String> qList = cluster.get(centroid);
		for (String q : qList) {
			ArrayList<String> list = qSkuMap.get(q);
			if (list != null) {
				for (String s : list) {
					if (!sList.contains(s))
						sList.add(s);
				}
			}
		}
		return sList;
	}

	private static ArrayList<String> getPopularSkus(int num,
			ArrayList<String> finalList) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			list.add(popSkus.get(i));
		}
		finalList.addAll(list);
		return finalList;
	}

	public static String getCentroid(String query,
			HashMap<String, ArrayList<String>> cluster) {
		String centroid = null;
		HashMap<String, Float> cDist = new HashMap<String, Float>();
		for (String cent : cluster.keySet()) {
			cDist.put(cent, new Levenshtein().getSimilarity(query, cent));
		}
		Map<String, Float> sortedMapDesc = sortByComparatorFloat(cDist, false);
		for(String s : sortedMapDesc.keySet()){
			centroid = s;
			break;
		}

		return centroid;
	}

	private static Map<String, Integer> sortByComparator(
			Map<String, Integer> unsortMap, final boolean order) {
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
	
	private static Map<String, Float> sortByComparatorFloat(
			Map<String, Float> unsortMap, final boolean order) {
		List<Entry<String, Float>> list = new LinkedList<Entry<String, Float>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Float>>() {
			public int compare(Entry<String, Float> o1,
					Entry<String, Float> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for (Entry<String, Float> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}
