/*
package view;

import java.util.ArrayList;

import control.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Proj;
import model.Dancer;

public class WorkViewController {
	
	public final static int CIRCLE_RADIUS=10;
	public final static int SELECT_RADIUS=12;
	public final static int LINE_WIDTH=2;
	public final static int ERASE_WIDTH=5;
	public final static int CANVAS_WIDTH=600;
	public final static int CANVAS_HEIGHT=200;
	
	@FXML
	private Canvas canvas;
	
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
	
	private int maxNum;
	private int currentDancersDrawn;
	
	private int lastClickedDancerIndex;
	
	private ArrayList<Dancer> dancers = new ArrayList<Dancer>();
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	
	private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Arc selectArc;
	
	private Stage dialogStage;
	private MainApp mainApp;
	
	@FXML
	private void initialize(){
		
	}
	
	public WorkViewController(){
		
	}
	
	public void setProj(Proj _proj){
		this.curProj = _proj;
		
		// Dancer Init
		maxNum = curProj.getNumOfDancers();
		currentDancersDrawn = curProj.getDrawnDancers();
		
		// Canvas Init
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Proj.WHITE);
		gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		
	}
	
	public void setMainApp(MainApp _mainApp){
		this.mainApp = _mainApp;
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	@FXML
	private void drawCircle(MouseEvent e) {
		GraphicsContext gc = canvas.getGraphicsContext2D();      
        //System.out.println(e.getSource());
        //System.out.println(e.getTarget());
        //System.out.println(e.getEventType());
        System.out.format("x:%f, y:%f%n", e.getSceneX(), e.getSceneY());
        System.out.format("x:%f, y:%f%n", e.getScreenX(), e.getScreenY());
        
        double x=e.getSceneX();
		double y=e.getSceneY()-220;
        if(addToggle.isSelected()){
        	if(currentDancersDrawn<maxNum){
        		Circle circle = new Circle(x,y,CIRCLE_RADIUS, Proj.colors[currentDancersDrawn]);
        		circle.setCursor(Cursor.HAND);
                circle.setOnMousePressed(circleOnMousePressedEventHandler);
                circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        		drawPane.getChildren().add(circle);
        		/*
        		gc.setFill(Proj.colors[currentDancersDrawn]);
        		gc.fillOval(x, y, 2*CIRCLE_RADIUS, 2*CIRCLE_RADIUS);
        		
        		dancers.add(new Dancer(currentDancersDrawn, x, y));
        		circles.add(circle);
        		currentDancersDrawn++;
        		System.out.println(dancers.size());
        	}
        }else{
        	int clickedDancerIndex = getClickedDancer(x,y);
        	// erase last one
        	if(lastClickedDancerIndex !=-1){
        		gc.setLineWidth(ERASE_WIDTH);
        		gc.setStroke(Proj.WHITE);
        		gc.strokeOval(dancers.get(lastClickedDancerIndex).x-LINE_WIDTH, dancers.get(lastClickedDancerIndex).y-LINE_WIDTH,
        				2*SELECT_RADIUS, 2*SELECT_RADIUS);
        	}
        	if(clickedDancerIndex != -1){
        		System.out.println(clickedDancerIndex);
        		// draw selected one
        		/*
        		gc.setLineWidth(LINE_WIDTH);
        		gc.setStroke(Proj.BLACK);
        		gc.strokeOval(dancers.get(clickedDancerIndex).x-LINE_WIDTH, dancers.get(clickedDancerIndex).y-LINE_WIDTH,
        				2*SELECT_RADIUS, 2*SELECT_RADIUS);
        				
        	}
        	lastClickedDancerIndex = clickedDancerIndex;
        }
        
    }
	
	private int getClickedDancer(double mouseX, double mouseY){
		int index=-1;
		double minDist=1000000;
		for(Dancer dancer:dancers){
			double dist=(dancer.x-mouseX)*(dancer.x-mouseX) + (dancer.y-mouseY)*(dancer.y-mouseY);
			if(dist < CIRCLE_RADIUS*CIRCLE_RADIUS && dist<minDist){
				index=dancer.index;
			}
		}
		return index;
	}
	EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
	        new EventHandler<MouseEvent>() {
	 
	        @Override
	        public void handle(MouseEvent t) {
	            orgSceneX = t.getSceneX();
	            orgSceneY = t.getSceneY();
	            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
	            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
        		
	        }
	    };
	     
	    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = 
	        new EventHandler<MouseEvent>() {
	 
	        @Override
	        public void handle(MouseEvent t) {
	            double offsetX = t.getSceneX() - orgSceneX;
	            double offsetY = t.getSceneY() - orgSceneY;
	            double newTranslateX = orgTranslateX + offsetX;
	            double newTranslateY = orgTranslateY + offsetY;
	             
	            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
	            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
	        }
	    };

	
	
}
*/