package cs475;

public class PolynomialKernel implements Kernel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6289275240421270590L;
	private int kernelExponent;
	
	public PolynomialKernel(int kernelExponent) {
		this.kernelExponent = kernelExponent;
	}
	
	@Override
	public double value(Instance i1, Instance i2) {
		return Math.pow(MathUtilities.innerProduct(i1.getFeatureVector(),i2)+1.0, kernelExponent);
	}
}
