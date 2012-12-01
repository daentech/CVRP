package com.github.daentech.algorithms;

import java.util.ArrayList;
import java.util.Random;

import com.github.daentech.CVRPData;
import com.github.daentech.LimitedPriorityQueue;

public class SimpleGA extends Algorithm{

	// Run the genetic algorithm to improve the results
	@Override
	public void run(int iterations) {
		// Do iterations
		for(int i = 0; i < iterations; i++){
			// Find the best 5 paths of the generated paths
			
			LimitedPriorityQueue lpq = new LimitedPriorityQueue(11, new Double(1));
			
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
			
		}
		
	}
	
	private int[] swap(int a, int b, int[] chromosome){
		// Find the position of a and b in each chromosome
		int aPos = 0, bPos = 0;
		for(int i = 0; i < chromosome.length; i++){
			if(chromosome[i] == a) aPos = i;
			if(chromosome[i] == b) bPos = i;
		}
		// Swap the values
		int tmp = chromosome[aPos];
		chromosome[aPos] = chromosome[bPos];
		chromosome[bPos] = tmp;
		// Return the new chromosome
		return chromosome;
	}
	
	private void pmx(int[] indices){
		// Create a new oldchromosomes object to store the current ones
		ArrayList<int[][]> oldChromosomes = new ArrayList<int[][]>();
		
		for(int j = 0; j < indices.length && j < 10; j++){
			oldChromosomes.add(chromosomes.get(indices[j]));
		}
		// Reinitialise the chromosome list
		chromosomes = new ArrayList<int[][]>();
		// Remove the depot from each truck's path
		//ArrayList<int[][]> chromosomes = getChromosomes();
		
		ArrayList<int[]> tmpPaths = new ArrayList<int[]>();
		
		for(int i = 0; i < oldChromosomes.size(); i++){
			int[] strippedPath = CVRPData.stripDepots(oldChromosomes.get(i));
			tmpPaths.add(strippedPath);
			/*String s = "{";
			for(int j = 0; j < strippedPath.length; j++){
				s = s.concat(String.valueOf(strippedPath[j]));
				if(j != strippedPath.length - 1) s = s.concat(",");
			}
			s = s.concat("}");
			System.out.println(s); */
		}
		// crossover each of the 5 with each other to give 25
		Random rnd = new Random();
		
		for(int i = 0; i < tmpPaths.size(); i++){
			for(int j = 0; j < tmpPaths.size(); j++){
				// Cross the 2 indices over
				int start = rnd.nextInt() % (CVRPData.NUM_NODES - 2);
				int end = rnd.nextInt() % (CVRPData.NUM_NODES - 2);
				if (start < 0) start = start * -1;
				if (end < 0) end = end * -1;
				while(start == end)
					end = rnd.nextInt() & CVRPData.NUM_NODES - 1;
				if(start > end){
					// We need to swap them
					int tmp = start;
					start = end;
					end = tmp;
				}
				
				int[] newPathA = tmpPaths.get(i);
				int[] newPathB = tmpPaths.get(j);
				
				// Between start and end we need to swap the values
				for(int k = start; k < end; k++){
					int a = tmpPaths.get(i)[k];
					int b = tmpPaths.get(j)[k];
					newPathA = swap(a, b, newPathA);
					newPathB = swap(a, b, newPathB);
				}
				
				// Randomly apply a mutation
				newPathA = mutate(newPathA);
				newPathB = mutate(newPathB);
				
				chromosomes.add(makeValidPaths(newPathA));
				chromosomes.add(makeValidPaths(newPathB));
			}
		}
		//chromosomes = oldChromosomes;
	}
	
	private int[] mutate(int[] path) {	 
		
		Random rnd = new Random();

		for(int i = 0; i < path.length; i++){
			if(rnd.nextDouble() < 0.002){
				// We are swapping i
				int swapWith = rnd.nextInt() % (CVRPData.NUM_NODES - 2) + 1;
				if(swapWith < 0) swapWith = swapWith * -1;
				path = swap(path[i], path[swapWith], path);
			}
		}
		
		return path;
	}

	private int[][] makeValidPaths(int[] inpath){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for(int i = 0; i < inpath.length; i++){
			nodes.add(new Integer(inpath[i]));
		}
		
		int[][] paths = new int[76][];
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(1));
		int j = 0;
		int pos = 0;
		while(CVRPData.pathIsValid(convertIntegers(path)) && nodes.size() > 0){
			Integer i = nodes.get(0);
			path.add(i);
			if(CVRPData.pathIsValid(convertIntegers(path))){
				// The path is valid, so we need to remove the node from the set
				nodes.remove(0);
				j++;
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
		
		return paths;
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
