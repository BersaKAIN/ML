package cs475;

import java.io.Serializable;
import java.util.HashMap;

public class FeatureVector implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817908880311125195L;
	private HashMap<Integer, Double> data;
	
	public FeatureVector() {
		this.data = new HashMap<Integer, Double>();
	}

	public void add(int index, double value) {
		data.put(index, value);
	}
	
	public double get(int index) {
		if (data.containsKey(index)) {
			return data.get(index);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	public HashMap<Integer, Double> getVector() {
		return data;
	}

}
