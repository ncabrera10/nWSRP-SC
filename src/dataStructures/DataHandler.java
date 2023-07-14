package dataStructures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import parameters.GlobalParameters;

public class DataHandler {

	// Instance information:
	
	/**
	 * Instance data file
	 */
	
	public static String instance_dataFile;
	
	/**
	 * Instance type: C, RC, R
	 */
	
	public String instance_type;
	
	/**
	 * Instance number: 101, 103, 201, 203
	 */
	
	public int instance_number;
	
	/**
	 * Instance technicians version: Complete and Reduced
	 */
	
	public static String instance_technician;
	
	/**
	 * Instance version: Team or NoTeam
	 */
	
	public String instance_version;
	
	/**
	 * Instance number of skills: 5,6,7
	 */
	
	public static int instance_skills;
	
	/**
	 * Instance number of levels: 4,6,4
	 */
	
	public static int instance_levels;
	

	/**
	 * Instance number of nodes
	 */
	
	public static int instance_numNodes;
	
	/**
	 * Instance number of technicians
	 */
	public static int instance_numTechnicians;
	
	/**
	 * Maximum number of workers per team
	 */
	public static int instance_max_per_team;
	
	// Constants for the pulse algorithm:
	
	/**
	 * Threads
	 */
	public static Thread[] threads;
	
	/**
	 * Number of threads
	 */
	
	public static int number_of_threads;
	
	/**
	 * Bound step
	 */
	
	public static int bound_step;
	
	/**
	 * Minimum time limit for the bounding procedure:
	 */
	
	public static int bound_lower_time_pulse;
	
	// Important information about the instance:
	
	/**
	 * Maximum duration for each route
	 */
	public static double route_duration_limit;
	
	/**
	 * Capacity of each route in terms of load
	 */
	public static double route_capacity;
	
	/**
	 * Maximum walking distance for each route
	 */
	public static double route_walking_distance_limit;

	/**
	 * Maximum duration for each walking subtour
	 */
	public static double subtour_duration_limit;
	
	/**
	 * Maximum walking distance between two points
	 */
	public static double max_walking_distance_bt2P;
	
	/**
	 * Walking speed 
	 */
	public static double walking_speed;
	
	/**
	 * Driving speed
	 */
	public static double driving_speed;
	
	/**
	 * Fixed cost
	 */
	public static double fixed_cost;
	
	/**
	 * Variable cost
	 */
	public static double variable_cost;
	
	/**
	 * Number of workers
	 */
	
	public static int number_of_workers;
	
	/**
	 * This method creates a DataHandler instance
	 * @param dataFile
	 * @param ty
	 * @param instanceType
	 * @param instanceNumber
	 * @param instanceSkills
	 * @param instanceLevels
	 * @param instanceVersion
	 * @throws IOException 
	 */
	public DataHandler(String dataFile, String instanceType,int instanceNumber, int instanceSkills, int instanceLevels, String instanceVersion, int numTasks, int numTechs, String instTech, int max_per_team) throws IOException {
		
		// Initialize the main variables:
		
			instance_type = instanceType;
			instance_number = instanceNumber;
			instance_skills = instanceSkills;
			instance_levels = instanceLevels;
			instance_version = instanceVersion;
			instance_dataFile = dataFile;
			instance_numNodes = numTasks;
			instance_numTechnicians = numTechs;
			instance_technician = instTech;
			instance_max_per_team = max_per_team;
			
		// Creates the graph manager:
			
			new GraphManager();
			
		// Read the tasks information:
			
			readTasks();
			
		// Read the workers information:
			
			readWorkers();
				
		// Initialize main additional constraints:
			
			readMainConstraints();
			
		// Initialize the graph manager:
			
			GraphManager.initializeGraphManager();
		
		// Create the graph:
			
			createArcs();
	}
	
