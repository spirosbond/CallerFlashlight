package com.spirosbond.callerflashlight;


import android.graphics.drawable.Drawable;

/**
 * Created by spiros on 9/5/13.
 */


public class Model {

	private String name;
	private Drawable label;
	private String packageName;
	private boolean selected;

	public Model(String name) {
		this.name = name;
		selected = false;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getLabel() {
		return label;
	}

	public void setLabel(Drawable label) {
		this.label = label;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
