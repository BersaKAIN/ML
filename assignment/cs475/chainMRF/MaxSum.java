package cs475.chainMRF;

public class MaxSum {

	private ChainMRFPotentials potentials;
	private int[] assignments;
	// add whatever data structures needed

	public MaxSum(ChainMRFPotentials p) {
		this.potentials = p;
		assignments = new int[p.chainLength()+1];
	}
	
	public int[] getAssignments() {
		return assignments;
	}

	public double maxProbability(int x_i) {
		int n = potentials.chainLength();
		int k = potentials.numXValues();
		
		double best = Double.NEGATIVE_INFINITY;
		double current = 0;
		for (int v = 1 ; v <= k ; v++) {
			current = messageFactor2Node(n,n,v) + messageFactor2Node(2*n-1,n,v);
			if (current > best) {
				best = current;
				assignments[n] = v;
			}
		}

		// normalize
		SumProduct sp = new SumProduct(potentials);
		sp.marginalProbability(1);
			
		return best - Math.log(sp.sum);
	}
	
	public double messageNode2Factor(int factor, int node, int value) {
		double result = 0;
//		System.out.println("factor:" + factor + "   node:" + node);
		
		result += messageFactor2Node(node, node, value);
		if (factor == potentials.chainLength() + node && factor > potentials.chainLength() + 1) {
			// right factor  f' -> n -> f && this is not the second node
			result += messageFactor2Node(factor-1, node, value);
		}
		else if (factor == potentials.chainLength() + node -1 && factor <= 2 * potentials.chainLength() - 2) {
			// left factor f <- n <- f'
			result += messageFactor2Node(factor+1, node, value);
		}

		return result;
	}
	
	public double messageFactor2Node(int factor, int node, int value) {
		double best = Double.NEGATIVE_INFINITY;
		double current = 0;

		if (factor == node) {
			return  Math.log(potentials.potential(node, value));
		}
		else if (factor == potentials.chainLength() + node -1) {
			// left factor n' -> f -> n
			for (int v = 1; v <= potentials.numXValues(); v++) {
				current = Math.log(potentials.potential(factor, v, value)) + messageNode2Factor(factor, node-1, v);
				if (current > best) {
					best = current;
					assignments[node-1] = v;
				}
			}
		}
		else if (factor == potentials.chainLength() + node) {
			// left factor n <- f <- n'
			for (int v = 1; v <= potentials.numXValues(); v++) {
				current = Math.log(potentials.potential(factor, value, v)) + messageNode2Factor(factor, node+1, v);
				if (current > best) {
					best = current;
					assignments[node+1] = v;
				}
			}
		}
		else {
			// they are not connected
			throw new RuntimeException("wrong assignment");
		}
		
		return best;
	}
}
