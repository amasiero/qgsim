package similarity.qsim;

import java.util.List;

import models.qsim.DataRegister;
import utils.qsim.Stats;

/**
 * Uses the p-norm as the similarity measure between data points.
 * 
 * A norm is a function that assigns sizes to vectors in a vector space. The
 * p-norm is a specific type of norm whose use is of interest in cluster
 * analysis due to the fact that, when p = 1, the p-norm equals the Manhattan
 * distance; when p = 2, it equals the Euclidean distance; when p = infinity,
 * the p-norm is equal to the Chebyshev distance; and for 1 < p < infinity, the
 * p-norm equals the behavior of the Minkowski distance.
 * 
 * Also, for p > 1, the p-norm satisfies the four basic conditions of a metric,
 * that is, non-negativity, identity of indiscernibles, symmetry and the
 * triangke inequality.
 * 
 * @author Douglas De Rizzo Meneghetti
 *
 */
public class PNorm implements Similarity {

	private int p;

	/**
	 * P-norm constructor that mimics the Euclidean distance
	 */
	public PNorm() {
		this.p = 2;
	}

	/**
	 * P-norm constructor
	 * 
	 * @param p
	 *            the p value for the norm
	 */
	public PNorm(int p) {
		this.p = p;
	}

	@Override
	public short[][] getDistanceMatrix(List<DataRegister> data) {
		float[][] matrix = new float[data.size()][data.size()];

		for (int i = 0; i < matrix.length; i++)
			for (int j = i; j < matrix.length; j++) {
				double sqSum = 0.0;
				for (int k = 0; k < data.get(0).getRegister().size(); k++)
					sqSum += Math.pow(data.get(i).getRegister().get(k)
							- data.get(j).getRegister().get(k), p);
				matrix[i][j] = (float) (root(sqSum, p) / data.get(0)
						.getRegister().size());
				matrix[j][i] = matrix[i][j];
			}

		return Stats.dataNormalization(matrix);
	}

	/**
	 * Calculates the n-th root of a number
	 * 
	 * @param num
	 *            The number whose root will be calculated
	 * @param root
	 *            The root number
	 * @return the root of num
	 */
	private double root(double num, double root) {
		return Math.pow(num, 1 / root);
	}

}
