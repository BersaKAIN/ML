package cs475;

import java.util.HashMap;
import java.util.List;

public class MajorityClassifier extends Predictor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7547643452051682487L;
	private HashMap<String, Integer> counter;
	private Label majority;
	private int max;
//	private DefaultedMap counter;
	
	public MajorityClassifier() {
		counter = new HashMap<String, Integer>();
		majority = new ClassificationLabel(-1);
		max = 0;
//		counter = new DefaultedMap(_counter, 0);
	}
	
	@Override
	public void train(List<Instance> instances) {
		// check if the instance is classfier instance
		
		for (Instance instance : instances) {
			String label = instance.getLabel().toString();
			if (counter.containsKey(label)) {
				counter.put(label, counter.get(label)+1);
			}
			else {
				counter.put(label, 1);
			}
		}
		max = 0;
		for (String key : counter.keySet()) {
			if (counter.get(key) > max) {
				max = counter.get(key);
				majority = new ClassificationLabel(Integer.valueOf(key));
			}
		}
	}

	@Override
	public Label predict(Instance instance) {
		return majority;
	}
}
