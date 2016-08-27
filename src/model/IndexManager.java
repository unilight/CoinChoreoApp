package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class IndexManager {
	
	private List<Integer> indexPool;
	private final int numOfDancer; 
	
	public IndexManager(int _numOfDancer) {
		numOfDancer = _numOfDancer;
		indexPool = new ArrayList<Integer>(numOfDancer);
		for(int i=0; i<numOfDancer; i++){
			indexPool.add(i);
		}
	}
	
	public int deque(){
		int ans=indexPool.remove(0);
		return ans;
	}
	
	public void enque(int _index){
		indexPool.add(_index);
		Collections.sort(indexPool);
	}
	
	public String toString(){
		return indexPool.toString();
	}
	
	public static void main(String[] args) {
		IndexManager a = new IndexManager(5);
		System.out.println(a);
		System.out.println(a.deque());
		System.out.println(a);
		a.enque(8);
		a.enque(10);
		a.enque(9);
		System.out.println(a);
		a.deque();
		a.deque();
		a.enque(11);
		System.out.println(a);
	}
}
