package model;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Model class for a Project.
 *
 */

public class Proj {
	
	    private final int numOfDancers;
	    private int currentDancersDrawn;
	    private ArrayList<Dancer> dancers;

	    public static final Color[] colors = {Color.FORESTGREEN,
	    									Color.ORANGE,
	    									Color.AQUA,
	    									Color.CORAL,
	    									Color.MEDIUMPURPLE,
	    									Color.YELLOW,
	    									Color.AQUAMARINE
	    									};
	    public static final Color BLACK = Color.BLACK;
	    public static final Color WHITE = Color.WHITE;

	    /**
	     * Default constructor.
	     */
	    public Proj() {
	        this(0);
	    }
	    
	    /**
	     * Constructor with number of dancers
	     * 
	     * @param num
	     */
	    public Proj(int num){
	    	this.numOfDancers=num;
	    	this.currentDancersDrawn=0;
	    	dancers = new ArrayList<Dancer>(num);
	    }

		public int getNumOfDancers() {
			return numOfDancers;
		}
		
		public int getCurrentDancersDrawn() {
			return currentDancersDrawn;
		}

		public void setCurrentDancersDrawn(int drawnDancers) {
			this.currentDancersDrawn = drawnDancers;
		}
		
		public void addDancer(Dancer dancer) {
			this.dancers.add(dancer);
			this.currentDancersDrawn++;
		}
		
		public void removeDancer(Circle circle) {
			Dancer _dancer = new Dancer();
			for(Dancer dancer:dancers){
				if(dancer.getCircle().equals(circle)){
					_dancer = dancer;
					break;
				}
			}
			this.dancers.remove(_dancer);
			this.currentDancersDrawn--;
		}
		

	    /**
		 * @return the dancers
		 */
		public ArrayList<Dancer> getDancers() {
			return dancers;
		}

		/**
		 * @param dancers the dancers to set
		 */
		public void setDancers(ArrayList<Dancer> dancers) {
			this.dancers = dancers;
		}
	    
		public int getDancerIndex(Circle circle){
			Dancer _dancer = new Dancer();
			for(Dancer dancer:dancers){
				if(dancer.getCircle().equals(circle)){
					_dancer = dancer;
					break;
				}
			}
			return _dancer.getIndex();
		}
	    
}
