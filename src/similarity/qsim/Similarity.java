package similarity.qsim;

import java.util.List;

import models.qsim.DataRegister;

/**
 * Represents a similarity measure. Create a class that implements this
 * interface in order to make QSIM use your own similarity measures
 * 
 * @author Douglas De Rizzo Meneghetti
 *
 */
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
	public short[][] getDistanceMatrix(List<DataRegister> data);

}