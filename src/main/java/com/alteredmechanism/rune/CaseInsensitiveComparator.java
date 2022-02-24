package com.alteredmechanism.rune;

import java.util.Comparator;

public class CaseInsensitiveComparator implements Comparator<String> {

	public int compare(String s, String t) {
		return s.toString().compareToIgnoreCase(t.toString());
	}

}
