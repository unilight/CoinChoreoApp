package model;

import javafx.scene.shape.Line;

public class TimelineLine extends Line {

	double startTime;
	double endTime;
	
	public TimelineLine() {
	}

	public TimelineLine(double startX, double startY, double endX, double endY) {
		super(startX, startY, endX, endY);
	}

	public TimelineLine(double startX, double startY, double endX, double endY, double sT, double eT) {
		super(startX, startY, endX, endY);
		this.startTime = sT;
		this.endTime = eT;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}
	
	
}
