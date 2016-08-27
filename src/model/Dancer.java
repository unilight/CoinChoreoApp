package model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

public class Dancer {
	public int index;


	public double x,y;
	public Circle circle;
	public Path path;
	
	public Dancer() {
	}
	
	
	/**
	 * @param index
	 * @param x
	 * @param y
	 */
	public Dancer(int index, double x, double y, Circle circle) {
		this.index = index;
		this.x = x;
		this.y = y;
		this.circle = circle;
	}
	
	public Dancer(int index, Circle circle, Path path) {
		this.index = index;
		this.circle = circle;
		this.path = path;
	}

	/**
	 * @return the circle
	 */
	public Circle getCircle() {
		return circle;
	}

	/**
	 * @param circle the circle to set
	 */
	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
	public int getIndex() {
		return index;
	}


	public Path getPath() {
		return path;
	}
	
	
	
}