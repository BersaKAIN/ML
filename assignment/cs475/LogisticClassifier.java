package cs475;

import java.util.List;

public class LogisticClassifier extends Predictor{
	private int num_of_features;
	private FeatureVector linearParam;
	private int gd_iteration;
	private double gd_eta;
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < gd_iteration; i++) {
			for (int index : linearParam.getVector().keySet()) {
				linearParam.add(index, gd_eta * partialDerivative(instances, index) + linearParam.get(index));
			}
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static double linkFunction(double z) {
		return 1 / 1 + Math.exp(-z);
	}
	
	private double innerProduct(Instance instance) {
		double sum = 0;
		for (int index : linearParam.getVector().keySet()) {
			if (instance.getFeatureVector().getVector().containsKey(index)) {
				sum += instance.getFeatureVector().get(index) * linearParam.get(index);
			}
		}
		return sum;
	}
	
	private double partialDerivative(List<Instance> instances, int index) {
		double result = 0;
		for (Instance instance : instances) {
			if (!instance.getFeatureVector().containIndex(index)) {
				continue;
			}
			int y = Integer.parseInt(instance.getLabel().toString());
			
			result += y * linkFunction(-1 * innerProduct(instance)) * instance.getFeatureVector().get(index) 
					- (1-y) * linkFunction(innerProduct(instance)) * instance.getFeatureVector().get(index);
		}
		return result;
	}
}
