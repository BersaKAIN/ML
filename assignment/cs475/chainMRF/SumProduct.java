
package cs475.chainMRF;

public class SumProduct {

	private ChainMRFPotentials potentials;
	// add whatever data structures needed
	// may need variables to store for DP
	double sum = 0;

	public SumProduct(ChainMRFPotentials p) {
		this.potentials = p;
	}

	public double[] marginalProbability(int x_i) {
		int n = potentials.chainLength();
		int k = potentials.numXValues();
		
		double[] result = new double[k+1];
		sum = 0;
		result[0] = 0;
				
		for (int v =1 ; v <= k ; v++) {
			result[v] = messageFactor2Node(x_i, x_i, v);
			if (x_i != 1) {
				// not the first one
				result[v] *= messageFactor2Node(n+x_i -1, x_i, v);
			}
			if (x_i != n) {
				result[v] *= messageFactor2Node(n+x_i, x_i, v);
				// not the last one
			}
			sum += result[v];
		}	
		// normalization
		for (int v =1 ; v <= k ; v++) {
			result[v] /= sum;
		}
		return result;
	}
	
	public double messageNode2Factor(int factor, int node, int value) {
		double result = 1.0;
		
		result *= messageFactor2Node(node, node, value);
		if (factor == potentials.chainLength() + node && factor > potentials.chainLength() + 1) {
			// right factor  f' -> n -> f && this is not the second node
			result *= messageFactor2Node(factor-1, node, value);
		}
		else if (factor == potentials.chainLength() + node -1 && factor <= 2 * potentials.chainLength() - 2) {
			// left factor f <- n <- f'
			result *= messageFactor2Node(factor+1, node, value);
		}
		else {
			// they are not connected
			return result;
		}
		return result;
	}
	
	public double messageFactor2Node(int factor, int node, int value) {
		double result = 0;
		if (factor == node) {
			return potentials.potential(node, value);
		}
		else if (factor == potentials.chainLength() + node -1) {
			// left factor n' -> f -> n
			for (int v = 1; v <= potentials.numXValues(); v++) {
				result += potentials.potential(factor, v, value) * messageNode2Factor(factor, node-1, v);
			}
		}
		else if (factor == potentials.chainLength() + node) {
			// left factor n <- f <- n'
			for (int v = 1; v <= potentials.numXValues(); v++) {
				result += potentials.potential(factor, value, v) * messageNode2Factor(factor, node+1, v);
			}
		}
		else {
			// they are not connected
			return 1;
		}
		
		return result;
	}

}

