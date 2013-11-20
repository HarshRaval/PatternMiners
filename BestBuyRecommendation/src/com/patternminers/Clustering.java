package com.patternminers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Clustering {
	CheckQuerySimilarity cq = new CheckQuerySimilarity();

	public void performProductClustering(String productCSV) {
		ArrayList<Product> products = new ArrayList<Product>();
		try {
			FileReader fReader = new FileReader(productCSV);
			BufferedReader bReader = new BufferedReader(fReader);
			String line = bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				String[] columns = line.split(",");
				Product p = new Product(Long.parseLong(columns[0]), columns[2]);
				products.add(p);
			}
			bReader.close();
			fReader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		for (Product p : products) {
			getSimilarProducts(p, products);
		}
	}

	public void getSimilarProducts(Product p, ArrayList<Product> prods) {
		ArrayList<Long> hsSKU = new ArrayList<Long>();
		for (Product tp : prods) {
			String checkStr = tp.getName().replaceAll("xbox|360", "");
			String str = p.getName().replaceAll("xbox|360", "");
			if (cq.getDistance(str, checkStr) > 0.65) {
				if (!hsSKU.contains(tp.getSkuID())) {
					hsSKU.add(tp.getSkuID());
				}
				// System.out.println(name+ " & " +checkStr);;
			}
		}
		Collections.sort(hsSKU);
		// System.out.println(p.getSkuID()+ " = " +hsSKU.toString());
	}
}
