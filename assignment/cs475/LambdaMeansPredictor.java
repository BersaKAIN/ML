package cs475;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class LambdaMeansPredictor extends Predictor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783464646598201791L;
	
	double cluster_lambda = 0.0;
	int clustering_training_iterations;
	ArrayList <Integer> assignments = new ArrayList <Integer>(); // assignments.get(i) is the assignment for instance i.
	List <FeatureVector> clusters = new ArrayList<FeatureVector>();

	public LambdaMeansPredictor(List<Instance> instances) {
		if (CommandLineUtilities.hasArg("cluster_lambda")) {
		    cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");
		}
		clustering_training_iterations = 10;
		if (CommandLineUtilities.hasArg("clustering_training_iterations")) {
		    clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
		}
		// init the first cluster. 
		FeatureVector mean = new FeatureVector();
		int count = 0;
		for (Instance instance : instances) {
			for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
				if (mean.containIndex(entry.getKey())) {
					mean.add(entry.getKey(), mean.get(entry.getKey())+entry.getValue());
				}
				else {
					mean.add(entry.getKey(),entry.getValue());
				}
			}
			// init assignments
			assignments.add(0);
			count ++;
		}
		for (Entry<Integer, Double> entry : mean.getVector().entrySet()) {
			mean.add(entry.getKey(), entry.getValue()/count);
		}
		clusters.add(mean);		
		// init lambda if it is not specified by the user.
		if (cluster_lambda == 0.0 && instances.size() != 0) {
			for (Instance instance : instances) {
				cluster_lambda += MathUtilities.SquaredL2Distance(mean, instance);
			}
			cluster_lambda /= instances.size();
			System.out.println("Lambda value is:" + cluster_lambda);
		}		
	}
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		// init clusters
		for (int i = 0 ; i < clustering_training_iterations ; i++) {
			this.EStep(instances);
			this.Mstep(instances);
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		int feaIndex =0;
		int result = 0;
		double minDistance = Double.MAX_VALUE;
		for (FeatureVector cluster : clusters) {
			if (MathUtilities.SquaredL2Distance(cluster, instance) < minDistance) {
				minDistance = MathUtilities.SquaredL2Distance(cluster, instance);
				result = feaIndex;
			}
			feaIndex ++;
		}
		return new ClassificationLabel(result);
	}
	
	public void EStep(List<Instance> instances) {
		int insIndex = 0;
		for (Instance instance : instances) {
			// get the closest cluster.
			int feaIndex =0;
			double minDistance = Double.MAX_VALUE;
			for (FeatureVector cluster : clusters) {
				if (MathUtilities.SquaredL2Distance(cluster, instance) < minDistance) {
					minDistance = MathUtilities.SquaredL2Distance(cluster, instance);
					assignments.set(insIndex, feaIndex);
				}
				feaIndex ++;
			}
			// add a new cluster if needed.
			if (minDistance > cluster_lambda) {
				clusters.add(instance.getFeatureVector());
				assignments.set(insIndex, feaIndex);
			}
			insIndex ++;
		}
	}
	
	public void Mstep(List<Instance> instances) {
		int insIndex = 0;
		List <Double> counts = new ArrayList<Double>();
		List <FeatureVector> newClusters = new ArrayList<FeatureVector>();
		// init the counts and new clusters.
		for (int i = 0 ; i < clusters.size() ; i++ ) {
			counts.add(0.0);
			newClusters.add(new FeatureVector());
		}
		// accumulate new clusters
		for (Instance instance : instances) {
			int assignment = assignments.get(insIndex);
			newClusters.get(assignment).add(instance);
			counts.set(assignment, counts.get(assignment)+1.0);
			insIndex ++;
		}
		int feaIndex = 0;
		for (FeatureVector cluster : newClusters) {
			cluster.scalarDivide(counts.get(feaIndex));
			feaIndex ++;
		}
		this.clusters = newClusters;
	}

}
