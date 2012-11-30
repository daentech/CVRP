package com.github.daentech.algorithms;

import java.util.ArrayList;

import com.github.daentech.CVRPData;
import com.github.daentech.LimitedPriorityQueue;

public class SimpleGA extends Algorithm{

	// Run the genetic algorithm to improve the results
	@Override
	public void run(int iterations) {
		// Do iterations
		for(int i = 0; i < iterations; i++){
			// Find the best 5 paths of the randomly generated paths
			
			LimitedPriorityQueue lpq = new LimitedPriorityQueue(5, new Double(1));
			
			//shortestPathLength.
			ArrayList<int[][]> chromosomes = getChromosomes();
			
			for(int j = 0; j < chromosomes.size(); j++){
				double length = fitness(chromosomes.get(j));
				lpq.push(j, new Double(length));
			}
			lpq.printArray();
			
			int[] indices = lpq.getIndices();
			
			

			// Cross the values over to form new paths
			pmx(indices);
			
			// calculate the new fitness of each path
			
			// Chop the list down to the best 10 paths
			
			// save the best path, weight and score (mean, median and mode weights)
			
		}
		
	}
	
	private void pmx(int[] indices){
		// Create a new oldchromosomes object to store the current ones
		ArrayList<int[][]> oldChromosomes = new ArrayList<int[][]>();
		
		for(int j = 0; j < indices.length; j++){
			oldChromosomes.add(chromosomes.get(indices[j]));
		}
		// Reinitialise the chromosome list
		chromosomes = new ArrayList<int[][]>();
		// Remove the depot from each truck's path
		//ArrayList<int[][]> chromosomes = getChromosomes();
		for(int i = 0; i < oldChromosomes.size(); i++){
			int[] strippedPath = CVRPData.stripDepots(oldChromosomes.get(i));
			String s = "{";
			for(int j = 0; j < strippedPath.length; j++){
				s = s.concat(String.valueOf(strippedPath[j]));
				if(j != strippedPath.length - 1) s = s.concat(",");
			}
			s = s.concat("}");
			System.out.println(s);
		}
		// crossover each of the 5 with each other to give 25
		
		// randomly apply mutation
		
		// re-apply the depot using first fit again
		chromosomes = oldChromosomes;
	}

	// Calculate the fitness of a given path array
	private double fitness(int[][] paths){
		// We want to minimise the path weight
		
		double weight = 0.0;
		for(int[] path : paths ){
			if(path == null) return weight;
			weight += CVRPData.getPathDistance(path);
		}
		return weight;
	}

	@Override
	public String getName() {
		return "SimpleGA";
	}

}
