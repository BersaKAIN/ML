package cs475.RBM;
import cs475.MathUtilities;
import cs475.RBM.*;

import java.util.Random;

public class RBMEnergy {
	private RBMParameters _parameters;
	private int numSamples;
	private Random random;
	private int[][] generatedVisibles;
	private int[][] generatedHiddens;
	
	// TODO: Add the required data structures and methods.

	public RBMEnergy(RBMParameters parameters, int numSamples) {
		this._parameters = parameters;
		this.numSamples = numSamples;
		random = new Random(0L);
		int n = this._parameters.numHiddenNodes();
		int m = this._parameters.numVisibleNodes();
		generatedVisibles = new int[numSamples][m];
		generatedHiddens = new int[numSamples][n];
		
	}
	
	public double computeMarginal() {
		// TODO: Add code here
		// generate samples
		for (int i = 0; i < numSamples; i++) {
			// generateHidden
			for (int j = 0; j < _parameters.numHiddenNodes(); j++) { 
				generatedHiddens[i][j] = generateH(i,j);
			}
			// generateVisibles
			for (int j = 0; j < _parameters.numVisibleNodes(); j++) {
				generatedVisibles[i][j] = generateX(i,j);
			}
		}
		
		// count the number of samples that is exactly the same as the visibles
		double count = 0.0;
		for (int i = 0; i < numSamples; i++) {
			boolean correct = true;
			for (int j = 0; j < _parameters.numVisibleNodes(); j++) {
				if (generatedVisibles[i][j] != _parameters.visibleNode(j)) {
					correct = false; // assume it will continue to the next second for-loop.
				}
			}
			if (correct) {
				count += 1;
			}
		}
		
		
		return count / numSamples;
	}
	
	public int generateX(int i, int j) {
		double mu = 0.0;
		mu += _parameters.visibleBias(j);
		for (int jj = 0; jj < _parameters.numHiddenNodes(); jj++) {
			 mu += _parameters.weight(j, jj) * generatedHiddens[i][jj];
		}
		mu = MathUtilities.sigmoid(mu);
		if (random.nextDouble() >= 1 - mu) {
			return 1;
		}
		else {
			return 0;
		}
	};
	
	public int generateH(int i, int j) {
		double mu = 0.0;
		mu += _parameters.hiddenBias(j);
		if (i == 0) {	
			for (int ii = 0; ii < _parameters.numVisibleNodes(); ii++) {
				 mu += _parameters.weight(ii, j) * _parameters.visibleNode(ii);
			}
		}
		else {
			for (int ii = 0; ii < _parameters.numVisibleNodes(); ii++) {
				mu += _parameters.weight(ii, j) * generatedVisibles[i-1][ii];
			}
		}
		mu = MathUtilities.sigmoid(mu);
		if (random.nextDouble() >= 1 - mu) {
			return 1;
		}
		else {
			return 0;
		}
	};
}
