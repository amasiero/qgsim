package algorithm.qsim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.qsim.DataRegister;
import models.qsim.ElementsGroup;
import similarity.qsim.Similarity;
import utils.qsim.Misc;
import utils.qsim.Stats;
import file.qsim.TxtFunctions;
import file.qsim.XMLFunctions;

/**
 *
 * Quality Similarity Clustering Algorithm (Q-SIM)
 *
 * @author Andrey Araujo Masiero
 * @version 1.0 November, 26th 2012
 *
 */
public class QSim implements Runnable {

	private List<DataRegister> data;
	private List<ElementsGroup> groups;
	private short[][] similarityMatrix;
	private short q;
	private float[] densityArray;
	private String configFile;
	private String dataFile;
	private String groupsFile;
	private Similarity similarity;

	/**
	 * Constructor of QSim class
	 *
	 * @param q
	 *            q value
	 */
	public QSim(float q) {
		super();
		this.q = (short) (q * 1000);
		data = new ArrayList<DataRegister>();
		groups = new ArrayList<ElementsGroup>();
	}

	/**
	 * Constructor of QSim class
	 *
	 * @param q
	 *            q value
	 * @param similarity
	 *            : object that defines the similarity measures to be used by
	 *            the algorithm
	 * @param configFile
	 *            : full path and name for the XML configuration file
	 * @param dataFile
	 *            : full path and name for the text file containing the
	 *            comma-separated data to be clustered
	 * @param configFile
	 *            : full path and name to save the text file containing the
	 *            final classification of the algorithm
	 */
	public QSim(float q, Similarity similarity, String configFile,
			String dataFile, String groupsFile) {
		super();
		this.similarity = similarity;
		this.q = (short) (q * 1000);
		groups = new ArrayList<ElementsGroup>();
		setData(configFile, dataFile);
		this.setGroupsFile(groupsFile);
	}

	/**
	 * Constructor simple of QSim class
	 *
	 * @param data
	 *            data matrix
	 * @param q
	 *            q value
	 */
	public QSim(List<DataRegister> data, float q) {
		super();
		this.data = data;
		this.q = (short) (q * 1000);
		groups = new ArrayList<ElementsGroup>();
	}

	/**
	 * Density Calculus Function
	 *
	 * @param moi
	 *            matrix with the 
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

			for (int j : ind)
				aux.add((float) similarityMatrix[i][j] / 1000);

			// Using coefficient of variation to calculate the density value for
			// each object
			if (Stats.coefVariation(aux) > 0)
				densityArray[i] = aux.size() / Stats.coefVariation(aux);
			else
				densityArray[i] = aux.size();
		}

		// Returns densityArray
		this.densityArray = densityArray;
	}

	/**
	 * Density Calculus Function
	 *
	 * @param moi
	 *            matrix with the maximum object intersection
	 * @param densityArray
	 *            array with density value to be updated
	 * @return densityArray: array with density value for each object
	 */
	public void densityDataArray(List<ElementsGroup> moi, float[] densityArray) {
		try {
			// Roam through all objects
			for (int i = 0; i < moi.size(); i++)
				// Verifies if the object has a group
				if (densityArray[i] != -1) {
					// aux list receive all similarity values from moi related
					// objects
					List<Short> ind = moi.get(i).getElements();
					List<Float> aux = new ArrayList<Float>();

					// Select only objects that hasn't a group
					for (int j : ind)
						if (densityArray[j] != -1)
							aux.add((float) similarityMatrix[i][j] / 1000);

					// Using coefficient of variation to calculate the density
					// value
					// for
					// each object
					if (Stats.coefVariation(aux) > 0)
						densityArray[i] = aux.size() / Stats.coefVariation(aux);
					else
						densityArray[i] = aux.size();
				}

			// Returns densityArray
			this.densityArray = densityArray;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
		}
	}

