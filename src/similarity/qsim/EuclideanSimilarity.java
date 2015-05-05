package similarity.qsim;

import java.util.ArrayList;
import java.util.List;

import models.qsim.DataRegister;
import utils.qsim.Stats;

/**
 * Euclidean distance
 * 
 * @author Andrey Araujo Masiero
 * @author Douglas De Rizzo Meneghetti
 *
 */
public class EuclideanSimilarity implements Similarity {

	PNorm pnorm = new PNorm(2);

	/**
	 *
	 * Calculus of similarity based on Euclidean Distance
	 *
	 * @param data
	 *            : List of values
	 * @return similarityMatrix: Matrix of similarities between data
	 */
	public short[][] getDistanceMatrix(List<DataRegister> data) {
		return pnorm.getDistanceMatrix(data);
	}

	@Override
	public String toString() {
		return "Euclidean";
	}
}
