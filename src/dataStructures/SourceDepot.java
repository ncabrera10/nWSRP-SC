package dataStructures;

import java.util.ArrayList;

public class SourceDepot extends Node{

	
	/**
	 * This method creates a new source depot node
	 * @param i id
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param e early arrival time
	 * @param l late arrival time
	 */
	public SourceDepot(int i, int x, int y, int e,int l) {
		
		id = i;
		print_id = i;
		coor_x = x;
		coor_y = y;
		tw_a = e;
		tw_b = l;
		
		// Arcs:
		
		forwardStar = new ArrayList<Arc>();
		forwardStar_drive = new ArrayList<Arc>();
		forwardStar_walk = new ArrayList<Arc>();
		firstTime_drive = true;
		firstTime_walk = true;
		firstTime = true;
		
		
	}
	
	public String toString() {
		return "SourceDepot: "+id+" located at: ("+coor_x+";"+coor_y+")"+" may be visited by: ("+tw_a+";"+tw_b+")";
	}

}
