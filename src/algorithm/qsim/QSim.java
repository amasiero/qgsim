package algorithm.qsim;

import java.util.ArrayList;
import java.util.List;

import file.qsim.TxtFunctions;
import file.qsim.XMLFunctions;
import utils.qsim.*;
import models.qsim.*;
import similarity.qsim.Similarity;

/**
 * 
 * Quality Similarity Clustering Algorithm (Q-SIM)
 * 
 * @author Andrey Araujo Masiero
 * @version 1.0 November, 26th 2012
 * 
 */
public class QSim {

	private List<DataRegister> data;
	private List<ElementsGroup> groups;
	private short[][] similarityMatrix;
	private short q;
	private float[] densityArray;
	private String configFile;
	private String dataFile;

	/**
	 * Constructor of QSim class
	 * 
	 * @param q
	 *            : q value
	 */
	public QSim(float q) {
		super();
		this.q = (short) (q * 1000);
		this.data = new ArrayList<DataRegister>();
		this.groups = new ArrayList<ElementsGroup>();
	}

	/**
	 * Constructor simple of QSim class
	 * 
	 * @param data
	 *            : data matrix
	 * @param q
	 *            : q value
	 */
	public QSim(List<DataRegister> data, float q) {
		super();
		this.data = data;
		this.q = (short) (q * 1000);
		this.groups = new ArrayList<ElementsGroup>();
	}

	/**
	 * Calculus of Related Set for each object
	 * 
	 * @return relatedSets: Matrix of related set's object
	 */
	public List<ElementsGroup> relatedSets() {

		// Initialize relatedSets matrix
		List<ElementsGroup> relatedSets = new ArrayList<ElementsGroup>();

		// Roam through all similarity matrix
		for (short i = 0; i < this.similarityMatrix.length; i++) {
			ElementsGroup e = new ElementsGroup();
			relatedSets.add(e);
			for (short j = 0; j < this.similarityMatrix.length; j++) {
				// Checks if is a related set of object i or not
				if (i != j) {
					if (this.similarityMatrix[i][j] >= this.q) {
						relatedSets.get(i).getElements().add(j);
					}
				}
			}
		}

		// Returns relatedSets matrix
		return relatedSets;
	}

	/**
	 * 
	 * Calculus of the maximum object intersection for each object.
	 * 
	 * @param relatedSets
	 *            : Related sets matrix
	 * @return moi: Updated with the maximum object intersection
	 */
	public List<ElementsGroup> maximumObjectIntersection(
			List<ElementsGroup> relatedSets) {

		// Initialize moi matrix
		List<ElementsGroup> moi = new ArrayList<ElementsGroup>();

		// Roam through all objects of related set
		for (int i = 0; i < relatedSets.size(); i++) {

			// Recovery index from all related objects
			List<Short> aArray = relatedSets.get(i).getElements();
			
			// Checks if the list aArray is empty
			if (!aArray.isEmpty()) {

				// Recovery index from all related objects of the first object
				// in aArray
				List<Short> bArray = relatedSets.get(aArray.get(0))
						.getElements();

				// Time safe object to insert into reduced
				short c = aArray.get(0);

				// Initialize reduced array that contains moi of this object
				List<Short> reduced = new ArrayList<Short>();

				boolean finish = true;
				while (finish) {
					// Store the intersection between aArray and bArray
					List<Short> auxArray = Misc.intersection(aArray, bArray);
					// Roam the rest of aArray elements to find the greatest
					// intersection between objects
					for (int j = 1; j < aArray.size(); j++) {
						bArray = relatedSets.get(aArray.get(j)).getElements();
						if (auxArray.size() < Misc.intersection(aArray, bArray)
								.size()) {
							// Save a great intersection
							auxArray = Misc.intersection(aArray, bArray);
							// Time safe object to insert into reduced
							c = aArray.get(j);
						}
					}

					// Add c object into reduced array and prepare to the next
					// interaction
					if (!auxArray.isEmpty()) {
						if (!reduced.contains(c)) {
							reduced.add(c);
						}
						aArray = auxArray;
						bArray = relatedSets.get(aArray.get(0)).getElements();
						c = aArray.get(0);
					} else {
						reduced.add(aArray.get(0));
						finish = false;
					}
				}

				// Update moi matrix with reduced array from i object
				ElementsGroup temp = new ElementsGroup();
				temp.setElements(reduced);
				moi.add(temp);
				temp = null;
			} else {
				ElementsGroup temp = new ElementsGroup();
				temp.setElements(aArray);
				moi.add(temp);
				temp = null;
			}
		}

		// Returns moi matrix
		return moi;
	}

