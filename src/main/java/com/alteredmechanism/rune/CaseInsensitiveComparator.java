package com.alteredmechanism.rune;

import java.util.Comparator;

public class CaseInsensitiveComparator implements Comparator {

	public int compare(Object s, Object t) {
		return s.toString().compareToIgnoreCase(t.toString());
	}

}
