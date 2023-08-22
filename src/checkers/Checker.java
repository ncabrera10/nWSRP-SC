package checkers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import dataStructures.CustomerNode;
import dataStructures.DataHandler;
import dataStructures.GraphManager;
import dataStructures.Solution;
import evaluators.ConstraintsEvaluator;
import evaluators.FOEvaluator;
import parameters.GlobalParameters;

public class Checker {

	public Checker(String solutionFileName) throws IOException {
		
		//Captures the current instance number:
		
		String solutionFileNameM = solutionFileName.replace(".txt", "");
	
		String[] parts = solutionFileNameM.split("_");
		String algorithm = parts[0];
		int num_tasks = Integer.parseInt(parts[1]);
		int num_techs = Integer.parseInt(parts[2]);
		String compOrRed = parts[3];
		int maxPerTeam = Integer.parseInt(parts[4]);
		String instance = parts[5];
		String instance_type = "";
		int instance_number =  -1;
		if(instance.length() == 5) {
			instance_type = instance.substring(0,2);
			instance_number = Integer.parseInt(instance.substring(2,4));
		}else {
			instance_type = instance.substring(0,1);
			instance_number =Integer.parseInt(instance.substring(1,3));
		}
		
		String skills = parts[6];
		String[] parts_aux = skills.split("x");
		int number_skills = Integer.parseInt(parts_aux[0]);
		int number_levels = Integer.parseInt(parts_aux[1]);
		String version = parts[7];
		
		String dataFile = instance+"_"+skills+"_"+version+".txt";
	
		
		//1. Read the data:
		
			DataHandler data = this.readDataInfo(dataFile,instance_type,instance_number,number_skills,number_levels,version,num_tasks,num_techs,compOrRed,maxPerTeam);

		//3. Reads the solution:
			
			Solution solution = this.readSolution(solutionFileName,dataFile,instance_type,instance_number,number_skills,number_levels,version,maxPerTeam);
		
		//4. Prints the report:
			
			printReport(algorithm,instance,skills,version,solution,data,maxPerTeam);
			
	}	
	
	
	public void printReport(String algorithm,String instance,String skills, String version,Solution solution,DataHandler data, int maxPerTeam) {
		
		
		//Creates a path to print the report:
		
			String ruta = "";
			String ruta_details = "";
			
			ruta = GlobalParameters.RESULT_FOLDER+"Report_"+algorithm+"_"+instance+"_"+skills+"_"+version+"_"+maxPerTeam+("_"+DataHandler.instance_numNodes+"_"+DataHandler.instance_numTechnicians+"_"+DataHandler.instance_technician)+".txt";
			ruta_details = GlobalParameters.RESULT_FOLDER+"DetailsReport_"+algorithm+"_"+instance+"_"+skills+"_"+version+"_"+maxPerTeam+("_"+DataHandler.instance_numNodes+"_"+DataHandler.instance_numTechnicians+"_"+DataHandler.instance_technician)+".txt";
			String ruta_summary = GlobalParameters.RESULT_FOLDER+"Summary_"+algorithm+"_"+instance+"_"+skills+"_"+version+"_"+maxPerTeam+("_"+DataHandler.instance_numNodes+"_"+DataHandler.instance_numTechnicians+"_"+DataHandler.instance_technician)+".txt";
			
		//Main logic:
		
			try {
				PrintWriter pw = new PrintWriter(new File(ruta));
				PrintWriter pw2 = new PrintWriter(new File(ruta_details));
				PrintWriter pw3 = new PrintWriter(new File(ruta_summary));
				
				//Headline:
				
				pw.println("Instance:"+instance);
				pw3.println("Parameter;Value");
				pw3.println("Instance;"+instance);
				pw3.println("Algorithm;"+algorithm);
				
				//Check if the solution complies with the constraints:
				
				ConstraintsEvaluator evaluator_cons = new ConstraintsEvaluator();
				boolean cons1 = evaluator_cons.evaluateRouteWalkingDistance(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons2 = evaluator_cons.evaluateRouteDuration(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons3 = evaluator_cons.evaluateStartEndDepot(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons4 = evaluator_cons.evaluateRouteCapacity(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons5 = evaluator_cons.evaluateRouteTW(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons6 = evaluator_cons.evaluateSkills(solution, data, GlobalParameters.PRECISION, pw2);
				boolean cons7 = evaluator_cons.evaluateNodeServed(solution, data, GlobalParameters.PRECISION, pw2);
				
				pw.println("----------------Constraints----------------");
				pw.println("Route walking distance limit: "+cons1);
				pw.println("Route duration constraints: "+cons2);
				pw.println("Route start-end constraints: "+cons3);
				pw.println("Route capacity: "+cons4);
				pw.println("Route time windows: "+cons5);
				pw.println("Workers skills: "+cons6);
				pw.println("Node is served by only one/outsourced: "+cons7);
				//pw.println("All customers must be visited constraints: "+cons4);
				//pw.println("Subtour duration limit: "+cons5);
				if(cons1 && cons2 && cons3 && cons4 && cons5 && cons6 && cons7) {
					pw.println("The solution is valid");
					pw3.println("Feasible;"+1);
				}else {
					System.out.println("Invalid "+algorithm+" - "+instance);
					pw.println("The solution is invalid");
					pw3.println("Feasible;"+0);
				}
				
			//Objective function for the solution:
				
				//The objective function:
				
				FOEvaluator fo_eva = new FOEvaluator();
				double objFO = fo_eva.evaluateOF(solution, data);
				pw.println("----------------ObjectiveFunction----------------");
				pw.println("ObjectiveFunction: "+objFO);
				pw3.println("ObjectiveFunction;"+objFO);
				
				//Objective function for each route:
				pw.println("----------------Routes----------------");
				pw.println("RouteID - ObjectiveFunction");
				for(Integer route : solution.getRoutesUnique()) {
					pw.println(route+" - "+fo_eva.evaluateObjectiveFunctionRoute(route, solution, data));
				}
				
				//Attributes for each route:
				pw.println("----------------Attributes----------------");
				pw.println("RouteID - ObjectiveFunction");
				for(Integer route : solution.getRoutesUnique()) {
					fo_eva.evaluateAttributes(route, solution, data,pw);
				}
				
				//Attributes for each customer:
				pw.println("----------------Customers----------------");
				pw.println("NodeID - VisitedBy - Outsourced");
				for(CustomerNode node : GraphManager.customerNodes) {
					fo_eva.evaluateAttributes(node, solution, data,pw);
				}
				
				pw.close();
				pw2.close();
				pw3.close();
				
			}
			catch(Exception e) {
				System.out.println("Problem while printing the report");
				e.printStackTrace();
			}

	}
	
	public Solution readSolution(String solutionFileName,String dataFile, String instance_type, int instance_number, int instance_skills,int instance_levels,String version, int max_per_team) throws IOException {
		
		//Creates a solution:
		
			Solution sol = new Solution(dataFile,instance_type,instance_number,instance_skills,instance_levels,version);
			
		//Reads the solution file arcs:
			
			//0. Creates a buffered reader:
			
				BufferedReader buff = new BufferedReader(new FileReader(GlobalParameters.SOLUTIONS_FOLDER+solutionFileName));
		
			//1. Reads the header
				
				String line = buff.readLine();
			
			//2. Reads the routes:
				
				while(!line.equals("---")) {
					String[] attrs = line.split(";");
					sol.getTails().add(Integer.parseInt(attrs[0]));
					sol.getHeads().add(Integer.parseInt(attrs[1]));
					sol.getTypes().add(Integer.parseInt(attrs[2]));
					if(!sol.getRoutes().contains(Integer.parseInt(attrs[3]))) {
						sol.getRoutesUnique().add(Integer.parseInt(attrs[3]));
					}
					sol.getRoutes().add(Integer.parseInt(attrs[3]));
					if(sol.getRoutes_visiting_customer().containsKey(Integer.parseInt(attrs[0])-1)) {
						if(!sol.getRoutes_visiting_customer().get(Integer.parseInt(attrs[0])-1).contains(Integer.parseInt(attrs[3]))) {
							sol.getRoutes_visiting_customer().get(Integer.parseInt(attrs[0])-1).add(Integer.parseInt(attrs[3]));
						}
					}else {
						sol.getRoutes_visiting_customer().put(Integer.parseInt(attrs[0])-1,new ArrayList<Integer>());
						sol.getRoutes_visiting_customer().get(Integer.parseInt(attrs[0])-1).add(Integer.parseInt(attrs[3]));
					}
					line = buff.readLine();
					
				}
				
				line = buff.readLine();
				
			//3. Reads the workers assigned to each route:
				
				while(!line.equals("---")) {
					String[] attrs = line.split(";");
					int profile_id = Integer.parseInt(attrs[0])-1;
					int num_w = Integer.parseInt(attrs[1]);
					int route_id = Integer.parseInt(attrs[2]);
					
					
					
					if(sol.getProfiles_ids().containsKey(route_id)) {
						sol.getProfiles_ids().get(route_id).add(profile_id);
						sol.getProfiles_number_ids().get(route_id).add(num_w);
						for(int i = 0;i<DataHandler.instance_skills;i++) {
							for(int j = 0;j<DataHandler.instance_levels;j++) {
								sol.getSkills_per_route().get(route_id)[i][j] += GraphManager.workerNodes.get(profile_id).skills_available[i][j]*num_w;
							}
						}
					}else {
						sol.getSkills_per_route().put(route_id, new int[DataHandler.instance_skills][DataHandler.instance_levels]);
						for(int i = 0;i<DataHandler.instance_skills;i++) {
							for(int j = 0;j<DataHandler.instance_levels;j++) {
								sol.getSkills_per_route().get(route_id)[i][j] += GraphManager.workerNodes.get(profile_id).skills_available[i][j]*num_w;
							}
						}
						sol.getProfiles_ids().put(route_id, new ArrayList<Integer>());
						sol.getProfiles_number_ids().put(route_id, new ArrayList<Integer>());
						sol.getProfiles_ids().get(route_id).add(profile_id);
						sol.getProfiles_number_ids().get(route_id).add(num_w);
					}
					line = buff.readLine();
					
					
				}
				
				// Read the outsourcing:
				
				line = buff.readLine();
				while(line != null) {
					String[] attrs = line.split(";");
					int node_id = Integer.parseInt(attrs[0]);
					int outsourced = Integer.parseInt(attrs[1]);
					if(outsourced == 1) {
						sol.getOutsourcedNodes().add(node_id-1);
					}
					line = buff.readLine();
				}
				
		
			//4. Closes the buffered reader
			
				buff.close();
			
		//Returns the solution:
			
			return(sol);
	}
	
	
	/**
	 * Creates the data handler
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public DataHandler readDataInfo(String dataFile, String instance_type, int instance_number, int instance_skills,int instance_levels,String version, int num_tasks, int num_techs,String instance_technician, int max_per_team) throws IOException {
		
		//1.0 Creates a data handler
		
		DataHandler data = new DataHandler(dataFile,instance_type,instance_number,instance_skills,instance_levels,version, num_tasks,num_techs,instance_technician,max_per_team);
		
		//1.4 Returns the data handler
		
		return(data);
	}
	
}
