package com.patternminers;

public class Product {
	String name;
	long skuID;

	public Product() {
		this.name = null;
		this.skuID = Long.MIN_VALUE;
	}

	public Product(long skuID, String name) {
		this.skuID = skuID;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSkuID() {
		return skuID;
	}

	public void setSkuID(long skuID) {
		this.skuID = skuID;
	}
}
