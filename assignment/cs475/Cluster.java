package cs475;

import java.io.Serializable;
import java.util.Map.Entry;

public class Cluster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4918691590852062628L;
	private FeatureVector featureVector;
	private int numOfMembers;
	
	public Cluster(FeatureVector fv) {
		featureVector = new FeatureVector();
		for (Entry<Integer, Double> entry : fv.getVector().entrySet()) {
			featureVector.add(entry.getKey(), entry.getValue());
		}
		numOfMembers = 0;
	}
	
	public FeatureVector getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(FeatureVector featureVector) {
		this.featureVector = featureVector;
	}

	public int getNumOfMembers() {
		return numOfMembers;
	}

	public void setNumOfMembers(int numOfMembers) {
		this.numOfMembers = numOfMembers;
	}

	public void addMember(Instance instance) {
		if (numOfMembers == 0) {
			featureVector = new FeatureVector();
			for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
				featureVector.add(entry.getKey(), entry.getValue());
			}			
			numOfMembers = 1;
		}
		else {
			this.featureVector.scalarDivide(1.0/numOfMembers);
			this.featureVector.add(instance);
			numOfMembers ++;
			this.featureVector.scalarDivide(numOfMembers);
		}
	}
	
	public void removeMember(Instance instance) {
		if (numOfMembers == 1) {
			featureVector = new FeatureVector();
			numOfMembers = 0;
		}
		else {
			this.featureVector.scalarDivide(-1.0/numOfMembers);
			this.featureVector.add(instance);
			this.featureVector.scalarDivide(-1.0);
			numOfMembers -= 1;
			this.featureVector.scalarDivide(numOfMembers);
		}
	}

}
