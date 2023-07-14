package constraints;

import java.io.PrintWriter;
import dataStructures.DataHandler;
import dataStructures.GraphManager;
import dataStructures.Solution;

public class Cons_Skills {

	public Cons_Skills() {
		
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

		//Iterate through the lists:
		
		int numArcs = sol.getHeads().size();
	
		for(int i = 0;i<numArcs;i++) {
			
			int tail = sol.getTails().get(i)-1;
			int head = sol.getHeads().get(i)-1;
			int route = sol.getRoutes().get(i);
			
			if(routeID == route) {
				
				if(tail != GraphManager.sourceDepotNode.id) {
					for(int k = 0;k<DataHandler.instance_skills;k++) {
						for(int j = 0;j<DataHandler.instance_levels;j++) {
							if(sol.getSkills_per_route().get(route)[k][j] < GraphManager.customerNodes.get(tail-1).skills_requirements[k][j]) {
								 return false;
							}
						}
					}
				}
				
				if(head != GraphManager.sourceDepotNode.id) {
					for(int k = 0;k<DataHandler.instance_skills;k++) {
						for(int j = 0;j<DataHandler.instance_levels;j++) {
							//System.out.println("Evaluating :"+head+" - "+k+" - "+j+" - "+sol.getSkills_per_route().get(route)[k][j]+" - "+GraphManager.customerNodes.get(head-1).skills_requirements[k][j]);
							if(sol.getSkills_per_route().get(route)[k][j] < GraphManager.customerNodes.get(head-1).skills_requirements[k][j]) {
								 return false;
							}
						}
					}
				}
				
				 
			}
			
			
		}
		
		//If nothing happened:
		
			return true;
	}
}
