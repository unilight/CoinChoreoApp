package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Keyframe {
	
	private Circle paneCircle;
	private List<CircleTranslate> circleTranslates;
	private Duration time;
	

	public Keyframe(List<CircleTranslate> _circleTranslates, Duration time) {
		this.circleTranslates = new ArrayList<CircleTranslate>(_circleTranslates.size());
		for(CircleTranslate element : _circleTranslates){
			this.circleTranslates.add(element);
		}
		this.time = time;
	}

	public Circle getPaneCircle() {
		return paneCircle;
	}

	public void setPaneCircle(Circle paneCircle) {
		this.paneCircle = paneCircle;
	}

	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

	public List<CircleTranslate> getCircleTranslates() {
		return circleTranslates;
	}
	
	public final CircleTranslate getCircleTranslateByIndex(int index) {
		for(CircleTranslate circleTranslate : circleTranslates){
			if(circleTranslate.getIndex() == index){
				return circleTranslate;
			}
		}
		return null;
	}

	public void setCircleTranslates(List<CircleTranslate> _circleTranslates) {
		this.circleTranslates = new ArrayList<CircleTranslate>(_circleTranslates.size());
		for(CircleTranslate element : _circleTranslates){
			this.circleTranslates.add(element);
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		s += (time.toSeconds()+" ");
		for(int i=0; i<circleTranslates.size();i++){
			s +=(circleTranslates.get(i).toString()+" ");
		}
		return s;
	}
	
	
}
