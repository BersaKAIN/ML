package cs475;

import java.util.List;

public class EvenOddClassifier extends Predictor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6477453900132944328L;

	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		double sumOdd = 0;
		double sumEven = 0;
		for (Integer index: instance.getFeatureVector().getVector().keySet()) {
			if (index % 2 == 0) {
				sumOdd += instance.getFeatureVector().get(index);
			}
			else if (index % 2 == 1) {
				sumEven += instance.getFeatureVector().get(index);
			}
		}
		if (sumOdd >= sumEven) {
			return new ClassificationLabel(1);
		}
		else {
			return new ClassificationLabel(0);
		}
	}
}
