package cs475;

import java.util.HashSet;
import java.util.Set;

public class MathUtilities {
	public static double innerProduct(FeatureVector linearParam, Instance instance) {
		double sum = 0;
		for (int index : linearParam.getVector().keySet()) {
			if (instance.getFeatureVector().getVector().containsKey(index)) {
				sum += instance.getFeatureVector().get(index) * linearParam.get(index);
			}
		}
		return sum;
	}
	
	public static double SquaredL2Distance(FeatureVector linearParam, Instance instance) {
		double result = 0.0;
		// take the union of the keyset
		Set<Integer> keyUnion = new HashSet<Integer>();
		keyUnion.addAll(linearParam.getVector().keySet());
		keyUnion.addAll(instance.getFeatureVector().getVector().keySet());
		
		for (Integer key : keyUnion) {
			double v1 = 0.0;
			double v2 = 0.0;
			if (linearParam.getVector().containsKey(key)) {
				v1 = linearParam.get(key); 
			}
			if (instance.getFeatureVector().containIndex(key)) {
				v2 = instance.get(key);
			}
			result += (v1-v2)*(v1-v2);
		}
		return result;
	}
	
}
