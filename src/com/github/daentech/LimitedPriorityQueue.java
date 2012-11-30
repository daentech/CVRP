package com.github.daentech;

import java.util.ArrayList;

public class LimitedPriorityQueue {

	private ArrayList<WeightLookup> values;
	private int size;
	
	public LimitedPriorityQueue(int size, Number sample){
		values = new ArrayList<WeightLookup>(size);
		this.size = size;
		if(sample instanceof Integer)
			for(int i = 0; i < size; i++)
				values.add(i, new WeightLookup(0, (Number) new Integer(Integer.MAX_VALUE)));
		else if (sample instanceof Double)
			for(int i = 0; i < size; i++)
				values.add(i, new WeightLookup(0, (Number) new Double(Integer.MAX_VALUE)));
		else if (sample instanceof Float)
			for(int i = 0; i < size; i++)
				values.add(i, new WeightLookup(0, (Number) new Float(Integer.MAX_VALUE)));
	}
	
	public boolean push(int index, Number newVal){
		for(int i = 0; i < values.size(); i++){
			if(lessThan(newVal,values.get(i).weight)){
				for(int j = values.size() - 1; j > i; j--){
					values.set(j, values.get(j - 1));
				}
				values.set(i, new WeightLookup(index, newVal));
				return true;
			}
		}
		return false;
	}
	
	public void printArray(){
		
		String s = "";
		for(int i = 0; i < values.size(); i++){
			s = s.concat("{" + String.valueOf(values.get(i).index) + ", " + String.valueOf(values.get(i).weight + "}"));
			if(i < values.size() - 1) s = s.concat(", ");
		}
		
		System.out.println(s);
	}
	
	public int[] getIndices(){
		int[] indices = new int[size];
		
		for(int i = 0; i < size; i++){
			indices[i] = values.get(i).index;
		}
		
		return indices;
	}
	
	private boolean lessThan(Number a, Number b){
		if(a instanceof Integer){
			if((Integer)a < (Integer)b) return true;
		} else if(a instanceof Double){
			if((Double)a < (Double)b) return true;
		} else if(a instanceof Float){
			if((Float)a < (Float)b) return true;
		}
		return false;
	}
	
	private class WeightLookup{
		public int index;
		public Number weight;
		
		public WeightLookup(int index, Number weight){
			this.index = index;
			this.weight = weight;
		}
	}
}
