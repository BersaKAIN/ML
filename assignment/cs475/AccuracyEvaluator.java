package cs475;

import java.util.List;

public class AccuracyEvaluator extends Evaluator{

	@Override
	public double evaluate(List<Instance> instances, Predictor predictor) {
		// TODO Auto-generated method stub
		double correct = 0.0;
		double total = 0.0;
		for (Instance instance : instances) {
			total += 1.0;
			if (!(instance.getLabel() == null) && predictor.predict(instance).toString().equals(instance.getLabel().toString())) {
				correct += 1.0;
			}
		}
		try {
			return correct/total;
		}
		catch(Exception e) {
			System.out.println("there is no instance in the list.");
			throw e;
		}
	}

}
