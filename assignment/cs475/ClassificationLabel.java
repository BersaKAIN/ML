package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3107886260106912830L;
	private int label; 
	
	public ClassificationLabel(int label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return Integer.toString(label);
	}
}
