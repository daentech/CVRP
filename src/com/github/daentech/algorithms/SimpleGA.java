package com.github.daentech.algorithms;

import java.util.ArrayList;
import java.util.Random;

import com.github.daentech.CVRPData;
import com.github.daentech.LimitedPriorityQueue;

public class SimpleGA extends Algorithm {

	private final double probability = 0.002;
	private final double zeroDeltaAddition = 0.00015;
	private final int queueLimit = 222;

	private int zeroDeltaIterations = 1;
	private double prevWeight;

	private int[][] bestPath = generateRandomPath();

	// Run the genetic algorithm to improve the results
	@Override
	public void run(int iterations) {
		// Do iterations
		for (int i = 0; i < iterations; i++) {
			// Find the best 5 paths of the generated paths

			LimitedPriorityQueue lpq = new LimitedPriorityQueue(queueLimit,
					new Double(1));

			// shortestPathLength.
			ArrayList<int[][]> chromosomes = getChromosomes();

			for (int j = 0; j < chromosomes.size(); j++) {
				double length = fitness(chromosomes.get(j));
				lpq.push(j, new Double(length));
			}
			if (i % 1000 == 0) {
				lpq.printArray(i);
				//System.out.println("ZeroDeltaIterations: " + zeroDeltaIterations);
				System.out.println("Best path: " + fitness(bestPath));
			}

			int[] indices = lpq.getIndices();

			// Cross the values over to form new paths
			// System.out.println("Iterations without change: " +
			// zeroDeltaIterations + ". Swapping with probability: " +
			// (probability + zeroDeltaIterations * zeroDeltaAddition));
			pmx(indices);

			if (fitness(chromosomes.get(indices[0])) < fitness(bestPath))
				bestPath = chromosomes.get(indices[0]);
			
			// If iterations > 1000 then we keep the first 2 and scrap the rest.
			if (zeroDeltaIterations == 5000) {
				// If we've had 10,000 iterations with no change. Scrap all
				// (keep the best?)
				// We're probably stuck in a local minima

				randomise();
				//for (int k = 0; k < 30; k++) {
				//	chromosomes.add(generateRandomPath());
				//}
				zeroDeltaIterations = 0;
			} else if (zeroDeltaIterations % 300 == 0 && zeroDeltaIterations >= 300) {
				// We've probably hit a local minima, so generate a lot more
				// random ones
				int[][] pathA = chromosomes.get(indices[0]);
				// int[][] pathB = chromosomes.get(1);
				chromosomes.clear();
				chromosomes.add(pathA);
				// chromosomes.add(pathB);
				for (int k = 0; k < 200; k++) {
					chromosomes.add(generateRandomPath());
				}

			} /*else if (zeroDeltaIterations % 500 == 0) {
				int[][] pathA = chromosomes.get(indices[0]);
				int[][] pathB = chromosomes.get(indices[1]);
				chromosomes.clear();
				chromosomes.add(pathA);
				chromosomes.add(pathB);
				for (int k = 0; k < 15; k++) {
					// Add a copy of the existing path
					chromosomes.add(pathA);
				}
			}*/

			double delta = fitness(chromosomes.get(indices[0])) - prevWeight;
			if (Math.abs(delta) < 0.2) {
				zeroDeltaIterations++;
			} else {
				zeroDeltaIterations = 0;
			}

			prevWeight = fitness(chromosomes.get(indices[0]));
			weights.add(new Double(fitness(bestPath)));
		}

	}

	public int[][] getBestPath() {
		return bestPath;
	}
	
	public void printBestPath(){
		String s = "[";
		for(int[] path : bestPath){
			if(path == null){
				s = s.substring(0, s.length() - 1);
				break;
			}
			s = s.concat("{");
			for(int i = 0; i < path.length; i++){
				s = s.concat(String.valueOf(path[i]));
				if(i < path.length - 1) s = s.concat(",");
			}
			s = s.concat("},");
		}
		s = s.concat("]");
		
		System.out.println(s);
	}