	/**
	 * This method creates a DataHandler instance
	 * @param dataFile
	 * @param ty
	 * @param instanceType
	 * @param instanceNumber
	 * @param instanceSkills
	 * @param instanceLevels
	 * @param instanceVersion
	 * @throws IOException 
	 */
	public DataHandler(String dataFile, String instanceType,int instanceNumber, int instanceNumNodes) throws IOException {
		
		// Initialize the main variables:
		
			instance_type = instanceType;
			instance_number = instanceNumber;
			instance_skills = 0;
			instance_levels = 0;
			instance_version = "NoTeam";
			instance_numNodes = instanceNumNodes;
			instance_dataFile = dataFile;

		// Creates the graph manager:
			
			new GraphManager();
			
		// Read the tasks information:
			
			readTasks_Solomon();
			
		// Read the workers information:
			
			readWorkers_Solomon();
				
		// Initialize main additional constraints:
			
			readMainConstraints_Solomon();
			
		// Initialize the graph manager:
			
			GraphManager.initializeGraphManager();
		
		// Create the graph:
			
			createArcs();
			GraphManager.sourceDepotNode.print_id = GraphManager.endDepotNode.id;
	}
	
	/**
	 * This method reads the information of the tasks
	 * @throws IOException 
	 */
	public void readTasks() throws IOException {
		
		// Create the path to the instance file:
		
			String path_to_file = GlobalParameters.INSTANCE_FOLDER;
		
			if(instance_version.equals("Team")) {
				path_to_file += "tasks_Team/";
			}else {
				path_to_file += "tasks_noTeam/";
			}
		
			path_to_file += instance_dataFile;
			
		// Create a buffered reader:
			
			BufferedReader buff = new BufferedReader(new FileReader(path_to_file));
		
		// 1. Reads the first two lines
			
			String capacity_line = buff.readLine(); // This line can be ignored: as 
			String customer_line = buff.readLine();
			
		// 2. Capture the number of nodes and the capacity:
			
			int n = Math.min(instance_numNodes+1,Integer.parseInt(customer_line.split(": ")[1]));
			int Q = Integer.parseInt(capacity_line.split(": ")[1]);
			
			Q = Integer.MAX_VALUE;
			route_capacity = Q;
			
		// 3. Skip two lines:
			
			buff.readLine();
			buff.readLine();
			
		// 4. Read the remaining lines: information for each task/customer:
			
			// Read the customers and the depot:
			
			String line = buff.readLine();
			while(line != null) {
				String[] parts = line.split("\t");
				int id = Integer.parseInt(parts[0]);
				int coor_x = Integer.parseInt(parts[1]);
				int coor_y = Integer.parseInt(parts[2]);
				int demand = Integer.parseInt(parts[3]);
				int early = Integer.parseInt(parts[4]);
				int late = Integer.parseInt(parts[5]);
				int service = Integer.parseInt(parts[6]);
				double out_source_cost = Integer.parseInt(parts[7]);
				int[][] skills_requirements = new int[instance_skills][instance_levels];
				
				int act = 8;
				int aux_outsource = 0;
				for(int i = 0;i<instance_skills;i++) {
					for(int j=0;j<instance_levels;j++) {
						int num_level_skill = Integer.parseInt(parts[act]);
						skills_requirements[i][j] = num_level_skill;
						aux_outsource += num_level_skill;
						act++;
					}
					
				}
				
				// Calculate the outsourcing cost:
				
				out_source_cost = 200 + Math.pow(aux_outsource, 1.5);
				
				if (id == 1) { // This means that we are reading the information of the depot:
					
					SourceDepot node1 = new SourceDepot(id-1,coor_x,coor_y,early,late);
					EndDepot node2 = new EndDepot(n,coor_x,coor_y,early,late);
					
					GraphManager.sourceDepotNode = node1;
					GraphManager.endDepotNode = node2;
					
					route_duration_limit = node1.tw_b;
					
					
				}else if(GraphManager.customerNodes.size() < instance_numNodes){
					CustomerNode node = new CustomerNode(id-1,coor_x,coor_y,demand,early,late,service,out_source_cost,skills_requirements);
					GraphManager.customerNodes.add(node);
				}
				
				line = buff.readLine();
			}
			
		// Close the buffered reader:
			
			buff.close();
	}
	
