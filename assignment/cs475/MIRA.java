package cs475;

import java.util.List;
import java.util.Map.Entry;

public class MIRA extends Predictor {
	
	private FeatureVector linearParam;
	private int iters = 5;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6259830288357965808L;

	
	public MIRA() {
		linearParam = new FeatureVector();
		iters = 5;
		if (CommandLineUtilities.hasArg("online_training_iterations")) {
		    iters = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
		}
	}
	@Override
	public void train(List<Instance> instances) {
		// initialize w values
		for (Instance instance : instances) {
			for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
				linearParam.add(entry.getKey(), 0.0);
			}
		}
		
		int iter = 0;
		while (iter < iters) {
			iter ++;
			for (Instance instance : instances) {
				double y = Double.valueOf(instance.getLabel().toString()) * 2.0 - 1.0; // get label
				double loss = y * MathUtilities.innerProduct(linearParam, instance);
				if ( loss < 1) {
					double tau = (1.0 -loss) / MathUtilities.innerProduct(instance.getFeatureVector(), instance);
					for (Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
						int key = entry.getKey();
						linearParam.add(key, linearParam.get(key) + tau * y * instance.getFeatureVector().get(key));
					}
				}
			}
		}
		
		
	}

	@Override
	public Label predict(Instance instance) {
		if (MathUtilities.innerProduct(linearParam, instance) > 0) {
			return new ClassificationLabel(1);
		}
		return new ClassificationLabel(0);
	}

}
