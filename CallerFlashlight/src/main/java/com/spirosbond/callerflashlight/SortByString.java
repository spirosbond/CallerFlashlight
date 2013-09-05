package com.spirosbond.callerflashlight;

import java.util.Comparator;

/**
 * Created by spiros on 9/5/13.
 */
public class SortByString implements Comparator {

	public int compare(Object o1, Object o2) {
		Model p1 = (Model) o1;
		Model p2 = (Model) o2;
		int i = p1.getName().compareToIgnoreCase(p2.getName());
		if (i < 0) return -1;
		else if (i == 0) return 0;
		else return 1;
	}
}
