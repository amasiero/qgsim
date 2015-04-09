package similarity.qsim;

import java.util.List;

import models.qsim.DataRegister;

public interface Similarity {

	/**
	 * @param aux
	 * @param data
	 * @return
	 */
	public DataRegister centroid(List<Short> aux, List<DataRegister> data);

	/**
	 *
	 * Calculus of similarity based on Euclidean Distance
	 *
	 * @param data
	 *            : List of values
	 * @return similarityMatrix: Matrix of similarities between data
	 */
	public short[][] euclideanDistance(List<DataRegister> data);

}