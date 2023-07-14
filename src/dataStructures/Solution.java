package dataStructures;

import java.util.ArrayList;
import java.util.Hashtable;

public class Solution {

	/**
	 * Current instance
	 */
	private int instance;
	
	private ArrayList<Integer> tails;
	
	private ArrayList<Integer> heads;
	
	private ArrayList<Integer> types;
	
	private ArrayList<Integer> routes;
	
	private ArrayList<Integer> routesUnique;
	
	private Hashtable<Integer,ArrayList<Integer>> profiles_ids;
	
	private Hashtable<Integer,ArrayList<Integer>> profiles_number_ids;
	
	private Hashtable<Integer,int[][]> skills_per_route;
	
	private ArrayList<Integer> outsourcedNodes;
	
	private Hashtable<Integer,ArrayList<Integer>> routes_visiting_customer;
	
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
	 * This method creates a new solution
	 * @param dataFile
	 * @param instanceType
	 * @param instanceNumber
	 * @param instanceSkills
	 * @param instanceLevels
	 * @param instanceVersion
	 */
	public Solution(String dataFile, String instanceType,int instanceNumber, int instanceSkills, int instanceLevels, String instanceVersion) {
		
		// Initialize the main variables:
		
		instance_type = instanceType;
		instance_number = instanceNumber;
		instance_skills = instanceSkills;
		instance_levels = instanceLevels;
		instance_version = instanceVersion;
		instance_dataFile = dataFile;

		tails = new ArrayList<Integer>();
		heads = new ArrayList<Integer>();
		types = new ArrayList<Integer>();
		routes = new ArrayList<Integer>();
		setOutsourcedNodes(new ArrayList<Integer>());
		setProfiles_ids(new Hashtable<Integer,ArrayList<Integer>>());
		setProfiles_number_ids(new Hashtable<Integer,ArrayList<Integer>>());
		setRoutes_visiting_customer(new Hashtable<Integer,ArrayList<Integer>>());
		this.setSkills_per_route(new Hashtable<Integer,int[][]>());
		routesUnique = new ArrayList<Integer>();
	}

	
	public String getSkillsByRouteString(int route) {
		String skills_string = "";
		if(skills_per_route.containsKey(route)) {
			for(int i=0;i < DataHandler.instance_skills;i++) {
				boolean found = false;
				int level = 4;
				for(int j=0;j < DataHandler.instance_levels && !found;j++) {
					if(skills_per_route.get(route)[i][j] == 0) {
						level = j;
						found = true;
					}
				}
				skills_string += "("+level+")"+"_";
			}
		}
		return skills_string;
	}
	
	/**
	 * @return the instance
	 */
	public int getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(int instance) {
		this.instance = instance;
	}

	/**
	 * @return the tails
	 */
	public ArrayList<Integer> getTails() {
		return tails;
	}

	/**
	 * @param tails the tails to set
	 */
	public void setTails(ArrayList<Integer> tails) {
		this.tails = tails;
	}

	/**
	 * @return the heads
	 */
	public ArrayList<Integer> getHeads() {
		return heads;
	}

	/**
	 * @param heads the heads to set
	 */
	public void setHeads(ArrayList<Integer> heads) {
		this.heads = heads;
	}

	/**
	 * @return the types
	 */
	public ArrayList<Integer> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(ArrayList<Integer> types) {
		this.types = types;
	}

	/**
	 * @return the routes
	 */
	public ArrayList<Integer> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(ArrayList<Integer> routes) {
		this.routes = routes;
	}

	/**
	 * @return the routesUnique
	 */
	public ArrayList<Integer> getRoutesUnique() {
		return routesUnique;
	}

	/**
	 * @param routesUnique the routesUnique to set
	 */
	public void setRoutesUnique(ArrayList<Integer> routesUnique) {
		this.routesUnique = routesUnique;
	}

	public Hashtable<Integer,ArrayList<Integer>> getProfiles_ids() {
		return profiles_ids;
	}

	public void setProfiles_ids(Hashtable<Integer,ArrayList<Integer>> profiles_ids) {
		this.profiles_ids = profiles_ids;
	}

	public Hashtable<Integer,ArrayList<Integer>> getProfiles_number_ids() {
		return profiles_number_ids;
	}

	public void setProfiles_number_ids(Hashtable<Integer,ArrayList<Integer>> profiles_number_ids) {
		this.profiles_number_ids = profiles_number_ids;
	}

	public Hashtable<Integer,int[][]> getSkills_per_route() {
		return skills_per_route;
	}

	public void setSkills_per_route(Hashtable<Integer,int[][]> skills_per_route) {
		this.skills_per_route = skills_per_route;
	}

	public ArrayList<Integer> getOutsourcedNodes() {
		return outsourcedNodes;
	}

	public void setOutsourcedNodes(ArrayList<Integer> outsourcedNodes) {
		this.outsourcedNodes = outsourcedNodes;
	}

	public Hashtable<Integer,ArrayList<Integer>> getRoutes_visiting_customer() {
		return routes_visiting_customer;
	}

	public void setRoutes_visiting_customer(Hashtable<Integer,ArrayList<Integer>> routes_visiting_customer) {
		this.routes_visiting_customer = routes_visiting_customer;
	}
	
	
}
