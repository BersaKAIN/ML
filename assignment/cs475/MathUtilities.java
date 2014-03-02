package cs475;

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
}
