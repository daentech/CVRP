package com.github.daentech;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.github.daentech.algorithms.SimpleGA;
import com.github.daentech.graphics.RouteVisualiser;
import com.github.daentech.graphics.TimeGraph;

public class CVRP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Run each algorithm and output the result distance
		
		// Run the best by default, but allow switching on commandline input
		
		// Output a graph file
		RouteVisualiser rv = new RouteVisualiser(true);
		rv.getNodeMap();
		/*int[][] paths = {
				{1,3,4,6,7,34,25,11,56,32,1},
				{1,5,2,22,43,70, 60,50,36,1},
				{1,8,9,10,12,13,14,15,17,1}, 
				{1, 33, 52, 19, 24, 71,1},
				{1, 47, 63, 20, 25, 51, 40, 49, 1}
		};
		rv.drawPaths(paths);
		rv.drawKey(paths);
		rv.saveImage("SimpleGA");*/
		
		SimpleGA sga = new SimpleGA();
		sga.randomise();
		int[][] paths = sga.getPaths();
		
		rv.drawPaths(paths);
		rv.drawKey(paths);
		rv.saveImage(sga.getName());
		
		TimeGraph tg = new TimeGraph(true);
		double[] weights = new double[1000];
		double[] weights2 = new double[1000];
		
		Random rnd = new Random();
		
		for(int i = 0; i < 1000; i++){
			weights[i] = rnd.nextDouble() * 500 + 600;
			weights2[i] = rnd.nextDouble() * 500 + 600;
		}
		
		Arrays.sort(weights);
		Arrays.sort(weights2);
		
		// Add results to the graph
		tg.addResults(weights, "SimpleGA");
		tg.addResults(weights2, "AdvancedGA");
		
		tg.render();
		
		tg.save("SimpleGA");
		

	}

}
