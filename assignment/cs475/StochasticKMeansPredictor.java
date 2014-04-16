package cs475;

import java.util.ArrayList;
import java.util.List;

public class StochasticKMeansPredictor extends Predictor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4270719188741625041L;
	
	int num_clusters;
	int clustering_training_iterations;
	ArrayList <Integer> assignments = new ArrayList <Integer>(); // assignments.get(i) is the assignment for instance i.
	List <Cluster> clusters = new ArrayList<Cluster>();
	
	public StochasticKMeansPredictor(List<Instance> instances) {
		
		num_clusters = 3;
		if (CommandLineUtilities.hasArg("num_clusters")) {
		    num_clusters = CommandLineUtilities.getOptionValueAsInt("num_clusters");
		}
		// init clusters
		for (int i = 0 ; i < num_clusters ; i++) {
			clusters.add(new Cluster(instances.get(i).getFeatureVector()));
		}
		
		clustering_training_iterations = 10;
		if (CommandLineUtilities.hasArg("clustering_training_iterations")) {
		    clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
		}
	}
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < clustering_training_iterations ; i++) {
			int insIndex = 0;
			ArrayList <Integer> newAssignments = new ArrayList <Integer>();

			for (Instance instance : instances) {
				// get the closest cluster.
				int feaIndex =0;
				double minDistance = Double.MAX_VALUE;
				newAssignments.add(-1);
				for (Cluster cluster : clusters) {
					if (MathUtilities.SquaredL2Distance(cluster.getFeatureVector(), instance) < minDistance) {
						minDistance = MathUtilities.SquaredL2Distance(cluster.getFeatureVector(), instance);						
						
						newAssignments.set(insIndex, feaIndex);
					}
					feaIndex ++;
				}
				
				// update the clusters related to the assignment of the instance if assignment is changed.
				feaIndex = newAssignments.get(insIndex);
				
				if (i == 0) {
					clusters.get(feaIndex).addMember(instance);
				}
				else if (feaIndex != assignments.get(insIndex)) {
					clusters.get(feaIndex).addMember(instance);
					clusters.get(assignments.get(insIndex)).removeMember(instance);
				}
				insIndex ++;
			}
			assignments = newAssignments;
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		
		int feaIndex =0;
		int result = -1;
		double minDistance = Double.MAX_VALUE;
		for (Cluster cluster : clusters) {
			double dis = MathUtilities.SquaredL2Distance(cluster.getFeatureVector(), instance);
			if (dis < minDistance) {
				minDistance = dis;
				result = feaIndex;
			}
			feaIndex ++;
		}
		return new ClassificationLabel(result);	}
}
