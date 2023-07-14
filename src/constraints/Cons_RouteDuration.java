package constraints;

import java.io.PrintWriter;
import java.util.ArrayList;

import dataStructures.Arc;
import dataStructures.DataHandler;
import dataStructures.GraphManager;
import dataStructures.Solution;

public class Cons_RouteDuration {

	public Cons_RouteDuration() {
		
	}
	
	/**
	 * This method checks the constraint for all routes:
	 * @param solution
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraint(Solution solution, DataHandler data, boolean output, int precision,PrintWriter pw) {
		for (Integer route : solution.getRoutesUnique()) {
			if (!checkConstraint(route,solution, data, output, precision,pw)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method checks a route
	 * @param route
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraint(int routeID,Solution sol, DataHandler data,boolean output, int precision,PrintWriter pw) {

		//Initialize the important values:
		
		Double duration=0.0,drivingTime=0.0,drivingDistance=0.0,walkingTime=0.0,walkingDistance=0.0,serviceTime = 0.0;
		ArrayList<Integer> nodesInRoute = new ArrayList<Integer>();
		

		//Iterate through the lists:
		
		int numArcs = sol.getHeads().size();
		boolean parked = false;
		int parking_spot = -1;
		for(int i = 0;i<numArcs;i++) {
			
			int tail = sol.getTails().get(i)-1;
			int head = sol.getHeads().get(i)-1;
			if(head == GraphManager.sourceDepotNode.id) {
				head = GraphManager.endDepotNode.id;
			}
			int type = sol.getTypes().get(i);
			int route = sol.getRoutes().get(i);
			Arc arc = null;
			if(type == 1) {
				arc = GraphManager.arcs_g2.get(tail+"-"+head);
			}else {
				arc = GraphManager.arcs_g2.get(tail+"_"+head);
			}
			
			if(routeID == route) {
				
				if(!nodesInRoute.contains(tail)) {
					nodesInRoute.add(tail);
				}
				if(type == 1) {
					
					drivingTime += arc.getTime();
					drivingDistance += arc.getDistance();
					if(parking_spot == -1) {
						if(tail == GraphManager.sourceDepotNode.id) {
							
							duration = Math.max(arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							
						}else if(head == GraphManager.endDepotNode.id) {
							duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.endDepotNode.tw_a);
							serviceTime += GraphManager.customerNodes.get(tail-1).service;
							
						}
						else {
							duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							serviceTime += GraphManager.customerNodes.get(tail-1).service;
							
						}
					}else {
						if(tail == GraphManager.sourceDepotNode.id) {
							
							duration = Math.max(arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							
						}else if(head == GraphManager.endDepotNode.id) {
							duration = Math.max(arc.getTime() + duration,GraphManager.endDepotNode.tw_a);
							
						}
						else {
							duration = Math.max(arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							
						}
						parking_spot = -1;
					}
					
					
					
				}else {
					
					if(parked) {
						walkingTime += arc.getTime();
						walkingDistance += arc.getDistance();
						if(head == parking_spot) {
							duration = GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration;
							serviceTime += GraphManager.customerNodes.get(tail-1).service;
							parked = false;
							parking_spot = -2;
						}else {
							if(tail == GraphManager.sourceDepotNode.id) {
								
								duration = Math.max(arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
								
							}else if(head == GraphManager.endDepotNode.id) {
								duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.endDepotNode.tw_a);
								serviceTime += GraphManager.customerNodes.get(tail-1).service;
								
							}
							else {
								duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
								serviceTime += GraphManager.customerNodes.get(tail-1).service;
								
							}
						}
						
						
					}else {
						walkingTime += arc.getTime();
						walkingDistance += arc.getDistance();
						if(tail == GraphManager.sourceDepotNode.id) {
							
							duration = Math.max(arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							
						}else if(head == GraphManager.endDepotNode.id) {
							duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.endDepotNode.tw_a);
							serviceTime += GraphManager.customerNodes.get(tail-1).service;
							
						}
						else {
							duration = Math.max(GraphManager.customerNodes.get(tail-1).service + arc.getTime() + duration,GraphManager.customerNodes.get(head-1).tw_a);
							serviceTime += GraphManager.customerNodes.get(tail-1).service;
							
						}
						parked = true;
						parking_spot = tail;
					}
					
				}
				 
			}
			
			
		}
		
		//Check the constraints:
		
		if(duration - Math.pow(10, -precision) > DataHandler.route_duration_limit) {
			pw.println("Route "+routeID+" has a duration "+duration+" > "+DataHandler.route_duration_limit);
			return false;
		}
		
		//If nothing happened:
		
			return true;
	}
}
