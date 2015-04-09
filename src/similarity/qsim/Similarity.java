package similarity.qsim;

import java.util.ArrayList;
import java.util.List;

//import models.qsim.Attribute;
import models.qsim.DataRegister;
import utils.qsim.Misc;

public class Similarity {

	/**
	 *
	 * @param aux
	 * @param data
	 * @return
	 */
	public static DataRegister centroid(List<Short> aux, List<DataRegister> data) {

		// Initialize aux variables
		DataRegister centroid = new DataRegister();
		List<Float> register = new ArrayList<Float>();
		for (int i = 0; i < data.get(0).getRegister().size(); i++) {
			float temp = 0.0f;
			for (int j : aux)
				temp += data.get(j).getRegister().get(i);
			temp /= aux.size();
			register.add(temp);
		}

		centroid.setRegister(register);

		// Returns centroid
		return centroid;
	}

	/**
	 *
	 * This function normalizes data between 0 and 1 for a better algorithm
	 * performance
	 *
	 * @param data
	 *            : Double matrix to normalize data between 0 and 1
	 * @return data: Data normalized
	 */
	public static short[][] dataNormalization(float[][] data) {

		short[][] newData = new short[data.length][data.length];
		// Save min value of data
		float minValue = Misc.min(data);
		// Save max value of data
		float maxValue = Misc.max(data);

		// Roam through data normalizing its information
		for (int i = 0; i < data.length; i++)
			for (int j = i; j < data.length; j++) {
				newData[i][j] = (short) ((1 - (data[i][j] - minValue)
						/ (maxValue - minValue)) * 1000);
				newData[j][i] = newData[i][j];
			}

		// Returns data normalized
		return newData;
	}

	/**
	 *
	 * Calculus of similarity based on Euclidean Distance
	 *
	 * @param data
	 *            : List of values
	 * @return similarityMatrix: Matrix of similarities between data
	 */
	public static short[][] euclideanDistance(List<DataRegister> data) {

		float[][] matrix = new float[data.size()][data.size()];

		for (int i = 0; i < matrix.length; i++)
			for (int j = i; j < matrix.length; j++) {
				double sqSum = 0.0;
				for (int k = 0; k < data.get(0).getRegister().size(); k++)
					sqSum += Math.pow(data.get(i).getRegister().get(k)
							- data.get(j).getRegister().get(k), 2);
				matrix[i][j] = (float) (Math.sqrt(sqSum) / data.get(0)
						.getRegister().size());
				matrix[j][i] = matrix[i][j];
			}

		// printSimilarity(matrix);
		return dataNormalization(matrix);
	}
}
