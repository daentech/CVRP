package com.github.daentech.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.github.daentech.CVRPData;

public abstract class Algorithm {
	
	private int[][] paths = new int[76][];

	// Private method for converting arraylist<Integer> to int[]
	private static int[] convertIntegers(List<Integer> integers)	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	// Abstract method for path calculation
	// Must be overriden by sub classes
	public abstract void run();
	
	// Abstract method for returning the name of the algorithm
	public abstract String getName();
	
	// Randomise trucks
	public void randomise(){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for(int i = 2; i < CVRPData.getCoords().length; i++){
			nodes.add(new Integer(i));
		}
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(1));
		Random rand = new Random();
		int pos = 0;
			while(CVRPData.pathIsValid(convertIntegers(path)) && nodes.size() > 0){
			// Choose a random number from the ArrayList
			int getNodeVal = rand.nextInt(nodes.size());
			//System.out.println(getNodeVal);
			Integer i = nodes.get(getNodeVal);
			path.add(i);
			if(CVRPData.pathIsValid(convertIntegers(path))){
				// The path is valid, so we need to remove the node from the set
				nodes.remove(getNodeVal);
			} else {
				path.remove(path.size() - 1); // Remove the last item
				path.add(new Integer(1));
				paths[pos++] = convertIntegers(path);
				
				path = new ArrayList<Integer>();
				path.add(new Integer(1));
			}
		}
			
		// Store the last path as well
		path.add(new Integer(1));
		paths[pos++] = convertIntegers(path);
	}
	
	// Return an array of int arrays for the series of paths
	public int[][] getPaths(){
		return paths;
	}
	
}
