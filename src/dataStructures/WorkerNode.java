package dataStructures;

import java.util.ArrayList;

public class WorkerNode extends Node{

	public int[][] skills_available;
	
	public String skills_key;
	
	public int profile_id;
	
	/**
	 * This method creates a new worker node
	 * @param i
	 * @param skills_req
	 */
	public WorkerNode(int i, int[][]skills_req, String key,int profile) {
		
		id = i;
		skills_available = skills_req.clone();
		skills_key = key;
		profile_id = profile;
		
		// Arcs:
		
		forwardStar = new ArrayList<Arc>();
		forwardStar_drive = new ArrayList<Arc>();
		forwardStar_walk = new ArrayList<Arc>();
		firstTime_drive = true;
		firstTime_walk = true;
		firstTime = true;

	}
	
	public String toString() {
		return "Worker: "+id+" has the skills: "+skills_key+" - "+profile_id;
	}

	
}
