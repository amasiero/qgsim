package utils.qsim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.qsim.ElementsGroup;

/**
 *
 * Miscellaneous Class to help on Q-Sim Algorithm
 *
 * @author Andrey Araujo Masiero
 * @version 1.0 November, 26th 2012
 *
 */
public class Misc {

	/**
	 * This function copy a list into a new one
	 *
	 * @param list
	 *            : the list to be copied
	 * @return newList: a copy of list
	 */
	public static List<Short> copyIntegerList(List<Short> list) {
		// Initialize a new list
		List<Short> newList = new ArrayList<Short>();

		// Copy all elements of list into newList
		for (short j : list)
			newList.add(j);

		// Returns newList
		return newList;
	}

	/**
	 *
	 * Find related objects into a array.
	 *
	 * @param dataArray
	 *            : Array of information
	 * @param value
	 *            : value to compare
	 * @return al: List with the related objects
	 */
	public static List<Short> findObjectIndex(float[] dataArray, float value) {

		// Initialize result list
		List<Short> al = new ArrayList<Short>();

		// Search for the related objects
		for (short i = 0; i < dataArray.length; i++)
			if (dataArray[i] == value)
				al.add(i);

		// Returns related list
		return al;
	}

	/**
	 * Check intersection between two lists
	 *
	 * @param aArray
	 *            : first list
	 * @param bArray
	 *            : second list
	 * @return list: intersection list
	 */
	public static List<Short> intersection(List<Short> aArray,
			List<Short> bArray) {

		// Initialize list
		List<Short> list = new ArrayList<Short>();

		// Roam through list1 checking if there is the element into list2

		short i = 0, j = 0;

		while (i < aArray.size() && j < bArray.size())
			if (aArray.get(i).equals(bArray.get(j))) {
				list.add(aArray.get(i));
				i++;
				j++;
			} else if (aArray.get(i) <= bArray.get(j))
				i++;
			else if (aArray.get(i) > bArray.get(j))
				j++;

		// Returns list
		return list;
	}

	/**
	 * Check intersection between two lists
	 *
	 * @param list1
	 *            : first list
	 * @param list2
	 *            : second list
	 * @return list: intersection list
	 */
	public static List<Short> intersection2(List<Short> list1, List<Short> list2) {

		// Initialize list
		List<Short> list = new ArrayList<Short>();

		// Roam through list1 checking if there is the element into list2

		for (Short t : list1)
			if (list2.contains(t))
				list.add(t);

		// Returns list
		return list;
	}

	/**
	 * Check intersection between two lists
	 *
	 * @param list1
	 *            : first list
	 * @param list2
	 *            : second list
	 * @return list: intersection list
	 */
	public static List<Short> intersection3(List<Short> list1, List<Short> list2) {

		// Initialize list
		Set<Short> s1 = new HashSet<Short>(list1), s2 = new HashSet<Short>(
				list2);
		Set<Short> list = s1.size() <= s2.size() ? s1 : s2;

		// Roam through list1 checking if there is the element into list2

		list.retainAll(s2);
		
		return new ArrayList<Short>(list);
	}

	/**
	 *
	 * This function finds the max value into a array
	 *
	 * @param data
	 *            : Double array
	 * @return maxValue: max value of array
	 */
	public static float max(float[] data) {
		// Inicialize maxValue variable with the first matrix element
		float maxValue = data[0];

		// Roam through all matrix to find the max value
		for (float element : data)
			maxValue = Math.max(element, maxValue);

		// Returns minValue
		return maxValue;
	}

	/**
	 *
	 * This function finds the max value into a matrix
	 *
	 * @param data
	 *            : Double matrix
	 * @return maxValue: max value of matrix
	 */
	public static float max(float[][] data) {
		// Initialize maxValue variable with the first matrix element
		float maxValue = data[0][0];

		// Roam through all matrix to find the max value
		for (float[] element : data)
			for (int j = 0; j < data[0].length; j++)
				maxValue = Math.max(element[j], maxValue);

		// Returns maxValue
		return maxValue;
	}

	/**
	 *
	 * This function finds the max value into a array
	 *
	 * @param data
	 *            : Double array
	 * @return maxValue: max value of array
	 */
	public static int max(int[] data) {
		// Inicialize maxValue variable with the first matrix element
		int maxValue = data[0];

		// Roam through all matrix to find the max value
		for (int element : data)
			maxValue = Math.max(element, maxValue);

		// Returns minValue
		return maxValue;
	}

