package dataStructures;

public class EndDepot extends Node{
	
	
	/**
	 * This method creates a new end depot node
	 * @param i id
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param e early arrival time
	 * @param l late arrival time
	 */
	public EndDepot(int i, int x, int y, int e,int l) {
		
		id = i;
		print_id = i;
		coor_x = x;
		coor_y = y;
		tw_a = e;
		tw_b = l;
		
	}
	
	
	public String toString() {
		return "EndDepot: "+id+" located at: ("+coor_x+";"+coor_y+")"+" may be visited by: ("+tw_a+";"+tw_b+")";
	}

	
}
