package com.pattern.miners;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class CheckQuerySimilarity {
	public float getDistance(String s1, String s2){
		float value = 0;
		value += new Levenshtein().getSimilarity(s1, s2);
		value += new JaroWinkler().getSimilarity(s1, s2);
		value += new EuclideanDistance().getSimilarity(s1, s2);
		value += new CosineSimilarity().getSimilarity(s1, s2);
		value = value/4.0f;
		return value;
	}
}