	/**
	 *
	 * This function finds the min value into a array
	 *
	 * @param data
	 *            : Double array
	 * @return minValue: min value of array
	 */
	public static float min(float[] data) {
		// Inicialize minValue variable with the first matrix element
		float minValue = data[0];

		// Roam throught all matrix to find the min value
		for (float element : data)
			minValue = Math.min(element, minValue);

		// Returns minValue
		return minValue;
	}

	/**
	 *
	 * This function finds the min value into a matrix
	 *
	 * @param data
	 *            : Double matrix
	 * @return minValue: min value of matrix
	 */
	public static float min(float[][] data) {
		// Inicialize minValue variable with the first matrix element
		float minValue = data[0][0];

		// Roam throught all matrix to find the min value
		for (float[] element : data)
			for (int j = 0; j < data[0].length; j++)
				minValue = Math.min(element[j], minValue);

		// Returns minValue
		return minValue;
	}

	public static void printMatrix(double[][] m) {
		String str = "|\t";
		for (double[] element : m) {
			for (int j = 0; j < m[0].length; j++)
				str += element[j] + "\t";
			System.out.println(str + "|");
			str = "|\t";
		}
	}

	public static void printMatrix(int[][] m) {
		String str = "|\t";
		for (int[] element : m) {
			for (int j = 0; j < m[0].length; j++)
				str += element[j] + "\t";
			System.out.println(str + "|");
			str = "|\t";
		}
	}

	/**
	 *
	 * Sort the groups list from the biggest intersection to the smaller
	 * intersection with list
	 *
	 * @param list
	 *            : a list of elements to be compare
	 * @param groups
	 *            : list of all groups find
	 * @return group list reorganized
	 */
	public static List<ElementsGroup> sortByBiggerIntersection(
			List<Short> list, List<ElementsGroup> groups) {

		// Creates a new list of groups to support sorting action
		List<ElementsGroup> newGroups = new ArrayList<ElementsGroup>();

		// This procedure continues until new list reach same size of groups
		while (newGroups.size() != groups.size()) {

			// aux variables
			ElementsGroup g = new ElementsGroup();
			int sizeIntersection = -1;

			// Roam though all group searching for the biggest intersection
			// value
			for (ElementsGroup h : groups) {

				// intersection size with active group
				int aux = Misc.intersection(list, h.getElements()).size();

				// Save this group if the intersection are bigger and h group is
				// not inside to the new list
				if (aux > sizeIntersection && !newGroups.contains(h)) {
					sizeIntersection = aux;
					g = h;
				}
			}
			// Add the group selected into new list
			newGroups.add(g);
		}

		// Returns reordered list
		return newGroups;
	}

	/**
	 * This function prints existing groups
	 *
	 * @param groups
	 *            : list of all groups find
	 * @param filename
	 *            : name of the file to be saved
	 */
	public static void writeGroupsIndex(List<ElementsGroup> groups,
			String filename) {

		try {
			FileWriter file = new FileWriter(filename);
			PrintWriter out = new PrintWriter(file);

			for (ElementsGroup group : groups) {
				String str = "";
				for (int i = 0; i < group.getSize(); i++)
					if (i == group.getSize() - 1)
						str += group.getElements().get(i);
					else
						str += group.getElements().get(i) + "\t";
				out.println(str);
			}

			out.close();
			file.close();

		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}

	}

	/**
	 * This function prints a text file with the index of output groups
	 *
	 * @param groups
	 *            : list of all groups find
	 * @param filename
	 *            : name of the file to be saved
	 */
	public static void writeOutputGroups(List<ElementsGroup> groups,
			String filename) {

		try {
			FileWriter file = new FileWriter(filename);
			PrintWriter out = new PrintWriter(file);

			short total = 0;

			for (ElementsGroup group : groups) {
				total += group.getSize();
			}

			String str = "";
			for (short i = 0; i < total; i++)
				for (short ii = 0; ii < groups.size(); ii++) {
					if (groups.get(ii).contains(i)) {
						str += Short.toString(ii);
						if (i != total - 1)
							str += "\t";
						break;
					}
				}

			out.println(str);
			out.close();
			file.close();

		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}
}