	/**
	 * Density Calculus Function
	 * 
	 * @param moi
	 *            : matrix with the maximum object intersection
	 * @return densityArray: array with density value for each object
	 */
	public void densityDataArray(List<ElementsGroup> moi) {

		// Initialize densityArray
		float[] densityArray = new float[moi.size()];

		// Roam through all objects
		for (int i = 0; i < moi.size(); i++) {
			// aux list receive all similarity values from moi related objects
			List<Short> ind = moi.get(i).getElements();
			List<Float> aux = new ArrayList<Float>();

			for (int j : ind) {
				aux.add((float) similarityMatrix[i][j]/1000);
			}

			// Using coefficient of variation to calculate the density value for
			// each object
			if (Stats.coefVariation(aux) > 0) {
				densityArray[i] = aux.size() / Stats.coefVariation(aux);
			} else {
				densityArray[i] = aux.size();
			}
		}

		// Returns densityArray
		this.densityArray = densityArray;
	}

	/**
	 * Density Calculus Function
	 * 
	 * @param moi
	 *            : matrix with the maximum object intersection
	 * @param densityArray
	 *            : array with density value to be updated
	 * @return densityArray: array with density value for each object
	 */
	public void densityDataArray(List<ElementsGroup> moi, float[] densityArray) {
		try{
		// Roam through all objects
		for (int i = 0; i < moi.size(); i++) {
			// Verifies if the object has a group
			if (densityArray[i] != (-1)) {
				// aux list receive all similarity values from moi related
				// objects
				List<Short> ind = moi.get(i).getElements();
				List<Float> aux = new ArrayList<Float>();

				// Select only objects that hasn't a group
				for (int j : ind) {
					if (densityArray[j] != (-1)) {
						aux.add((float) similarityMatrix[i][j]/1000);
					}
				}

				// Using coefficient of variation to calculate the density value
				// for
				// each object
				if (Stats.coefVariation(aux) > 0) {
					densityArray[i] = aux.size() / Stats.coefVariation(aux);
				} else {
					densityArray[i] = aux.size();
				}
			}
		}

		// Returns densityArray
		this.densityArray = densityArray;
		}catch(ArrayIndexOutOfBoundsException e){
			e.getMessage();
		}
	}

	/**
	 * 
	 * This function generates the independent groups of objects and indicates
	 * this groups on label attribute.
	 * 
	 * @param moi
	 *            : matrix with the maximum object intersection
	 */
	public void generateGroups(List<ElementsGroup> moi) {

		// Keep grouping until all elements belong to a group
		while (Misc.max(this.densityArray) != (-1)) {

			// Select the element with higher density value
			List<Short> aux = Misc.findObjectIndex(this.densityArray,
					Misc.max(this.densityArray));

			short c = -1;

			// Checks if the element choose doesn't belong to any group
			if (this.densityArray[aux.get(0)] != (-1)) {
				c = aux.get(0);
			}

			// Separates all related elements and itself into aux list
			aux = moi.get(c).getElements();
			aux.add(c);

			// Checks if there is some group create to try to include the aux
			// list into existing groups
			if (!this.groups.isEmpty()) {
				aux = this.insertNewElements(aux);
			}

			// Checks if aux list is empty and if is not solve the intersection
			// between aux list and existing groups
			if (!aux.isEmpty()) {

				// Update density value of aux list elements
				for (int i : aux) {
					this.densityArray[i] = (-1);
				}

				// Solve the intersection
				this.resolveIntersection(aux);
			}

			// Recalculate the density values
			this.densityDataArray(moi, this.densityArray);
		}

		// Refine the border groups after intersection solved
		this.refineBoderGroups();
		// Try to minimize the number of groups created
		this.minimizeNumberOfGroups();

	}

