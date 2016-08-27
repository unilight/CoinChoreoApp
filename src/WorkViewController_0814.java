/*
package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import control.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.Math;
import model.Proj;
import model.CircleTranslate;
import model.Dancer;
import model.IndexManager;

public class WorkViewController {
	
	public final static int CIRCLE_RADIUS=10;
	public final static int SELECT_RADIUS=12;
	public final static int LINE_WIDTH=2;
	public final static int ERASE_WIDTH=5;
	public final static int CANVAS_WIDTH=600;
	public final static int CANVAS_HEIGHT=200;
	
	@FXML
	private Label time;
	@FXML
	private Slider slider;
	
	@FXML
	private ToggleGroup toggleGroup;
	@FXML
	private ToggleButton addToggle;
	@FXML
	private ToggleButton groupToggle;
	@FXML
	private ToggleButton deleteToggle;
	@FXML
	private AnchorPane drawPane;
	
	private Proj curProj;
	
	private IndexManager indexManager;
	private List<Dancer> dancers = new ArrayList<Dancer>();
	private List<Circle> circles = new ArrayList<Circle>();
	private List<CircleTranslate> circleTranslates = new ArrayList<CircleTranslate>();
	private List<Circle> groupedCircles = new ArrayList<Circle>();
	private List<Path> groupedPaths = new ArrayList<Path>();
	
	private double orgSceneX, orgSceneY;

	Timeline timeline  = new Timeline(); 
	
    /* Music 
    private String path = "music/TheBar-Kays-Propositions(Funk).mp3";
    Media media;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	Duration duration;
    
	private Stage dialogStage;
	private MainApp mainApp;
	
	@FXML
	private void initialize(){
		
	}
	
	public WorkViewController(){
		
	}
	
	public void setProj(Proj _proj){
		this.curProj = _proj;
		indexManager = new IndexManager(curProj.getNumOfDancers());
		
		// Dancer Init
		//maxNum = curProj.getNumOfDancers();
	}
	
	public void setMainApp(MainApp _mainApp){
		this.mainApp = _mainApp;
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	@FXML
	private void drawCircle(MouseEvent e) {
        //System.out.println(e.getSource());
        //System.out.println(e.getTarget());
        //System.out.println(e.getEventType());
        System.out.format("x:%f, y:%f%n", e.getSceneX(), e.getSceneY());
        System.out.format("x:%f, y:%f%n", e.getScreenX(), e.getScreenY());
        System.out.println(e.getTarget().toString());
        
        double x=e.getSceneX();
		double y=e.getSceneY()-205;
		
		if( !(e.getTarget() instanceof Circle) ){
		
			if(addToggle.isSelected() && curProj.getCurrentDancersDrawn()<curProj.getNumOfDancers()){
					
				int index = indexManager.deque();
					
				Circle circle = new Circle(x,y,CIRCLE_RADIUS, Proj.colors[index]);
				circle.setOnMousePressed(circleOnMousePressedEventHandler);
				circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
				circle.setOnMouseReleased(circleOnMouseReleaseEventHandler);
				circle.setOnMouseMoved(circleOnMouseMovedEventHandler);	
					
				Path path = drawSelectionPath(x, y);
				path.setVisible(false);
				groupedPaths.add(path);
			        
				Dancer newDancer = new Dancer(index, circle);
				curProj.addDancer(newDancer);
					
				circleTranslates.add(new CircleTranslate(index));
				//circleTranslates.set(index, new CircleTranslate(index));
				//circle.translateXProperty().bind();
				
				drawPane.getChildren().addAll(circle,path);
        		
				//dancers.add(new Dancer(currentDancersDrawn, x, y));
				circles.add(circle);      		        		
			}
			
			// clear the group
			if(groupedCircles.size()>0){
				for (Circle circle : groupedCircles) {
		        	int index = curProj.getDancerIndex(circle);
		        	groupedPaths.get(index).setVisible(false);
				}
				groupedCircles.clear();
				// System.out.println(groupedCircles.size()+"");
			}
        }
        	
        
    }

	private Path drawSelectionPath(double x, double y) {
		Path path = new Path();
		path.setStroke(Color.RED);
		path.setStrokeWidth(3);
		MoveTo moveTo = new MoveTo();
		moveTo.setX(x - CIRCLE_RADIUS);
		moveTo.setY(y);
		ArcTo arcToInner = new ArcTo();
		arcToInner.setX(x + CIRCLE_RADIUS);
		arcToInner.setY(y);
		arcToInner.setRadiusX(CIRCLE_RADIUS);
		arcToInner.setRadiusY(CIRCLE_RADIUS);
		MoveTo moveTo2 = new MoveTo();
		moveTo2.setX(x+CIRCLE_RADIUS);
		moveTo2.setY(y);
		ArcTo arcToInner2 = new ArcTo();
		arcToInner2.setX(x - CIRCLE_RADIUS);
		arcToInner2.setY(y);
		arcToInner2.setRadiusX(CIRCLE_RADIUS);
		arcToInner2.setRadiusY(CIRCLE_RADIUS);
		path.getElements().addAll(moveTo, arcToInner, moveTo2, arcToInner2);
		return path;
	}
	
	EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent t) {
	    	Circle circle = (Circle) (t.getSource());
	    	int index = curProj.getDancerIndex(circle);
	    	if(deleteToggle.isSelected()){
	    		if(groupedCircles.contains(circle)){
	    			groupedCircles.remove(circle);
	    		}
	    		drawPane.getChildren().remove(circle);
	    		indexManager.enque(curProj.getDancerIndex(circle));
	    		System.out.println("Deleting Dancer "+curProj.getDancerIndex(circle));
	    		curProj.removeDancer(circle);
	    	} else if(groupToggle.isSelected()){
	    		if(!groupedCircles.contains(circle)){
	    			groupedCircles.add(circle);
	    			groupedPaths.get(curProj.getDancerIndex(circle)).setVisible(true);
	    			
	    		}
	    		System.out.println(groupedCircles.size());
	    	}
	    	if(deleteToggle.isSelected()){
	    		((Circle)t.getSource()).setCursor(Cursor.HAND);
	    	}else if(!addToggle.isSelected()){
	    		((Circle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
	    	}
	        orgSceneX = t.getSceneX();
	        orgSceneY = t.getSceneY();
	        circleTranslates.get(index).setTranslateX(circle.getTranslateX());
	        circleTranslates.get(index).setTranslateY(circle.getTranslateY());
	        
	        
	        
	        //orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
	        System.out.format("orgTranslateX:%f, orgTranslateY:%f\n",circleTranslates.get(index).getTranslateX(), circleTranslates.get(index).getTranslateY());
	    }
	};
	
	EventHandler<MouseEvent> circleOnMouseMovedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent t) {
	    	if(deleteToggle.isSelected()){
	    		((Circle)t.getSource()).setCursor(Cursor.HAND);
	    	}else if(!addToggle.isSelected()){
	    		((Circle)t.getSource()).setCursor(Cursor.OPEN_HAND);
	    	}
	    }
	};
	     
	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent t) {
	    	
	        if (!addToggle.isSelected() && !groupToggle.isSelected()) {
	        	Circle source = (Circle) t.getSource();
				double offsetX = t.getSceneX() - orgSceneX;
				double offsetY = t.getSceneY() - orgSceneY;
				
				if (groupedCircles.contains(source) && groupedCircles.size() > 0) {
					for (Circle circle : groupedCircles) {
			        	int index = curProj.getDancerIndex(circle);
						double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
						double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
						circle.setTranslateX(newTranslateX);
						circle.setTranslateY(newTranslateY);
						groupedPaths.get(index).setTranslateX(newTranslateX);
						groupedPaths.get(index).setTranslateY(newTranslateY);
					}
				} else {
		        	int index = curProj.getDancerIndex(source);
					double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
					double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
					source.setTranslateX(newTranslateX);
					source.setTranslateY(newTranslateY);
					groupedPaths.get(index).setTranslateX(newTranslateX);
					groupedPaths.get(index).setTranslateY(newTranslateY);
				} 
			}
	    }
	};
	
	EventHandler<MouseEvent> circleOnMouseReleaseEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent t) {
	    	Circle source = (Circle) t.getSource();
        	
	    	if(deleteToggle.isSelected()){
	    		source.setCursor(Cursor.DEFAULT);
	    	}else{
	    		source.setCursor(Cursor.OPEN_HAND);
	    	}
	    	if (groupedCircles.contains(source) && groupedCircles.size() > 0) {
				for (Circle circle : groupedCircles) {
					int index = curProj.getDancerIndex(circle);
					circleTranslates.get(index).setTranslateX(circle.getTranslateX());
					circleTranslates.get(index).setTranslateY(circle.getTranslateY());
				}
			} else {
				int index = curProj.getDancerIndex(source);
				circleTranslates.get(index).setTranslateX(source.getTranslateX());
				circleTranslates.get(index).setTranslateY(source.getTranslateY());
			} 
			
	    }
	};
	
	private void initMediaPlayer(){
		mediaPlayer.currentTimeProperty().addListener((Observable ov) -> {
			updateValues();
		});

		mediaPlayer.setOnReady(() -> {
			duration = mediaPlayer.getMedia().getDuration();
			updateValues();
		});
		
		slider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (slider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					if (duration != null) {
						mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
					}
					updateValues();
				}
			}
		});

		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue observable, Duration oldValue, Duration newValue) {
				updateValues();
			}
		});
	}
	
	public void updateValues() {
		if (time != null) {
			Status status = mediaPlayer.getStatus();
			Duration currentTime = mediaPlayer.getCurrentTime();
			System.out.println(currentTime.toString());
			if(currentTime.equals(duration) && status==Status.PLAYING){
				mediaPlayer.pause();
				currentTime = mediaPlayer.getCurrentTime();
			}
			time.setText(formatTime(currentTime));
			slider.setDisable(duration.isUnknown());
			if (!slider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slider.isValueChanging()) {
				slider.setValue((currentTime.toMillis()/duration.toMillis()) * 100.0);
			}
		}
	}
	
	private static String formatTime(Duration elapsed) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed- elapsedMinutes * 60;
		return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
	}
	
	@FXML
	public void loadDefaultMusic(){
		if(mediaPlayer!=null){
			mediaPlayer.stop();
		}
		media= new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		initMediaPlayer();
	}

	@FXML
	public void loadMusic(){
		if(mediaPlayer!=null){
			mediaPlayer.stop();
		}
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3"));
		File file = fc.showOpenDialog(null);
		String fcPath = file.getAbsolutePath();
		fcPath = fcPath.replace("\\", "/");
		media = new Media(new File(fcPath).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView=new MediaView(mediaPlayer);
		initMediaPlayer();
	}
	
	@FXML
	public void playMusic(){
		//updateValues();
		Status status = mediaPlayer.getStatus();
		System.out.println(status.toString());
		mediaPlayer.setRate(1);
		mediaPlayer.play();
		
		System.out.format("%s %s\n",circles.get(0).centerXProperty().toString(), circles.get(0).centerYProperty().toString());
		
		KeyValue xValue  = new KeyValue(circles.get(0).centerXProperty(), 100); 
		KeyValue yValue  = new KeyValue(circles.get(0).centerXProperty(), 100);

		KeyFrame keyFrame  = new KeyFrame(Duration.millis(1000), xValue, yValue);
		timeline.getKeyFrames().add(keyFrame);
		
		timeline.play();
	}
	
	@FXML
	public void pauseMusic(){
		//updateValues();
		Status status = mediaPlayer.getStatus();

		if (!(status == Status.PAUSED	|| status == Status.READY || status == Status.STOPPED)) {
			mediaPlayer.pause();
		}
	}
	
	@FXML
	public void stopMusic(){
		//updateValues();
		Status status = mediaPlayer.getStatus();
		System.out.println(status.toString());
		mediaPlayer.stop();
	}
	
	@FXML
	public void setToBeginMusic(){
		//updateValues();
		Status status = mediaPlayer.getStatus();
		mediaPlayer.stop();
	}
	
	@FXML
	public void forwardMusic(){
		//updateValues();
		Status status = mediaPlayer.getStatus();
		mediaPlayer.setRate(2);
	}
	
}
*/
