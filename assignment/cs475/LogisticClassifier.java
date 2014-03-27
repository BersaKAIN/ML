package cs475;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogisticClassifier extends Predictor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8710589451358642726L;
	private int num_of_features;
	private FeatureVector linearParam;
	private int gd_iteration;
	private double gd_eta;
	
	public LogisticClassifier() {
		// option value for gradient descent
		gd_iteration = 20;
		if (CommandLineUtilities.hasArg("gd_iterations"))
		    gd_iteration = CommandLineUtilities.getOptionValueAsInt("gd_iterations");
		gd_eta = .01;
		if (CommandLineUtilities.hasArg("gd_eta"))
		    gd_eta = CommandLineUtilities.getOptionValueAsFloat("gd_eta");
		num_of_features = -1;
		if (CommandLineUtilities.hasArg("num_features_to_select"))
		    num_of_features = CommandLineUtilities.getOptionValueAsInt("num_features_to_select");
		linearParam = new FeatureVector();
	}
	
	@SuppressWarnings({ "unchecked",  "rawtypes" })
	@Override
	public void train(List<Instance> instances) {
		// select feature
//		boolean isDiscrete = true;
//		for (Map.Entry<Integer, Double> entry : instances.get(0).getFeatureVector().getVector().entrySet()) {
////			System.out.println(entry.getValue().intValue());
////			System.out.println(entry.getValue());
//			if (entry.getValue().toString().contains(".")) {
//				isDiscrete = false;
//				break;
//			}
//		}
		if (num_of_features == -1) {
			// we pick every single feature
			for (Instance instance : instances) {
				for (Map.Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
					linearParam.add(entry.getKey(), 0);
				}
			}
		} 
//		else {
//			// for discrete features
//			// initialize all features.
//			// we select the best features according to information gain
//			int countSample = 0; // num of data points
//			Map<Integer, Integer> featureCount= new HashMap<Integer, Integer>(); // count the feature and feature value of training data
//			Map<String, Integer> featureValueCount = new HashMap<String, Integer>();
//			Map<String, Integer> featureValueLabelCount = new HashMap<String, Integer>();
//			Map<Integer, Double> IG = new HashMap<Integer, Double>();
//			Map<Integer, Set<Integer>> fv_enum = new HashMap<Integer, Set<Integer>>();
//			for (Instance instance : instances) {
//				String y = instance.getLabel().toString();
//				countSample ++;
//				for (Map.Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
//					try {
//						featureCount.put(entry.getKey(), featureCount.get(entry.getKey()) + 1);
//					}
//					catch (Exception e) {
//						featureCount.put(entry.getKey(), 1);
//					}
//					String fv = entry.getKey().toString() +' '+ entry.getValue().intValue();
//					String fvy = fv + ' ' + y;
////					System.out.println(fvy);
//					try {
//						fv_enum.get(entry.getKey()).add(entry.getValue().intValue());
//						//System.out.println(fv_enum.get(entry.getKey()).size());
//					} catch (Exception e) {
//						fv_enum.put(entry.getKey(), new HashSet<Integer>());
//						fv_enum.get(entry.getKey()).add(entry.getValue().intValue());
//					}
//					
//					try {
//						featureValueCount.put(fv,featureValueCount.get(fv) + 1);
//					}
//					catch (Exception e) {
//						featureValueCount.put(fv,1);
//					}
//					
//					try {
//						featureValueLabelCount.put(fvy, featureValueLabelCount.get(fvy)+1);
//					}
//					catch (Exception e) {
//						featureValueLabelCount.put(fvy,1);
//					}
//				}
//			}
//			
//			// to compute information gain for a single feature:
//			for (Integer f : featureCount.keySet()) {
//				IG.put(f, 0.0);
//				for (Integer v : fv_enum.get(f)) {
//					String fv = f.toString() + ' ' + v.toString();
//					for (Integer y = 0 ; y <=1 ; y++) {
//						String fvy = f.toString() + ' ' + v.toString() + ' ' + y.toString();
////						System.out.println(fvy);
//						double  Pxy = 0;
//						double Px = 0;
//						try {
//							Pxy = (double) featureValueLabelCount.get(fvy) / countSample;
//							Px = (double) featureValueCount.get(fv) / countSample;
////							System.out.println("Pxy is: "+ Pxy);
////							System.out.println("Px is: "+ Px);
////							System.out.println("Feature " + f +"has ig:  "+ IG.get(f));
//						}
//						catch (Exception e) {
//							//IG.put(f, Double.NEGATIVE_INFINITY);
//						}
//						IG.put(f, IG.get(f) - Pxy * Math.log(Pxy/Px));
//					}
//				}
//			}
//			List<Map.Entry<Integer, Double>> a = new ArrayList<Map.Entry<Integer, Double>>(IG.entrySet());
//			Collections.sort(a,
//			         new Comparator() {
//			             public int compare(Object o1, Object o2) {
//			                 Map.Entry e1 = (Map.Entry) o1;
//			                 Map.Entry e2 = (Map.Entry) o2;
//			                 return ((Comparable) e1.getValue()).compareTo(e2.getValue());
//			             }
//			         });
//			for (int i = 0 ; i < num_of_features && i < a.size(); i++) {
//				System.out.println("Feature " + a.get(i).getKey() + " has weight:  " + a.get(i).getValue());
//				linearParam.add(a.get(i).getKey(),0);
//			}
//		}
		else {
			// continuous features
			FeatureVector cf = new FeatureVector();
			Map<Integer, Double> IG = new HashMap<Integer, Double>();
			for (Instance instance : instances) {
				for (Map.Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()) {
					cf.add(entry.getKey(), 0);
				}
			}
			
			for (Integer key : cf.getVector().keySet()) {
				double featureCount = 0.0;
				double numCount = 0.0;
				double hasFeatureCount = 0.0;
				double b0 = 0.0; // bigger than average and has label 0
				double b1 = 0.0;
				double s0 = 0.0; // smaller than average and has label 0
				double s1 = 0.0;
				double n0 = 0.0; // does not have label and has label 0
				double n1 = 0.0;
				for (Instance instance : instances) {
					numCount ++;
					if (instance.getFeatureVector().containIndex(key)) {
//						hasFeatureCount += 1;
						featureCount += instance.getFeatureVector().get(key);
					}
				}
//				double avg = featureCount / hasFeatureCount;
				double avg = featureCount / numCount;
				for (Instance instance : instances) {
					if (!instance.getFeatureVector().containIndex(key)) {
						if (instance.getLabel().toString().equals("1")) {
							if (0 >= avg) {
								b1 += 1;
							} else {
								s1 += 1;
							}
						}
						else {
							if (0 >= avg) {
								b0 += 1;
							} else {
								s0 += 1;
							}						}
					}
					else if (instance.getFeatureVector().get(key) >= avg) {
						if (instance.getLabel().toString().equals("1")) {
							b1 += 1;
						}
						else {
							b0 += 1;
						}
					}
					else {
						if (instance.getLabel().toString().equals("1")) {
							s1 += 1;
						}
						else {
							s0 += 1;
						}
					}
				}
				double value = 0.0;
				value = +b1/numCount * Math.log(b1/(b1 + b0))
						+b0/numCount * Math.log(b0/(b1 + b0))
						+s1/numCount * Math.log(s1/(s1 + s0))
						+s0/numCount * Math.log(s0/(s1 + s0));

						 	  
				IG.put(key, -1 * value);
			}
			List<Map.Entry<Integer, Double>> a = new ArrayList<Map.Entry<Integer, Double>>(IG.entrySet());
			Collections.sort(a,
			         new Comparator() {
			             public int compare(Object o1, Object o2) {
			                 Map.Entry e1 = (Map.Entry) o1;
			                 Map.Entry e2 = (Map.Entry) o2;
			                 return ((Comparable) e1.getValue()).compareTo(e2.getValue());
			             }
			         });
			for (int i = 0 ; i < num_of_features ; i++) {
				if (a.size() <= i) {
					break;
				}
				linearParam.add(a.get(i).getKey(),0);
//				System.out.println("Feature " + a.get(i).getKey() + " has weight:  " + a.get(i).getValue());

			}
		}
		System.out.println("Feature Selection Done! ");
		
		// train params
		for (int i = 0 ; i < gd_iteration; i++) {
			this.updateParam(instances);
		}
	}

	@Override
	public Label predict(Instance instance) {
		if (linkFunction(this.innerProduct(instance)) >= 0.5) {
			return new ClassificationLabel(1);
		}
		return new ClassificationLabel(0);
	}
	
	public static double linkFunction(double z) {
		return 1.0 / (1.0 + Math.exp(-z));
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
	
	private void updateParam(List<Instance> instances) {
		Map<Integer, Double> update = new HashMap<Integer, Double>();
		for (Instance instance : instances) {
			double y = Double.parseDouble(instance.getLabel().toString()) ; //yi
			double wx = this.innerProduct(instance);
			for (Map.Entry<Integer, Double> entry : instance.getFeatureVector().getVector().entrySet()){
				int key = entry.getKey(); //j
				if (!this.linearParam.containIndex(key)) {
					continue;
				}
				if (!update.containsKey(key)) {
					update.put(key, 0.0);
				}
				double value = entry.getValue(); //xij
				double updatedValue = update.get(key) + y * linkFunction(-1 * wx) * value 
						- (1-y) * linkFunction(wx) * value;
				update.put(key, updatedValue);
			}			
		}
		for (Map.Entry<Integer, Double> entry : linearParam.getVector().entrySet()) {
			entry.setValue(entry.getValue() + gd_eta *update.get(entry.getKey()));
		}
	}
}