	private int[][] generateRandomPath() {
		int[][] paths = new int[76][];
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (int i = 2; i < CVRPData.getCoords().length; i++) {
			nodes.add(new Integer(i));
		}
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(1));
		Random rand = new Random();
		int pos = 0;
		while (CVRPData.pathIsValid(convertIntegers(path)) && nodes.size() > 0) {
			// Choose a random number from the ArrayList
			int getNodeVal = rand.nextInt(nodes.size());
			// System.out.println(getNodeVal);
			Integer i = nodes.get(getNodeVal);
			path.add(i);
			if (CVRPData.pathIsValid(convertIntegers(path))) {
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

		return paths;
	}

	private int[] swap(int a, int b, int[] chromosome) {
		// Find the position of a and b in each chromosome
		int aPos = 0, bPos = 0;
		for (int i = 0; i < chromosome.length; i++) {
			if (chromosome[i] == a)
				aPos = i;
			if (chromosome[i] == b)
				bPos = i;
		}
		// Swap the values
		int tmp = chromosome[aPos];
		chromosome[aPos] = chromosome[bPos];
		chromosome[bPos] = tmp;
		// Return the new chromosome
		return chromosome;
	}

	private void pmx(int[] indices) {
		// Create a new oldchromosomes object to store the current ones
		ArrayList<int[][]> oldChromosomes = new ArrayList<int[][]>();

		for (int j = 0; j < indices.length && j < 10; j++) {
			oldChromosomes.add(chromosomes.get(indices[j]));
		}
		// Reinitialise the chromosome list
		chromosomes = new ArrayList<int[][]>();
		// Remove the depot from each truck's path
		// ArrayList<int[][]> chromosomes = getChromosomes();

		// Add the shortest 2 from the previous chromosomes
		//chromosomes.add(oldChromosomes.get(0));
		//chromosomes.add(oldChromosomes.get(1));

		ArrayList<int[]> tmpPaths = new ArrayList<int[]>();

		for (int i = 0; i < oldChromosomes.size(); i++) {
			int[] strippedPath = CVRPData.stripDepots(oldChromosomes.get(i));
			tmpPaths.add(strippedPath);
			/*
			 * String s = "{"; for(int j = 0; j < strippedPath.length; j++){ s =
			 * s.concat(String.valueOf(strippedPath[j])); if(j !=
			 * strippedPath.length - 1) s = s.concat(","); } s = s.concat("}");
			 * System.out.println(s);
			 */
		}
		// crossover each of the 5 with each other to give 25
		Random rnd = new Random();

		for (int i = 0; i < tmpPaths.size(); i++) {
			for (int j = 0; j < tmpPaths.size(); j++) {
				// Cross the 2 indices over
				int start = rnd.nextInt() % (CVRPData.NUM_NODES - 2);
				int end = rnd.nextInt() % (CVRPData.NUM_NODES - 2);
				if (start < 0)
					start = start * -1;
				if (end < 0)
					end = end * -1;
				while (start == end)
					end = rnd.nextInt() & CVRPData.NUM_NODES - 1;
				if (start > end) {
					// We need to swap them
					int tmp = start;
					start = end;
					end = tmp;
				}

				int[] newPathA = tmpPaths.get(i);
				int[] newPathB = tmpPaths.get(j);

				// Between start and end we need to swap the values
				for (int k = start; k < end; k++) {
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
	}

	private int[] mutate(int[] path) {

		Random rnd = new Random();

		for (int i = 0; i < path.length; i++) {
			if (rnd.nextDouble() < (probability + (zeroDeltaIterations % 1000)
					* zeroDeltaAddition)) {
				// We are swapping i
				int swapWith = rnd.nextInt() % (CVRPData.NUM_NODES - 2) + 1;
				if (swapWith < 0)
					swapWith = swapWith * -1;
				path = swap(path[i], path[swapWith], path);
			}
		}

		return path;
	}

	private int[][] makeValidPaths(int[] inpath) {
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (int i = 0; i < inpath.length; i++) {
			nodes.add(new Integer(inpath[i]));
		}

		int[][] paths = new int[76][];

		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(1));
		int j = 0;
		int pos = 0;
		while (CVRPData.pathIsValid(convertIntegers(path)) && nodes.size() > 0) {
			Integer i = nodes.get(0);
			path.add(i);
			if (CVRPData.pathIsValid(convertIntegers(path))) {
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
	private double fitness(int[][] paths) {
		// We want to minimise the path weight

		double weight = 0.0;
		for (int[] path : paths) {
			if (path == null)
				return weight;
			weight += CVRPData.getPathDistance(path);
		}
		return weight;
	}

	@Override
	public String getName() {
		return "SimpleGA-" + (int)fitness(bestPath);
	}

}