	/**
	 * This method reads the information of the tasks for the VRPTW
	 * @throws IOException 
	 */
	public void readTasks_Solomon() throws IOException {
		
		// Create the path to the instance file:
		
			String path_to_file = GlobalParameters.INSTANCE_FOLDER;
	
			path_to_file += instance_dataFile;
			
		// 1. Create a buffered reader:
			
			BufferedReader buff = new BufferedReader(new FileReader(path_to_file));
		
		// 2. Capture the number of nodes and the capacity:
			
			int n = instance_numNodes+1;
			readCapacity();
			
		// 3. Read the remaining lines: information for each task/customer:
			
			// Read the customers and the depot:
			
			String line = buff.readLine();
			while(line != null && GraphManager.customerNodes.size() < n-1) {
				StringTokenizer t = new StringTokenizer(line, " ");
				String[] parts = new String[7];
				int indexString = 0;
				while (t.hasMoreTokens()) {
					parts[indexString] = t.nextToken();
					indexString++;
				}
				
				int id = (int) Double.parseDouble(parts[0]);
				int coor_x = (int) Double.parseDouble(parts[1]);
				int coor_y = (int) Double.parseDouble(parts[2]);
				int demand = (int) Double.parseDouble(parts[3]);
				int early = (int) Double.parseDouble(parts[4]);
				int late = (int) Double.parseDouble(parts[5]);
				int service = (int) Double.parseDouble(parts[6]);
				
				if (id == 1) { // This means that we are reading the information of the depot:
					
					SourceDepot node1 = new SourceDepot(id-1,coor_x,coor_y,early,late);
					EndDepot node2 = new EndDepot(n,coor_x,coor_y,early,late);
					
					GraphManager.sourceDepotNode = node1;
					GraphManager.endDepotNode = node2;
					
					route_duration_limit = node1.tw_b;
	
					
				}else {
					CustomerNode node = new CustomerNode(id-1,coor_x,coor_y,demand,early,late,service,Integer.MAX_VALUE,new int[0][0]);
					GraphManager.customerNodes.add(node);
				}
				
				line = buff.readLine();
			}
			
		// Close the buffered reader:
			
			buff.close();
	}
	
	/**
	 * This method reads the information of the workers
	 * @throws IOException 
	 */
	public void readWorkers() throws IOException {
		
		// Create the path to the instance file:
		
			String path_to_file = GlobalParameters.INSTANCE_FOLDER;
		
			if(instance_technician.equals("Complete")) {
				if(instance_version.equals("Team")) {
					path_to_file += "technicians_Team/technicians_"+instance_skills+"x"+instance_levels+"_"+instance_version+".txt";
				}else {
					path_to_file += "technicians_noTeam/technicians_"+instance_dataFile;
				}
			}else {
				if(instance_version.equals("Team")) {
					String inst = ""+instance_number;
					String first_number = inst.substring(2);
					path_to_file += "techniciansReduced_Team/"+instance_type+first_number+"/technicians_"+instance_skills+"x"+instance_levels+"_"+instance_version+"_"+"long"+".txt";
				}else {
					path_to_file += "techniciansReduced_noTeam/technicians_"+instance_dataFile;
				}
			}
			
		// Create a buffered reader:
			
			BufferedReader buff = new BufferedReader(new FileReader(path_to_file));
		
		// 1. Reads the first two lines
			
			buff.readLine(); // The first line can be ignored
			String technicians_line = buff.readLine();
			
		// 2. Capture the number of nodes and the capacity:
			
			int w = Integer.parseInt(technicians_line.split(": ")[1]);
			if(instance_numTechnicians != -1) {
				number_of_workers = instance_numTechnicians;
			}else {
				number_of_workers = w;
			}
			
		// 3. Skip four lines:
			
			buff.readLine();
			buff.readLine();
			buff.readLine();
			buff.readLine();
			buff.readLine();
			
		// 4. Read the remaining lines: information for each task/customer:
			
			// Read the customers and the depot:
			
			String line = buff.readLine();
			while(line != null) {
				String[] parts = line.split("\t");
				
				if(parts.length > 1) {
					
					int id = Integer.parseInt(parts[0]);
					int[][] skills_requirements = new int[instance_skills][instance_levels];
					String key = "";
					
					int act = 1;
					for(int i = 0;i<instance_skills;i++) {
						int current_level = Integer.parseInt(parts[act]);
						key += current_level+"-";
						act++;
						for(int j=0;j<instance_levels;j++) {
							if(j < current_level) {
								skills_requirements[i][j] = 1;
							}else {
								skills_requirements[i][j] = 0;
							}
							
						}
						
						
					}
					
					
					// Update the number available for each profile: 
					
					if(GraphManager.workerNodes.size() < instance_numTechnicians) {
						int profile_id = -1;
						if(GraphManager.number_of_workers_available_per_profile.containsKey(key)) {
							GraphManager.number_of_workers_available_per_profile.put(key, GraphManager.number_of_workers_available_per_profile.get(key)+1);
							profile_id = GraphManager.id_per_profile.get(key);
							GraphManager.all_ids_per_profile.get(key).add(id);
							
						}else {
							GraphManager.number_of_workers_available_per_profile.put(key, 1);
							GraphManager.number_of_unique_workers ++;
							GraphManager.id_per_profile.put(key, GraphManager.number_of_unique_workers);
							Profile profile = new Profile(GraphManager.number_of_unique_workers,key);
							GraphManager.uniqueProfiles.add(profile);
							profile_id = GraphManager.number_of_unique_workers;
							
							GraphManager.all_ids_per_profile.put(key, new ArrayList<Integer>());
							GraphManager.all_ids_per_profile.get(key).add(id);
							
							// Create the node:
							
							WorkerNode node = new WorkerNode(id,skills_requirements,key,profile_id);
							GraphManager.workerNodes.add(node);
						}
					}
					
					
					
					
				}
				
				SourceNode node = new SourceNode(0);
				GraphManager.sourceNode = node;
				
				line = buff.readLine();
			}
			
		// Close the buffered reader:
			
			buff.close();
			
	}
	
