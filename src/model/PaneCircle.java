package model;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class PaneCircle extends Circle {
	
	double keyframeTime;

	public PaneCircle() {
	}

	public PaneCircle(double radius) {
		super(radius);
	}

	public PaneCircle(double radius, Paint fill) {
		super(radius, fill);
	}

	public PaneCircle(double centerX, double centerY, double radius) {
		super(centerX, centerY, radius);
	}

	public PaneCircle(double centerX, double centerY, double radius, Paint fill) {
		super(centerX, centerY, radius, fill);
	}
	public PaneCircle(double centerX, double centerY, double radius, Paint fill, double time) {
		super(centerX, centerY, radius, fill);
		this.keyframeTime = time;
	}

	public double getKeyframeTime() {
		return keyframeTime;
	}
	
	

}
