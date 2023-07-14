package dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class GraphManager {

	// Attributes for storing the network:
	
	/**
	 * List containing all the customer nodes:
	 */
	public static ArrayList<CustomerNode> customerNodes;
	
	/**
	 * Number of customer nodes
	 */
	public static int number_of_customers;
	
	/**
	 * List containing all the worker nodes:
	 */
	public static ArrayList<WorkerNode> workerNodes;
	
	/**
	 * Number of workers
	 */
	public static int number_of_workers;
	
	/**
	 * List containing all the unique profiles: 
	 * 
	 */
	
	public static ArrayList<Profile> uniqueProfiles;
	
	/**
	 * ID of the profile with the following key
	 */
	
	public static Hashtable<String,Integer> id_per_profile;
	
	/**
	 * All the initial ID's of the profiles with the following key
	 */
	
	public static Hashtable<String,ArrayList<Integer>> all_ids_per_profile;
	
	/**
	 * Number of unique workers profiles
	 */
	public static int number_of_unique_workers;
	
	/**
	 * Number of workers available for each profile:
	 */
	
	public static Hashtable<String,Integer> number_of_workers_available_per_profile;
	
	/**
	 * Reference to the source depot node
	 */
	public static SourceDepot sourceDepotNode;
	
	/**
	 * Reference to the end depot node
	 */
	public static EndDepot endDepotNode;
	
	/**
	 * Reference to the source node
	 */
	public static SourceNode sourceNode;
	
	/**
	 * List of arcs from the first graph
	 */
	public static HashMap<String,Arc> arcs_g1;
	
	/**
	 * List of arcs from the second graph
	 */
	public static HashMap<String,Arc> arcs_g2;
	

	// Main methods:
	
	/**
	 * This method generates a new Graph Manager
	 */
	public GraphManager() {
		
		GraphManager.number_of_unique_workers = 0;
		uniqueProfiles = new ArrayList<Profile>();
		customerNodes = new ArrayList<CustomerNode>();
		workerNodes = new ArrayList<WorkerNode>();
		arcs_g1 = new HashMap<String,Arc>();
		arcs_g2 = new HashMap<String,Arc>();
		number_of_workers_available_per_profile = new Hashtable<String,Integer>();
		id_per_profile = new Hashtable<String,Integer>();
		all_ids_per_profile = new Hashtable<String,ArrayList<Integer>>();
	}
	
	/**
	 * This method initializes key attributes of this class, once all the nodes have been added
	 */
	public static void initializeGraphManager() {
		
		// General constants:
		
		number_of_customers = customerNodes.size();
		number_of_workers = workerNodes.size();
		
	}
	
}