	/**
	 * This method reads the information of the workers
	 * @throws IOException 
	 */
	public void readWorkers_Solomon() throws IOException {
		
		// Set the number of workers:
		
			number_of_workers = 1;
			
		// Creates the source node:	
				
			SourceNode node = new SourceNode(0);
			GraphManager.sourceNode = node;

			
	}
	
	/**
	 * This method reads the main constraints: 
	 * \\TODO: for now, I will enter the values manually
	 */
	public void readMainConstraints() {
		
		driving_speed = GlobalParameters.DRIVING_SPEED; //km/h
		walking_speed = GlobalParameters.WALKING_SPEED; //km/h
		fixed_cost = GlobalParameters.FIXED_COST;
		variable_cost = GlobalParameters.VARIABLE_COST;
		max_walking_distance_bt2P = GlobalParameters.MAX_WD_B2P; //km 2.5
		subtour_duration_limit = GlobalParameters.SUBTOUR_TIME_LIMIT; //min
		route_walking_distance_limit = GlobalParameters.ROUTE_WALKING_DISTANCE_LIMIT; //km
		
	}
	
	/**
	 * This method reads the main constraints: 
	 * \\TODO: for now, I will enter the values manually
	 */
	public void readMainConstraints_Solomon() {
		
		driving_speed = GlobalParameters.DRIVING_SPEED; //km/h
		walking_speed = GlobalParameters.WALKING_SPEED; //km/h
		fixed_cost = GlobalParameters.FIXED_COST;
		variable_cost = GlobalParameters.VARIABLE_COST;
		max_walking_distance_bt2P = GlobalParameters.MAX_WD_B2P; //km 2.5
		subtour_duration_limit = GlobalParameters.SUBTOUR_TIME_LIMIT; //min
		route_walking_distance_limit = GlobalParameters.ROUTE_WALKING_DISTANCE_LIMIT; //km
		
	}
	
