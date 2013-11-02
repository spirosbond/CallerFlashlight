package com.spirosbond.callerflashlight;

import java.util.Comparator;

/**
 * Created by spiros on 9/5/13.
 */
public class SortByCheck implements Comparator {


	public int compare(Object o1, Object o2) {
		Model p1 = (Model) o1;
		Model p2 = (Model) o2;

		if ((p1.isSelected()) && (!p2.isSelected())) return -1;
		else if ((p2.isSelected()) && (!p1.isSelected())) return 1;
		else return 0;
	}
}
