package com.github.daentech.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.github.daentech.CVRPData;

public abstract class Algorithm {
	
	protected ArrayList<Double> weights = new ArrayList<Double>();
	protected ArrayList<int[][]> chromosomes = new ArrayList<int[][]>();
	protected ArrayList<Score> scores = new ArrayList<Score>();

	// Private method for converting arraylist<Integer> to int[]
	protected static int[] convertIntegers(List<Integer> integers)	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	private static double[] convertDoubles(List<Double> doubles)	{
	    double[] ret = new double[doubles.size()];
	    Iterator<Double> iterator = doubles.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().doubleValue();
	    }
	    return ret;
	}
	
	// Abstract method for path calculation
	// Must be overriden by sub classes
	// This method should store on each iteration the best weight path 
	// it has achieved in the weights arraylist
	public abstract void run(int iterations);
	
	// Abstract method for returning the name of the algorithm
	public abstract String getName();
	
	// Randomise trucks
	public void randomise(){
		for(int j = 0; j < 2000; j++){
			int[][] paths = new int[76][];
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
			
			chromosomes.add(paths);
		}
	}
	
	// Return the scores arraylist
	public ArrayList<Score> getScores(){
		return scores;
	}
	
	public void addScore(double best, double mean, double median){
		scores.add(new Score(best, mean, median));
	}
	
	// Return the ArrayList of Chromosomes
	public ArrayList<int[][]> getChromosomes(){
		return chromosomes;
	}
	
	// Return an array of best weight values at each step
	public double[] getWeightsOverTime(){
		return convertDoubles(weights);
	}
	
	public class Score{
		public double best;
		public double mean;
		public double median;
		
		public Score(double best, double mean, double median){
			this.best = best;
			this.mean = mean;
			this.median = median;
		}
	}
	
}
