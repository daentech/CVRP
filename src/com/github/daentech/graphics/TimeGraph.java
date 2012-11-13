/******************************************************************************
 * 
 * Time Graph class.
 * 
 * Writes an image for the data over time. Works on a series of input arrays
 * to output a graph with multiple different algorithms included.
 * 
 * Also generates a key including the title of the algorithm type
 * 
 *****************************************************************************/

package com.github.daentech.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TimeGraph {
	
	private BufferedImage b;
	private Graphics2D g;
	private boolean withKey;
	private int width, height = 770;
	int bottomLeftX = 60, bottomLeftY = height - 60;
	int topRightX = 740, topRightY = 60;
	
	private ArrayList<ResultSet> results = new ArrayList<ResultSet>();
	
	// Initialises the graphics context for saving the image
	public TimeGraph(boolean withKey){
		this.withKey = withKey;
		
		width = withKey ? 920 : 770;
		
		b = new BufferedImage( width ,height,BufferedImage.TYPE_INT_RGB);
		g = b.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.black);
		
		String title = "Algorithm Comparison of Fitness over Time";
		FontMetrics fm = g.getFontMetrics();
	    int x = (width - fm.stringWidth(title)) / 2;
		g.drawChars(title.toCharArray(), 0, title.length(), x, 20);
	}
	
	// Add an array of results over time to the graph
	public void addResults(double[] weights, String name){
		results.add(new ResultSet(weights, name));
	}
	
	// Draws the axes from some lower bound to some upper bound of weight
	public void drawAxes(int iterations, double maxWeight, double minWeight){
		g.setColor(Color.black);
		g.drawLine(bottomLeftX, bottomLeftY, width - (withKey ? 160 : 60), bottomLeftY); // Draw the x-axis
		g.drawLine(bottomLeftX, bottomLeftY, bottomLeftX, topRightY);
		
		// Now need to put values on the axes
		for(int i = 0; i <= 10; i++){
			
			// Output the x values on the x-axis
			int xval = (int)(((double)iterations / (double)10) * i);
			String xstring = String.valueOf(xval);
			g.drawChars(xstring.toCharArray(), 0, xstring.length(), bottomLeftX + (i * (topRightX - bottomLeftX) / 10), height - 40);
			
			// Output the y vlues on the y-axis
			double yval = ((maxWeight - minWeight) / 10 * i) + minWeight;
			String ystring = String.format("%1$,.0f", yval);
			FontMetrics fm = g.getFontMetrics();
			g.drawChars(ystring.toCharArray(), 0, ystring.length(), 55 - fm.stringWidth(ystring), bottomLeftY - i * (bottomLeftY - topRightY) / 10 + 5);
		}
	}
	
	
	// Renders each of the lines passed in
	public void render(){
		// If we require the key, add the key to the graph
		if(withKey){
			// Add the box to put the key in
			g.setColor(Color.black);
			g.drawRect(topRightX + 20, (height - (results.size() + 1) * 30) /2, width - topRightX - 40, (results.size() + 1) * 20);
			
			// Add the title
			String keyTitle = "Algorithm";
			g.drawChars(keyTitle.toCharArray(), 0, keyTitle.length(), topRightX + 30, (height - (results.size() + 1) * 30) /2 + 15);
		}
		// Draw the axes based on the highest values of weight (y) over iterations (x)
		int iterations = 0;
		double maxWeight = 0, minWeight = Double.MAX_VALUE;
		
		for(ResultSet result : results){
			iterations = Math.max(iterations, result.getWeights().length);
			maxWeight = Math.max(maxWeight, result.getMaxDistance());
			minWeight = Math.min(minWeight,  result.getMinDistance());
		}
		
		// Axes go to the values of iterations along the x and maxTotalWeight along the 
		drawAxes(iterations, maxWeight, minWeight);
		
		int colorOffset = 0;
		
		// Loop over the results and using the graph edge values, work out position
		for(ResultSet result : results){
			// Draw a line for each result
			double[] weights = result.getWeights();
			for(int i = 0; i < weights.length - 1; i++){
				int startx = bottomLeftX + (int)(i * (((double)topRightX - (double)bottomLeftX) / (double)iterations));
				int starty = bottomLeftY - (int)((weights[i] - minWeight) * (((double)bottomLeftY - (double)topRightY) / (maxWeight - minWeight))) ;
				int endx = bottomLeftX + (int)((i+1) * ((double)topRightX - (double)bottomLeftX) / (double)iterations);
				int endy = bottomLeftY - (int)((weights[i + 1] - minWeight) * (((double)bottomLeftY - (double)topRightY) / (maxWeight - minWeight))) ;
				
				// Draw the calculated values
				//g.setColor(Color.darkGray);
				//g.drawOval(startx, starty, 3, 3);
				g.setColor(Color.getHSBColor((float)((0.23 * colorOffset + 1.0) % 1.0), 0.8f, 1.0f));
				g.drawLine(startx, starty, endx, endy);
			}
			
			// if using a key, add the algorithm name to the key
			if(withKey){
				g.drawLine(topRightX + 30, (int)((height - (results.size() + 1) * 30) /2 + ((colorOffset + 1.5) * 20) - 5), topRightX + 60, (int)((height - (results.size() + 1) * 30) /2 + ((colorOffset + 1.5) * 20) - 5));
				g.setColor(Color.black);
				g.drawChars(result.getName().toCharArray(), 0, result.getName().length(), topRightX + 70, (int)((height - (results.size() + 1) * 30) /2 + ((colorOffset + 1.5) * 20)));
			}
			
			colorOffset++;
		}
		
	}
	
	public void save(String filename){
		try{ImageIO.write(b,"png",new File(filename + "-results.png"));}catch (Exception e) {}
	}
	
	private class ResultSet{
		double[] weights;
		String name;
		
		public ResultSet(double[] weights, String name){
			this.weights = weights;
			this.name = name;
		}
		
		public double[] getWeights(){ return this.weights; }
		public String getName(){ return this.name; }
		
		public double getMaxDistance(){
			double distance = 0;
			for(double weight : weights){ distance = Math.max(weight, distance); }
			return distance;
		}
		
		public double getMinDistance(){
			double distance = Double.MAX_VALUE;
			for(double weight : weights){ distance = Math.min(weight, distance); }
			return distance;
		}
	}

}
