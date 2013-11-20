package com.patternminers;

import java.util.HashSet;
import java.util.regex.*;

public class WordCleaner {
	CheckQuerySimilarity cs = new CheckQuerySimilarity();

	public HashSet<String> loadProductNameWords(String productName,
			HashSet<String> words) {
		// HashSet<String> words = new HashSet<String>();
		String[] wordArray = productName.split(" ");
		for (String word : wordArray) {
			if (isNumeric(word) || word.equals(" ")) {
				continue;
			}
			words.add(word.trim());
		}
		return words;
	}

	public String matchQueryWordToProduct(String oldQuery,
			HashSet<String> prodWords) {
		String[] oldQueryArray = oldQuery.split(" ");
		// System.out.println(oldQuery);
		StringBuilder sb = new StringBuilder();
		for (String word : oldQueryArray) {
			if (isNumeric(word) || word.equals(" ")) {
				sb.append(word);
			} else {
				word = correctWord(word.trim(), prodWords);
				sb.append(word);
			}
			sb.append(" ");
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}

	private String correctWord(String word, HashSet<String> prodWords) {
		String newWord = null;
		float oldSimVal = Integer.MIN_VALUE;
		for (String s : prodWords) {
			float newSimVal = cs.getDistance(s, word);
			if (newSimVal > oldSimVal) {
				newWord = s;
				oldSimVal = newSimVal;
			}
		}
		return newWord;
	}

	private boolean isNumeric(String word) {
		return Pattern.matches("\\d+", word);
	}
}
