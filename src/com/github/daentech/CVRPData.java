/**
 * CVRPData.java
 *
 * This class provides most of the information in fruitybun-data.vrp
 * (and what this class does not provide you do not need).
 * This class provides "get methods" to access the demand and distance 
 * information and it does some parameter checking to make sure
 * that nodes passed to the get methods are within the valid range.
 *
 * It does NOT provide direct access to node coordinates and instead only
 * computes the Euclidean distance between them. However, it would be easy
 * to add a get method to access coordinates, although you would have to
 * decide how to return a coordinate: in an array or some kind of coordinate 
 * class.
 *
 * This class is an alternative to reading fruitybun-data.vrp. This class
 * is more convenient if you want to work in java but it encodes
 * the data as arrays so it specifies only the fruitybun CVRP instance.
 * In contrast, code that can read fruitybun-data.vrp should be able to
 * read other .vrp files.
 *
 * @author Tim Kovacs
 * @version 9/9/2011
 */

package com.github.daentech;


public class CVRPData {

    /** The capacity that all vehicles in fruitybun-data.vrp have. */
    public static final int VEHICLE_CAPACITY = 220;

    /** The number of nodes in the fruitybun CVRP i.e. the depot and the customers */
    public static final int NUM_NODES = 76;

    /** Return the demand for a given node. */
    public static int getDemand(int node) {
	if (!nodeIsValid(node)) {
	    System.err.println("Error: demand for node " + node + 
			       " was requested from getDemand() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    
	return demand[node];
    }
        
    /** Return the Euclidean distance between the two given nodes */
    public static double getDistance(int node1, int node2) {
	if (!nodeIsValid(node1)) {
	    System.err.println("Error: distance for node " + node1 + 
			       " was requested from getDistance() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    
	
	if (!nodeIsValid(node2)) {
	    System.err.println("Error: distance for node " + node2 + 
			       " was requested from getDistance() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    

	int x1 = coords[node1][X_COORDINATE];
	int y1 = coords[node1][Y_COORDINATE];
	int x2 = coords[node2][X_COORDINATE];
	int y2 = coords[node2][Y_COORDINATE];

	// compute Euclidean distance
	return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));

    // For example, the distance between 33,44 and 44,13 is:
    // 33-44 = -11 and 11^2 = 121
    // 44-13 = 31 and 31^2 = 961
    // 121 + 961 = 1082
    // The square root of 1082 = 32.893768...
    // A simpler example is the distance between nodes 54 and 36, which have coordinates 55,57 and 55,50 and distance 7.0
    }    
    
    public static double getPathDistance(int[] path){
    	
    	double dist = 0;
    	
    	for (int i = 0; i < path.length - 1; i++){
    		dist += getDistance(path[i], path[i+1]);
    	}
    	
    	
    	return dist;
    }

    /** Return true if the given node is within the valid range (1..NUM_NODES), false otherwise */
    private static boolean nodeIsValid(int node) {
	if (node < 1 || node > NUM_NODES)
	    return false;
	else
	    return true;
    }
    
    public static final int X_COORDINATE = 0; // x-axis coordinate is dimension 0 in coords[][]
    public static final int Y_COORDINATE = 1; // y-axis coordinate is dimension 1 in coords[][]

    
    // Return the coordinates for rendering
    public static int[][] getCoords(){
    	return coords;
    }
    
    // Return the total demand for the graph
    public static int getTotalDemand(){
    	int totalDemand = 0;
    	for(int i = 1; i < demand.length; i++){
    		totalDemand += demand[i];
    	}
    	return totalDemand;
    }
    
    public static int getPathDemand(int[] path){
    	int totalDemand = 0;
    	for(int i = 1; i < path.length; i++){
    		totalDemand += demand[path[i]];
    	}
    	return totalDemand;
    }
    
    // Return whether the path is valid
    public static boolean pathIsValid(int[] path){
    	return getPathDemand(path) <= VEHICLE_CAPACITY;
    }
    
    // 2-dimensional array with the coordinates of each node in fruitybun-data.vrp. 
    // coords[0] is a dummy entry which is not used. Node 1 is at coords[1], node 2 is at coords[2] and so on.
    // A more Object-Oriented solution would have been to create coordinate objects but that may be overkill.
    private static int [][] coords = new int[][] 
	{{-1, -1}, // dummy entry to make index of array match indexing of nodes in fruitybun-data.vrp
	 {40, 40}, // the coordinates of node 1 (the depot)
	 {22, 22}, // the coordinates of node 2 ...
	 {36, 26},
	 {21, 45},
	 {45, 35},
	 {55, 20},
	 {33, 34},
	 {50, 50},
	 {55, 45},
	 {26, 59}, // node 10
	 {40, 66},
	 {55, 65},
	 {35, 51},
	 {62, 35},
	 {62, 57},
	 {62, 24},
	 {21, 36},
	 {33, 44},
	 {9, 56},
	 {62, 48}, // node 20
	 {66, 14},
	 {44, 13},
	 {26, 13},
	 {11, 28},
	 {7, 43},
	 {17, 64},
	 {41, 46},
	 {55, 34},
	 {35, 16},
	 {52, 26}, // node 30
	 {43, 26},
	 {31, 76},
	 {22, 53},
	 {26, 29},
	 {50, 40},
	 {55, 50},
	 {54, 10},
	 {60, 15},
	 {47, 66},
	 {30, 60}, // node 40
	 {30, 50},
	 {12, 17},
	 {15, 14},
	 {16, 19},
	 {21, 48},
	 {50, 30},
	 {51, 42},
	 {50, 15},
	 {48, 21},
	 {12, 38}, // node 50
	 {15, 56},
	 {29, 39},
	 {54, 38},
	 {55, 57},
	 {67, 41},
	 {10, 70},
	 {6, 25},
	 {65, 27},
	 {40, 60},
	 {70, 64}, // node 60
	 {64, 4},
	 {36, 6},
	 {30, 20},
	 {20, 30},
	 {15, 5},
	 {50, 70},
	 {57, 72},
	 {45, 42},
	 {38, 33},
	 {50, 4},  // node 70
	 {66, 8},
	 {59, 5},
	 {35, 60},
	 {27, 24},
	 {40, 20},
	 {40, 37}};// node 76
    
    private static int[] demand = new int[]
	{9999999,  // dummy entry to make index of array match indexing of nodes in fruitybun-data.vrp
	 0,  // node 1
	 18, // node 2
	 26, // node 3
	 11, // node 4
	 30, // node 5
	 21, // node 6
	 19, // node 7
	 15, // node 8
	 16, // node 9
	 29, // node 10
	 26, // node 11
	 37, // node 12
	 16, // node 13
	 12, // node 14
	 31, // node 15
	 8,  // node 16
	 19, // node 17
	 20, // node 18
	 13, // node 19
	 15, // node 20
	 22, // node 21
	 28, // node 22
	 12, // node 23
	 6,  // node 24
	 27, // node 25
	 14, // node 26
	 18, // node 27
	 17, // node 28
	 29, // node 29
	 13, // node 30
	 22, // node 31
	 25, // node 32
	 28, // node 33
	 27, // node 34
	 19, // node 35
	 10, // node 36
	 12, // node 37
	 14, // node 38
	 24, // node 39
	 16, // node 40
	 33, // node 41
	 15, // node 42
	 11, // node 43
	 18, // node 44
	 17, // node 45
	 21, // node 46
	 27, // node 47
	 19, // node 48
	 20, // node 49
	 5,  // node 50
	 22, // node 51
	 12, // node 52
	 19, // node 53
	 22, // node 54
	 16, // node 55
	 7,  // node 56
	 26, // node 57
	 14, // node 58
	 21, // node 59
	 24, // node 60
	 13, // node 61
	 15, // node 62
	 18, // node 63
	 11, // node 64
	 28, // node 65
	 9,  // node 66
	 37, // node 67
	 30, // node 68
	 10, // node 69
	 8,  // node 70
	 11, // node 71
	 3,  // node 72
	 1,  // node 73
	 6,  // node 74
	 10, // node 75
	 20};// node 76    

}