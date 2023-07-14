package dataStructures;

import java.util.ArrayList;

public class CustomerNode extends Node{

	
	/**
	 * This method creates a new customer node
	 * @param i id
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param d demand
	 * @param e early arrival time
	 * @param l late arrival time
	 * @param s service time
	 * @param o outsourcing cost
	 * @param skills_req skills requirements matrix
	 */
	public CustomerNode(int i, int x, int y,int d, int e,int l, int s,double o,int[][] skills_req) {
		
		id = i;
		print_id = i;
		coor_x = x;
		coor_y = y;
		demand = d;
		tw_a = e;
		tw_b = l;
		service = s;
		outsource = o;
		skills_requirements = skills_req.clone();

		// Arcs:
		
		forwardStar = new ArrayList<Arc>();
		forwardStar_drive = new ArrayList<Arc>();
		forwardStar_walk = new ArrayList<Arc>();
		firstTime_drive = true;
		firstTime_walk = true;
		firstTime = true;

	}

	
	
	// Auxiliary methods:
	
	@Override
	public String toString() {
		return "Customer: "+id+" located at: ("+coor_x+";"+coor_y+")"+" may be visited by: ("+tw_a+";"+tw_b+")";
	}
	
	
}
