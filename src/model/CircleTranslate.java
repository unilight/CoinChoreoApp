package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CircleTranslate {
	private DoubleProperty dpTranslateX, dpTranslateY;
	private int index;

	
	/**
	 * @param translateX
	 * @param translateY
	 */
	public CircleTranslate(int _index) {
		this.dpTranslateX = new SimpleDoubleProperty(0);
		this.dpTranslateY = new SimpleDoubleProperty(0);
		index = _index;
	}
	
	public CircleTranslate(int _index, double translateX, double translateY) {
		this.dpTranslateX = new SimpleDoubleProperty(translateX);
		this.dpTranslateY = new SimpleDoubleProperty(translateY);
		index = _index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getTranslateX() {
		// return translateX;
		return dpTranslateX.get();
	}

	public void setTranslateX(double translateX) {
		this.dpTranslateX.set(translateX);
	}

	public double getTranslateY() {
		// return translateY;
		return dpTranslateY.get();
	}

	public void setTranslateY(double translateY) {
		this.dpTranslateY.set(translateY);
	}

	public DoubleProperty getDpTranslateX() {
		return dpTranslateX;
	}

	public DoubleProperty getDpTranslateY() {
		return dpTranslateY;
	}
	
	@Override
	public String toString() {
		return "["+index+":"+dpTranslateX.get()+" "+dpTranslateY.get()+"]";
	}
	
}
