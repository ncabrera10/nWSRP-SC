package evaluators;

import java.io.PrintWriter;
import java.util.ArrayList;

import dataStructures.Arc;
import dataStructures.CustomerNode;
import dataStructures.DataHandler;
import dataStructures.GraphManager;
import dataStructures.Solution;

public class FOEvaluator {
	
	public FOEvaluator() {
		
	}
	
	public double evaluateOF(Solution sol, DataHandler data) {
		
		//Initialize the objective function:
		
		double fo = 0;
		
		//Iterate through the solution:
		
		int numberOfArcs = sol.getHeads().size(); 
		for(int i=0;i<numberOfArcs;i++) {
			
			int tail = sol.getTails().get(i)-1;
			int head = sol.getHeads().get(i)-1;
			if(head == GraphManager.sourceDepotNode.id) {
				head = GraphManager.endDepotNode.id;
			}
			int type = sol.getTypes().get(i);
			Arc arc = null;
			if(type == 1) {
				arc = GraphManager.arcs_g2.get(tail+"-"+head);
			}else {
				arc = GraphManager.arcs_g2.get(tail+"_"+head);
			}
			
			if(type == 1) {
				fo += arc.getDistance();
			}
			
		}
		
		// Outsourced nodes:
		
		for(Integer node:sol.getOutsourcedNodes()) {
			fo += GraphManager.customerNodes.get(node-1).outsource;
		}
					
		//Return the fo:
		
		return(fo);
	}
	
	public double evaluateObjectiveFunctionRoute(int routeID, Solution sol, DataHandler data) {
		
		//Initialize the objective function:
		
			double fo = 0;
			
			//Iterate through the solution:
			
			int numberOfArcs = sol.getHeads().size(); 
			for(int i=0;i<numberOfArcs;i++) {
				
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
				
				if(type == 1 && route == routeID) {
					fo += arc.getDistance();
				}
				
			}
			
			
			//Return the fo:
			
			return(fo);
	
	}
	
	public void evaluateAttributes(CustomerNode node, Solution sol, DataHandler data,PrintWriter pw) {
		
		//Check the constraints:
		
		if(sol.getRoutes_visiting_customer().containsKey(node.id)) {
			pw.println((node.id+1)+" - "+sol.getRoutes_visiting_customer().get(node.id).toString()+" - "+sol.getOutsourcedNodes().contains(node.id)+" - "+node.skillsToString()+" || "+sol.getSkillsByRouteString(sol.getRoutes_visiting_customer().get(node.id).get(0)));
			
		}else {
			pw.println((node.id+1)+" - "+"___"+" - "+sol.getOutsourcedNodes().contains(node.id)+" - "+node.skillsToString());
			
		}
		
	}
	
	
	public void evaluateAttributes(int routeID,Solution sol, DataHandler data,PrintWriter pw) {
		
		//Initialize the important values:
		
				try {
					Double duration=0.0,drivingTime=0.0,drivingDistance=0.0,walkingTime=0.0,walkingDistance=0.0,serviceTime = 0.0,load = 0.0;
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
					
					pw.println("Route "+routeID+":"+duration+" - "+drivingDistance+" - "+drivingTime+" - "+walkingDistance+" - "+walkingTime+" - "+serviceTime+" - "+load+" - "+sol.getSkillsByRouteString(routeID));
					
				}catch(Exception e) {
					pw.println("Route "+routeID+" is using an arc that does not comply with the problem constraints");
				}
				
				
		
	}
}