	/**
	 * This function creates independent groups
	 * 
	 * @param aux
	 *            : This is a new group to be included in labels
	 */
	public void resolveIntersection(List<Short> aux) {
		// Checks if exists some group
		if (!this.groups.isEmpty()) {
			// Calculates centroid of aux
			DataRegister centroidAux = Similarity.centroid(aux, this.data);
			// Roam through all groups created
			for (ElementsGroup g : this.groups) {
				// Calculates centroid from group g
				DataRegister centroidG = Similarity.centroid(g.getElements(),
						this.data);
				// list is an aux array keep just the intersection between g and
				// aux
				List<Short> list = Misc.intersection2(aux, g.getElements());

				// Checks if there is an intersection
				if (!list.isEmpty()) {
					// Roam through all elements into list
					for (short e : list) {
						// Aux list for calculates similarity between centroids
						// and the element
						List<DataRegister> lp = new ArrayList<DataRegister>();
						lp.add(centroidAux);
						lp.add(centroidG);
						lp.add(this.data.get(e));

						// Calculates the similarity between them
						short[][] sim = Similarity.euclideanDistance(lp);

						// Removes the element from least similar centroid
						if (sim[0][2] <= sim[1][2]) {
							aux.remove(aux.indexOf(e));
						} else {
							g.getElements().remove(g.getElements().indexOf(e));
						}

					}
				}
			}
		}
		// Add aux into groups list
		ElementsGroup g = new ElementsGroup();
		g.setElements(aux);
		this.groups.add(g);

	}

	/**
	 * 
	 * This function will try to allocate new elements into existing groups
	 * 
	 * @param list
	 *            : list of elements to be inserted
	 * @return list of remaining elements
	 */
	public List<Short> insertNewElements(List<Short> list) {

		// Sort the groups from the bigger intersection to the smaller with list
		Misc.sortByBiggerIntersection(list, this.groups);

		// This is a supporting list to help in the task of insert new elements
		// into existing groups
		List<Short> auxList = new ArrayList<Short>();

		// Roam through all existing groups
		for (short i = 0; i < this.groups.size(); i++) {
			// Supporting variables
			auxList = new ArrayList<Short>();
			boolean accept = true;

			// Select only elements without group
			for (short element : list) {
				if (this.densityArray[element] != (-1)) {
					auxList.add(element);
				}
			}

			// Check if there is any element on the list without group
			if (!auxList.isEmpty()) {
				// Roam through all elements without group
				for (short element : auxList) {
					// Roam through all elements of group i to compare if the q
					// value keeps into all elements adding this new element
					for (int element2 : this.groups.get(i).getElements()) {
						if (this.similarityMatrix[element][element2] < this.q) {
							accept = false;
						}
					}
					// Checks if the element can be included into group i, and
					// if is true add it.
					if (accept) {
						this.groups.get(i).getElements().add(element);
						this.densityArray[element] = (-1);
					} else {
						accept = true;
					}
				}
			}

			/**
			 * Precisa encontrar uma solucao mais elegante para esse problema,
			 * pois quando o unico elemento a ser inserido entra no ultimo
			 * grupo, auxList nao fica vazia tornando necessario resolver a
			 * intersecao entre os grupos. (7 proximas linhas de codigo)
			 */
			auxList = new ArrayList<Short>();
			// Select only elements without group
			for (short element : list) {
				if (this.densityArray[element] != (-1)) {
					auxList.add(element);
				}
			}

		}

		// If all element of new group (list) were include, list is initialize
		// again to return a empty group.
		if (auxList.isEmpty()) {
			list = new ArrayList<Short>();
		}

		// Returns list
		return list;
	}

	/**
	 * This function refines the border groups trying to allocate better the
	 * boundary elements
	 */
	public void refineBoderGroups() {

		// Roam through all groups existing
		for (short i = 0; i < this.groups.size(); i++) {

			// Creates a support list roam through all elements existing into a
			// group
			List<Short> list = Misc.copyIntegerList(this.groups.get(i)
					.getElements());

			// Roam through all elements into a group
			for (short element : list) {

				// Calculates all existing groups centroid
				List<DataRegister> lp = new ArrayList<DataRegister>();
				for (ElementsGroup g : this.groups) {
					lp.add(Similarity.centroid(g.getElements(), this.data));
				}
				// Add the element in comparison
				lp.add(this.data.get(element));

				// Calculates the similarity between them
				short[][] sim = Similarity.euclideanDistance(lp);

				// Copy the group number to the choose one temporary
				short choose = i;

				// Search for the biggest similarity group
				for (short j = 0; j < (lp.size() - 1); j++) {
					if ((i != j)
							&& (sim[lp.size() - 1][j] > sim[lp.size() - 1][choose])) {
						choose = j;
					}
				}

				// If the biggest similarity groups is not actual one, the
				// element is removed and added into new choose one.
				if (i != choose) {
					this.groups.get(choose).getElements().add(element);
					this.groups
							.get(i)
							.getElements()
							.remove(this.groups.get(i).getElements()
									.indexOf(element));
				}
			}
		}
	}

