package cs475;

import java.util.List;

public class MarginKernelPerceptron extends Predictor {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 2769904811188263481L;
	//	private Map<String, Double> gramMatrix = new HashMap<String, Double>();
	private double[][] gramMatrix;
	private double[] labels;
	private Kernel kernel;
	private double[] dualParam;
	private int iters = 5;
	private List<Instance> instances;
	
	public MarginKernelPerceptron(String kernelType) {
		if (kernelType.equals("perceptron_linear_kernel")) {
			kernel = new LinearKernel();
		}
		else if (kernelType.equals("perceptron_polynomial_kernel")) {
			int polynomial_kernel_exponent = 2;
			if (CommandLineUtilities.hasArg("polynomial_kernel_exponent")) {
				polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsInt("polynomial_kernel_exponent");
			}
			kernel = new PolynomialKernel(polynomial_kernel_exponent);
		}
		iters = 5;
		if (CommandLineUtilities.hasArg("online_training_iterations")) {
		    iters = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
		}
	}
	
	@Override
	public void train(List<Instance> instances) {
		// calculate the Gram Matrix.
		// java List maintains iterator order.
		this.instances = instances; 
		int index1 = 0;
		int index2 = 0;
		labels = new double[instances.size()];
		dualParam = new double[instances.size()];
		gramMatrix = new double[instances.size()][instances.size()];
		for (Instance i1 : instances) {
			labels[index1] = 2 * (Double.valueOf(i1.getLabel().toString()) - 0.5); // label values
			dualParam[index1] = 0; // alpha values
			index2 = 0;
			for (Instance i2 : instances) {
//				String key = String.valueOf(index1) + '_' + String.valueOf(index2);
//				gramMatrix.put(key, kernel.value(i1, i2));
				gramMatrix[index1][index2] = kernel.value(i1, i2);
				index2 ++;
			}
			index1 ++;
		}
		// start tuning param
		for (int iter = 0 ; iter < iters ; iter++) {
			for (int i = 0 ; i < instances.size() ; i++) {
				double yE = 0.0;
				for (int j = 0 ; j < instances.size() ; j++) {
					yE += dualParam[j] * labels[j] * gramMatrix[i][j];
				}
				if (yE * labels[i] < 1) {
					dualParam[i] += 1;
				}
			}
		}
	}

	@Override
	public Label predict(Instance instance) {
		int i = 0;
		double y = 0.0;
		for (Instance ins : instances) {
//			System.out.println("i is:" + i);
			y += dualParam[i] * labels[i] * kernel.value(ins, instance);
			i++;
		}
		if (y > 0) {
			return new ClassificationLabel(1);
		}
		return new ClassificationLabel(0);
	}
	
	

}
