package cs475;

import java.util.List;
import java.util.Map.Entry;

public class MarginPerceptron extends Predictor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2621825132119231892L;
	private FeatureVector linearParam;
	private double online_learning_rate;
	private int iters;
	
	public MarginPerceptron() {
		linearParam = new FeatureVector();
		online_learning_rate = 1.0;
		if (CommandLineUtilities.hasArg("online_learning_rate")) {
			online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");
		}
		iters = 5;
		if (CommandLineUtilities.hasArg("online_training_iterations")) {
		    iters = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
		}
	}
	
	@Override
	public void train(List<Instance> instances) {
		// initialize linearParam
		for (Instance instance : instances) {
			for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
				linearParam.add(entry.getKey(), 0.0);
			}
		}
		
		int iter = 0;
		while (iter < iters) {
			iter ++;
			for (Instance instance : instances) {
				double y = 2 * (Double.valueOf(instance.getLabel().toString()) - 0.5);
				if (MathUtilities.innerProduct(linearParam, instance)* y < 1) {
					for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
						linearParam.add(entry.getKey(), linearParam.get(entry.getKey()) + online_learning_rate * entry.getValue() * y);
					}
				}
			}
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		if (MathUtilities.innerProduct(linearParam, instance) > 0) {
			return new ClassificationLabel(1);
		}
		return new ClassificationLabel(0);
	}

}