	/**
	 * Execute all algorithm after object instance
	 */
	public void execute() {
		try {
			long time = System.currentTimeMillis();
			System.out.println("Iniciou...");
			long time2 = System.currentTimeMillis();
			System.out.println("Calculando a similaridade...");
			similarityMatrix = similarity.euclideanDistance(data);
			System.out.println("Tempo Calculo Similaridade: "
					+ (System.currentTimeMillis() - time2) / 1000.0
					+ " segundos");
			time2 = System.currentTimeMillis();
			System.out.println("Calculando o rs...");
			List<ElementsGroup> rs = this.relatedSets();
			System.out.println("Tempo Calculo RS: "
					+ (System.currentTimeMillis() - time2) / 1000.0
					+ " segundos");
			time2 = System.currentTimeMillis();
			System.out.println("Calculando o moi...");
			List<ElementsGroup> moi;
			moi = this.maximumObjectIntersection(rs);
			System.out.println("Tempo Calculo RRS: "
					+ (System.currentTimeMillis() - time2) / 1000.0
					+ " segundos");
			time2 = System.currentTimeMillis();
			System.out.println("Calculando a densidade...");
			this.densityDataArray(moi);
			System.out.println("Tempo Calculo Densidade: "
					+ (System.currentTimeMillis() - time2) / 1000.0
					+ " segundos");
			time2 = System.currentTimeMillis();
			System.out.println("Gerando os grupos...");
			this.generateGroups(moi);
			System.out.println("Tempo Calculo Groups: "
					+ (System.currentTimeMillis() - time2) / 1000.0
					+ " segundos");
			System.out.println("Terminou...\n.\n.\n.\n.\n.");
			System.out.println("Tempo de execucao: "
					+ (System.currentTimeMillis() - time) / 1000.0
					+ " segundos");
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * This function generates the independent groups of objects and indicates
	 * this groups on label attribute.
	 *
	 * @param moi
	 *            matrix with the maximum object intersection
	 */
	public void generateGroups(List<ElementsGroup> moi) {

		// Keep grouping until all elements belong to a group
		while (Misc.max(densityArray) != -1) {

			// Select the element with higher density value
			List<Short> aux = Misc.findObjectIndex(densityArray,
					Misc.max(densityArray));

			short c = -1;

			// Checks if the element choose doesn't belong to any group
			if (densityArray[aux.get(0)] != -1)
				c = aux.get(0);

			// Separates all related elements and itself into aux list
			aux = moi.get(c).getElements();
			aux.add(c);

			// Checks if there is some group create to try to include the aux
			// list into existing groups
			if (!groups.isEmpty())
				aux = this.insertNewElements(aux);

			// Checks if aux list is empty and if is not solve the intersection
			// between aux list and existing groups
			if (!aux.isEmpty()) {

				// Update density value of aux list elements
				for (int i : aux)
					densityArray[i] = -1;

				// Solve the intersection
				this.resolveIntersection(aux);
			}

			// Recalculate the density values
			this.densityDataArray(moi, densityArray);
		}

		// Refine the border groups after intersection solved
		this.refineBoderGroups();
		// Try to minimize the number of groups created
		this.minimizeNumberOfGroups();

	}

	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @return the data
	 */
	public List<DataRegister> getData() {
		return data;
	}

	/**
	 * @return the dataFile
	 */
	public String getDataFile() {
		return dataFile;
	}

	/**
	 * @return the groups
	 */
	public List<ElementsGroup> getGroups() {
		return groups;
	}

	public String getGroupsFile() {
		return groupsFile;
	}

	/**
	 *
	 * This function will try to allocate new elements into existing groups
	 *
	 * @param list
	 *            list of elements to be inserted
	 * @return list of remaining elements
	 */
	public List<Short> insertNewElements(List<Short> list) {

		// Sort the groups from the bigger intersection to the smaller with list
		Misc.sortByBiggerIntersection(list, groups);

		// This is a supporting list to help in the task of insert new elements
		// into existing groups
		List<Short> auxList = new ArrayList<Short>();

		// Roam through all existing groups
		for (short i = 0; i < groups.size(); i++) {
			// Supporting variables
			auxList = new ArrayList<Short>();
			boolean accept = true;

			// Select only elements without group
			for (short element : list)
				if (densityArray[element] != -1)
					auxList.add(element);

			// Check if there is any element on the list without group
			if (!auxList.isEmpty())
				// Roam through all elements without group
				for (short element : auxList) {
					// Roam through all elements of group i to compare if the q
					// value keeps into all elements adding this new element
					for (int element2 : groups.get(i).getElements())
						if (similarityMatrix[element][element2] < q)
							accept = false;
					// Checks if the element can be included into group i, and
					// if is true add it.
					if (accept) {
						groups.get(i).getElements().add(element);
						densityArray[element] = -1;
					} else
						accept = true;
				}

			/**
			 * Precisa encontrar uma solucao mais elegante para esse problema,
			 * pois quando o unico elemento a ser inserido entra no ultimo
			 * grupo, auxList nao fica vazia tornando necessario resolver a
			 * intersecao entre os grupos. (7 proximas linhas de codigo)
			 */
			auxList = new ArrayList<Short>();
			// Select only elements without group
			for (short element : list)
				if (densityArray[element] != -1)
					auxList.add(element);

		}

		// If all element of new group (list) were include, list is initialize
		// again to return a empty group.
		if (auxList.isEmpty())
			list = new ArrayList<Short>();

		// Returns list
		return list;
	}

	/**
	 *
	 * This function tries to join one group with another one.
	 *
	 * @param group1
	 *            first group to be joined
	 * @param group2
	 *            second group to be joined
	 * @return if the groups were joined or not.
	 */
	public boolean joinGroups(int group1, int group2) {
		// Supporting variable
		boolean joined = true;

		// Checks if all elements for both groups attempt to the q value
		for (int element1 : groups.get(group1).getElements())
			for (int element2 : groups.get(group2).getElements())
				if (similarityMatrix[element1][element2] < q)
					joined = false;

		// Join the groups if it's possible
		if (joined) {
			for (short element : groups.get(group2).getElements())
				groups.get(group1).getElements().add(element);
			groups.remove(groups.get(group2));
		}

		// Returns if the groups were joined or not.
		return joined;
	}

	/**
	 * Generates the maximum object intersection for each related set in a
	 * separated thread.
	 *
	 * @param relatedSets
	 *            Related sets matrix
	 * @return moi Updated with the maximum object intersection
	 */
	public List<ElementsGroup> maximumObjectIntersection(
			List<ElementsGroup> relatedSets) throws InterruptedException,
			ExecutionException {

		// Initialize moi matrix
		List<ElementsGroup> moi = new ArrayList<ElementsGroup>();

		for (int i = 0; i < relatedSets.size(); i++) {
			moi.add(new MOIGenerator(relatedSets, i).generateMOI());
		}

		return moi;
	}

	/**
	 *
	 * Generates the maximum object intersection for each related set in a
	 * separated thread.
	 *
	 * @author Douglas De Rizzo Meneghetti
	 * 
	 * @param relatedSets
	 *            Related sets matrix
	 * @return moi Updated with the maximum object intersection
	 */
	public List<ElementsGroup> parallelMaximumObjectIntersection(
			List<ElementsGroup> relatedSets) throws InterruptedException,
			ExecutionException {

		// Initialize moi matrix
		List<ElementsGroup> moi = new ArrayList<ElementsGroup>();
		ArrayList<MOIGenerator> tasks = new ArrayList<MOIGenerator>();
		ExecutorService executor = Executors.newCachedThreadPool();

		for (int i = 0; i < relatedSets.size(); i++) {
			tasks.add(new MOIGenerator(relatedSets, i));
		}
		
		List<Future<ElementsGroup>> results = executor.invokeAll(tasks);
		for (Future<ElementsGroup> result : results) {
			moi.add(result.get());
		}

		executor.shutdown();

		return moi;
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
			for (ElementsGroup g : groups)
				lp.add(similarity.centroid(g.getElements(), data));

			// Calculates the similarity between them
			short[][] simTemp = similarity.euclideanDistance(lp);

			// Supporting variables
			boolean include = false;

			// Roam through all groups trying to join each other
			for (int j = 0; j < simTemp.length && !include; j++)
				if (simTemp[count][j] > q && count != j)
					include = this.joinGroups(count, j);

			// When two groups are joined, it's necessary subtract count
			// variable to avoid index boundary exception.
			if (!include)
				finalize++;
			else
				count--;

			// This step checks if all groups was roamed and nothing change to
			// finalize the method
			if (finalize >= groups.size())
				stop = true;

			// Controls count variable
			if (count < groups.size())
				count++;
			else {
				count = 0;
				finalize = 0;
			}

		}
	}

	/**
	 * This function refines the border groups trying to allocate better the
	 * boundary elements
	 */
	public void refineBoderGroups() {

		// Roam through all groups existing
		for (short i = 0; i < groups.size(); i++) {

			// Creates a support list roam through all elements existing into a
			// group
			List<Short> list = Misc
					.copyIntegerList(groups.get(i).getElements());

			// Roam through all elements into a group
			for (short element : list) {

				// Calculates all existing groups centroid
				List<DataRegister> lp = new ArrayList<DataRegister>();
				for (ElementsGroup g : groups)
					lp.add(similarity.centroid(g.getElements(), data));
				// Add the element in comparison
				lp.add(data.get(element));

				// Calculates the similarity between them
				short[][] sim = similarity.euclideanDistance(lp);

				// Copy the group number to the choose one temporary
				short choose = i;

				// Search for the biggest similarity group
				for (short j = 0; j < lp.size() - 1; j++)
					if (i != j
							&& sim[lp.size() - 1][j] > sim[lp.size() - 1][choose])
						choose = j;

				// If the biggest similarity groups is not actual one, the
				// element is removed and added into new choose one.
				if (i != choose) {
					groups.get(choose).getElements().add(element);
					groups.get(i)
							.getElements()
							.remove(groups.get(i).getElements()
									.indexOf(element));
				}
			}
		}
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
		for (short i = 0; i < similarityMatrix.length; i++) {
			ElementsGroup e = new ElementsGroup();
			relatedSets.add(e);
			for (short j = 0; j < similarityMatrix.length; j++)
				// Checks if is a related set of object i or not
				if (i != j)
					if (similarityMatrix[i][j] >= q)
						relatedSets.get(i).getElements().add(j);
		}

		// Returns relatedSets matrix
		return relatedSets;
	}

	/**
	 * This function creates independent groups
	 *
	 * @param aux
	 *            This is a new group to be included in labels
	 */
	public void resolveIntersection(List<Short> aux) {
		// Checks if exists some group
		if (!groups.isEmpty()) {
			// Calculates centroid of aux
			DataRegister centroidAux = similarity.centroid(aux, data);
			// Roam through all groups created
			for (ElementsGroup g : groups) {
				// Calculates centroid from group g
				DataRegister centroidG = similarity.centroid(g.getElements(),
						data);
				// list is an aux array keep just the intersection between g and
				// aux
				List<Short> list = Misc.intersection2(aux, g.getElements());

				// Checks if there is an intersection
				if (!list.isEmpty())
					// Roam through all elements into list
					for (short e : list) {
						// Aux list for calculates similarity between centroids
						// and the element
						List<DataRegister> lp = new ArrayList<DataRegister>();
						lp.add(centroidAux);
						lp.add(centroidG);
						lp.add(data.get(e));

						// Calculates the similarity between them
						short[][] sim = similarity.euclideanDistance(lp);

						// Removes the element from least similar centroid
						if (sim[0][2] <= sim[1][2])
							aux.remove(aux.indexOf(e));
						else
							g.getElements().remove(g.getElements().indexOf(e));

					}
			}
		}
		// Add aux into groups list
		ElementsGroup g = new ElementsGroup();
		g.setElements(aux);
		groups.add(g);

	}

	@Override
	public void run() {
		execute();
		// saveGroupsFile(groupsFile);
		saveOutputGroups(groupsFile);
	}

	/**
	 * Save group index in a txt file.
	 */
	public void saveGroupsFile() {
		Misc.writeGroupsIndex(groups, "groups.txt");
	}

	/**
	 * Save group index in a txt file.
	 *
	 * @param path
	 *            The path to save the file
	 */
	public void saveGroupsFile(String path) {
		Misc.writeGroupsIndex(groups, path);
	}

	private void saveOutputGroups(String path) {
		Misc.writeOutputGroups(groups, path);
	}

	/**
	 * @param configFile
	 *            the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<DataRegister> data) {
		this.data = data;
	}

	/**
	 *
	 * @param configFile
	 * @param dataFile
	 */
	public void setData(String configFile, String dataFile) {
		this.configFile = configFile;
		this.dataFile = dataFile;
		data = TxtFunctions.readTxt(this.dataFile,
				XMLFunctions.readXML(this.configFile));
	}

	/**
	 * @param dataFile
	 *            the dataFile to set
	 */
	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<ElementsGroup> groups) {
		this.groups = groups;
	}

	public void setGroupsFile(String groupsFile) {
		this.groupsFile = groupsFile;
	}

}