	/**
	 * This function tries to minimize the number of groups generated
	 */
	public void minimizeNumberOfGroups() {

		// Control variables
		boolean stop = false;
		int count = 0;
		int finalize = 0;

		while (!stop) {

			// Calculates all existing groups centroid
			List<DataRegister> lp = new ArrayList<DataRegister>();
			for (ElementsGroup g : this.groups) {
				lp.add(Similarity.centroid(g.getElements(), this.data));
			}

			// Calculates the similarity between them
			short[][] simTemp = Similarity.euclideanDistance(lp);

			// Supporting variables
			boolean include = false;

			// Roam through all groups trying to join each other
			for (int j = 0; j < simTemp.length && !include; j++) {
				if ((simTemp[count][j] > this.q) && (count != j)) {
					include = this.joinGroups(count, j);
				}
			}

			// When two groups are joined, it's necessary subtract count
			// variable to avoid index boundary exception.
			if (!include) {
				finalize++;
			} else {
				count--;
			}

			// This step checks if all groups was roamed and nothing change to
			// finalize the method
			if (finalize >= this.groups.size()) {
				stop = true;
			}

			// Controls count variable
			if (count < this.groups.size()) {
				count++;
			} else {
				count = 0;
				finalize = 0;
			}

		}
	}

	/**
	 * 
	 * This function tries to join one group with another one.
	 * 
	 * @param group1
	 *            : first group to be joined
	 * @param group2
	 *            : second group to be joined
	 * @return if the groups were joined or not.
	 */
	public boolean joinGroups(int group1, int group2) {
		// Supporting variable
		boolean joined = true;

		// Checks if all elements for both groups attempt to the q value
		for (int element1 : this.groups.get(group1).getElements()) {
			for (int element2 : this.groups.get(group2).getElements()) {
				if (this.similarityMatrix[element1][element2] < this.q) {
					joined = false;
				}
			}
		}

		// Join the groups if it's possible
		if (joined) {
			for (short element : this.groups.get(group2).getElements()) {
				this.groups.get(group1).getElements().add(element);
			}
			this.groups.remove(this.groups.get(group2));
		}

		// Returns if the groups were joined or not.
		return joined;
	}

	/**
	 * @return the groups
	 */
	public List<ElementsGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<ElementsGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the data
	 */
	public List<DataRegister> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<DataRegister> data) {
		this.data = data;
	}

	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile
	 *            the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * @return the dataFile
	 */
	public String getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile
	 *            the dataFile to set
	 */
	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	/**
	 * Execute all algorithm after object instance
	 */
	public void execute() {
		long time = System.currentTimeMillis();
		System.out.println("Iniciou...");
		long time2 = System.currentTimeMillis();
		System.out.println("Calculando a similaridade...");
		this.similarityMatrix = Similarity.euclideanDistance(this.data);
		System.out.println("Tempo Calculo Similaridade: "
				+ ((System.currentTimeMillis() - time2) / 1000.0) + " segundos");
		time2 = System.currentTimeMillis();
		System.out.println("Calculando o rs...");
		List<ElementsGroup> rs = this.relatedSets();
		System.out.println("Tempo Calculo RS: "
				+ ((System.currentTimeMillis() - time2) / 1000.0) + " segundos");
		time2 = System.currentTimeMillis();
		System.out.println("Calculando o moi...");
		List<ElementsGroup> moi = this.maximumObjectIntersection(rs);
		System.out.println("Tempo Calculo RRS: "
				+ ((System.currentTimeMillis() - time2) / 1000.0) + " segundos");
		time2 = System.currentTimeMillis();
		System.out.println("Calculando a densidade...");
		this.densityDataArray(moi);
		System.out.println("Tempo Calculo Densidade: "
				+ ((System.currentTimeMillis() - time2) / 1000.0) + " segundos");
		time2 = System.currentTimeMillis();
		System.out.println("Gerando os grupos...");
		this.generateGroups(moi);
		System.out.println("Tempo Calculo Groups: "
				+ ((System.currentTimeMillis() - time2) / 1000.0) + " segundos");
		System.out.println("Terminou...\n.\n.\n.\n.\n.");
		System.out.println("Tempo de execucao: "
				+ ((System.currentTimeMillis() - time) / 1000.0) + " segundos");
	}

	/**
	 * 
	 * @param configFile
	 * @param dataFile
	 */
	public void setData(String configFile, String dataFile) {
		this.configFile = configFile;
		this.dataFile = dataFile;
		this.data = TxtFunctions.readTxt(this.dataFile,
				XMLFunctions.readXML(this.configFile));
	}

	/**
	 * Save group index in a txt file.
	 */
	public void saveGroupsFile() {
		Misc.writeGroupsIndex(this.groups, "groups.txt");
	}

}
