package utils.qsim;

import java.util.ArrayList;
import java.util.List;

import models.qsim.DataRegister;

/**
 *
 * This class contains all statistical functions
 *
 * @author Andrey Araujo Masiero
 * @version 1.0 November, 29th 2012
 *
 */

public class Stats {
	/**
	 *
	 * Coefficient of Variation calculus
	 *
	 * @param list
	 *            : list of values
	 * @return coefficient of variation value
	 */
	public static float coefVariation(List<Float> list) {
		return stdDev(list) / mean(list);
	}

	/**
	 * Calculus of the list's mean value
	 *
	 * @param list
	 *            : list of values
	 * @return mean value of the list
	 */
	public static float mean(List<Float> list) {

		return sum(list) / list.size();

	}

	/**
	 * Standard Deviation Calculus
	 *
	 * @param list
	 *            : list of values
	 * @return standard deviation value
	 */
	public static float stdDev(List<Float> list) {

		float sum = 0;
		float mean = mean(list);

		for (double l : list)
			sum += Math.pow(l - mean, 2);

		return (float) Math.sqrt(sum / (list.size() - 1));
	}

	/**
	 * Sum of list
	 *
	 * @param list
	 *            : list of values to be summed
	 * @return sum: result of the sum
	 */
	public static float sum(List<Float> list) {

		if (list.size() > 0) {

			float sum = 0;

			for (double l : list)
				sum += l;

			return sum;

		} else
			return 0;
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
	 * @param groupIndexes
	 * @param data
	 * @return
	 */
	public static DataRegister centroid(List<Short> groupIndexes, List<DataRegister> data) {
		// Initialize aux variables
		DataRegister centroid = new DataRegister();
		List<Float> register = new ArrayList<Float>();
		for (int i = 0; i < data.get(0).getRegister().size(); i++) {
			float temp = 0.0f;
			for (int j : groupIndexes)
				temp += data.get(j).getRegister().get(i);
			temp /= groupIndexes.size();
			register.add(temp);
		}

		centroid.setRegister(register);

		// Returns centroid
		return centroid;
	}

}
