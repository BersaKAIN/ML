package cs475;

import java.io.Serializable;

public class RegressionLabel extends Label implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3323563814085472700L;
	private double label;

	public RegressionLabel(double label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return Double.toString(label);
	}

}
