package cs475;

public class LinearKernel implements Kernel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5206627524813698904L;

	@Override
	public double value(Instance i1, Instance i2) {
		return MathUtilities.innerProduct(i1.getFeatureVector(), i2);
	}
}