	/**
	 * This method creates all the arcs.
	 */
	public void createArcs() {
		
		// Arcs in the workers graph:
		
			// Arcs from the source node to each worker node:
		
				for(WorkerNode node:GraphManager.workerNodes) {
					
					int cap = GraphManager.number_of_workers_available_per_profile.get(node.skills_key);
					
					for(int i = 1; i <= cap; i++) {
						
						GraphManager.arcs_g1.put(0+"-"+node.id+"-"+i, new Arc(GraphManager.sourceNode,node,0,0,0,1,i)); //We will assume these are driving arcs
						
						
					}
					
					
				}
			
				
				
			// Arcs from each worker node to the source depot node:
			
		
				for(WorkerNode node:GraphManager.workerNodes) {
					
					GraphManager.arcs_g1.put(node.id+"-"+0+" - "+1, new Arc(node,GraphManager.sourceDepotNode,0,0,0,1)); //We will assume these are driving arcs
					
				}
				
			
				
			// If the team version is allowed:
			
			
				if(instance_version.equals("Team")) {
					
					// Arcs from each worker node, to the next worker nodes:
					
					for(int i = 0; i < GraphManager.number_of_workers; i++) {
						
						WorkerNode node = GraphManager.workerNodes.get(i);
						
						for(int j = i;j < GraphManager.number_of_workers; j++) {
							
							WorkerNode node2 = GraphManager.workerNodes.get(j);
							
							int cap = GraphManager.number_of_workers_available_per_profile.get(node2.skills_key);
							
							for(int k = 1; k <= cap; k++) {
								
								GraphManager.arcs_g1.put(node.id+"-"+node2.id+" - "+k,new Arc(node,node2,0,0,0,1,k));
								
								
							}
							
						}
						
					}
					
					
				}
			
			
			
			// If skills are not to be considered:
				
				
					GraphManager.arcs_g1.put(GraphManager.sourceNode+"-"+GraphManager.sourceDepotNode,new Arc(GraphManager.sourceNode,GraphManager.sourceDepotNode,0,0,0,1));
					
				
			
		// Arcs in the customer graph:----------------------------------
				
			// Driving arcs arriving to any customer:
				
				// Arcs from the source depot:
				
					for(CustomerNode node:GraphManager.customerNodes) {
						
						double distance = calculateDistance(GraphManager.sourceDepotNode,node);
						double time = calculateTime(distance,driving_speed);	
						
						if(GraphManager.sourceDepotNode.tw_a + time <= node.tw_b) {
							
							
							GraphManager.arcs_g2.put(GraphManager.sourceDepotNode.id+"-"+node.id, new Arc(GraphManager.sourceDepotNode,node,distance,distance*variable_cost,time,1));
							
						}
						
					}
					
				// Arcs from other customers:
					
					for(CustomerNode node:GraphManager.customerNodes) {
						
						for(CustomerNode node2:GraphManager.customerNodes) {
							
							if(node.id != node2.id) {
								
								double distance = calculateDistance(node,node2);
								double time = calculateTime(distance,driving_speed);	
								
								if(node.tw_a + node.service + time <= node2.tw_b) {
								
									GraphManager.arcs_g2.put(node.id+"-"+node2.id, new Arc(node,node2,distance,distance*variable_cost,time,1));
								
								}
							}
							
						}
					}
					
			// Walking arcs arriving to any customer:
					
				// Arcs from the source depot:
				
					for(CustomerNode node:GraphManager.customerNodes) {
						
						double distance = calculateDistance(GraphManager.sourceDepotNode,node);
						double time = calculateTime(distance,walking_speed);	
						
						//System.out.println(distance+" - "+max_walking_distance_bt2P+" - "+time+" - "+calculateTime(distance,driving_speed));
						if(GraphManager.sourceDepotNode.tw_a + time <= node.tw_b && distance <= max_walking_distance_bt2P) {
							
							
							GraphManager.arcs_g2.put(GraphManager.sourceDepotNode.id+"_"+node.id, new Arc(GraphManager.sourceDepotNode,node,distance,0,time,2));
							
						}
						
					}
					
				// Arcs from other customers:
					
					for(CustomerNode node:GraphManager.customerNodes) {
						
						for(CustomerNode node2:GraphManager.customerNodes) {
							
							if(node.id != node2.id) {
								
								double distance = calculateDistance(node,node2);
								double time = calculateTime(distance,walking_speed);	
								
								if(distance <= max_walking_distance_bt2P) {
								
									GraphManager.arcs_g2.put(node.id+"_"+node2.id, new Arc(node,node2,distance,0,time,2));
								
								}
							}
							
						}
					}
					
			// Driving arcs towards the end depot:
					
				// Arcs from the source depot:
									
					GraphManager.arcs_g2.put(GraphManager.sourceDepotNode.id+"-"+GraphManager.endDepotNode.id, new Arc(GraphManager.sourceDepotNode,GraphManager.endDepotNode,0,0,0,1));
								
				// Arcs from customers:
					
					for(CustomerNode node:GraphManager.customerNodes) {
						
						double distance = calculateDistance(node,GraphManager.endDepotNode);
						double time = calculateTime(distance,driving_speed);	
								
						if(node.tw_a + node.service + time <= GraphManager.endDepotNode.tw_b) {
								
							GraphManager.arcs_g2.put(node.id+"-"+GraphManager.endDepotNode.id, new Arc(node,GraphManager.endDepotNode,distance,distance*variable_cost,time,1));
								
						}
					}
		
			// Walking arcs towards the start depot, from customers
					
				// Arcs from customers:
					
					for(CustomerNode node:GraphManager.customerNodes) {
						
						double distance = calculateDistance(node,GraphManager.sourceDepotNode);
						double time = calculateTime(distance,walking_speed);	
								
						if(node.tw_a + node.service + time <= GraphManager.sourceDepotNode.tw_b && distance <= max_walking_distance_bt2P) {
								
							GraphManager.arcs_g2.put(node.id+"_"+GraphManager.sourceDepotNode.id, new Arc(node,GraphManager.sourceDepotNode,distance,0,time,2));
								
						}
					}		
	}
	
