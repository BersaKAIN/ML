package cs475;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FeatureVector implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817908880311125195L;
	private Map<Integer, Double> data;
	
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
	
	public boolean containIndex(int index) {
		if (data.containsKey(index)) {
			return true;
		}
		return false;
	}
	
	public Map<Integer, Double> getVector() {
		return data;
	}
	
	public void add(Instance instance) {
		for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
			int index = entry.getKey();
			if (this.containIndex(index)) {
				this.add(index, this.get(index) + entry.getValue());
			}
			else {
				this.add(index, entry.getValue());
			}
		}
	}
	
	public void scalarDivide(double Denominator) {
		for (Entry<Integer, Double> entry : this.getVector().entrySet()) {
			entry.setValue(entry.getValue()/Denominator);
		}
	}

}
