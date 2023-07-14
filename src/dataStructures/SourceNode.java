package dataStructures;

import java.util.ArrayList;

public class SourceNode extends Node{

	public int id;
	
	/**
	 * This method creates a new customer node
	 * @param i id
	 */
	public SourceNode(int i) {
		
		id = i;

		firstTime = true;
		
		// Arcs:
		
			forwardStar = new ArrayList<Arc>();
			forwardStar_drive = new ArrayList<Arc>();
			forwardStar_walk = new ArrayList<Arc>();
			firstTime_drive = true;
			firstTime_walk = true;
			firstTime = true;
	
	}
	
	public String toString() {
		return "SourceNode: "+id;
	}

	
}