	/**
	 * This method calculates the time of an arc
	 * @param dis
	 * @param speed
	 * @return
	 */
	private double calculateTime(double dis,double speed) {
		return (dis * 60)/speed;
	}
	
	/**
	 * This method calculates the distance of an arc
	 * @param tail tail node
	 * @param head head node
	 * @return
	 */
	private double calculateDistance(Node tail, Node head) {
		//if(CGParameters.ROUND_SOLOMON) {
			//return Math.floor(utilities.EuclideanCalculator.calc(tail.coor_x,tail.coor_y,head.coor_x,head.coor_y)*10)/10;
			
		//}else {
			return utilities.EuclideanCalculator.calc(tail.coor_x,tail.coor_y,head.coor_x,head.coor_y);
			
		//}
				 
	}
	
	/**
	 * This method reads the capacities of the vehicles for each set of instances
	 * @throws IOException
	 */
	private void readCapacity() throws IOException {
			File file = new File(GlobalParameters.INSTANCE_FOLDER+"capacities.txt");
			BufferedReader bufRdr = new BufferedReader(new FileReader(file));
			for (int i = 0; i < 6; i++) {
				String line = bufRdr.readLine(); //READ Num Nodes
				String[] spread = line.split(":");
				if(instance_type.equals(spread[0])){
					int serie = Integer.parseInt(spread[1]);
					if (instance_number-serie<50) {
						route_capacity = Integer.parseInt(spread[2]);
						bufRdr.close();
						return;
					}
				}
			}
			bufRdr.close();
			
		}

	
	
	public static void printGraphInformation() {
		
		System.out.println("----------------------");
		
		for(CustomerNode node:GraphManager.customerNodes) {
			System.out.println(node);
		}
		
		for(WorkerNode node:GraphManager.workerNodes) {
			System.out.println(node);
		}
		
		System.out.println(GraphManager.sourceNode);
		System.out.println(GraphManager.sourceDepotNode);
		System.out.println(GraphManager.endDepotNode);
		
		System.out.println("----------------------");
		
		System.out.println("");
		
		System.out.println("----------------------");
		
		System.out.println("Graph 1: ");
		System.out.println("Number of arcs:"+GraphManager.arcs_g1.keySet().size());
		//for(Arc arc:GraphManager.arcs_g1.values()) {
		//	System.out.println(arc);
		//}
		
		System.out.println("");
		System.out.println("Graph 2: ");
		System.out.println("Number of arcs:"+GraphManager.arcs_g2.keySet().size());
		
		for(Arc arc:GraphManager.arcs_g2.values()) {
			
			if(arc.getT_mode() == 2) {
				System.out.println(arc);
			}
			
		}
		
		System.out.println("----------------------");
		
	}
}
