package algorithm.qsim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import utils.qsim.Misc;
import models.qsim.ElementsGroup;

/**
 * Generates maximum object intersections for Q-SIM related sets. This class
 * implements the {@linkplain java.util.concurrent.Callable} interface, which means it can be
 * executed in parallel in case there is the need to calculate intersections for
 * a large group of related sets
 * 
 * @author Douglas De Rizzo meneghetti
 */
public class MOIGenerator implements Callable<ElementsGroup> {

	private List<ElementsGroup> relatedSets;
	private int i;

	public MOIGenerator(List<ElementsGroup> relatedSets, int i) {
		this.relatedSets = relatedSets;
		this.i = i;
	}

	public ElementsGroup generateMOI() {
		// Recovery index from all related objects
		List<Short> aArray = relatedSets.get(i).getElements();

		// Checks if the list aArray is empty
		if (!aArray.isEmpty()) {

			// Recovery index from all related objects of the first
			// object
			// in aArray
			List<Short> bArray = relatedSets.get(aArray.get(0)).getElements();

			// Time safe object to insert into reduced
			short c = aArray.get(0);

			// Initialize reduced array that contains moi of this
			// object
			List<Short> reduced = new ArrayList<Short>();

			boolean finish = true;
			while (finish) {
				// Store the intersection between aArray and bArray
				List<Short> auxArray = Misc.intersection(aArray, bArray);
				// Roam the rest of aArray elements to find the
				// greatest
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

				// Add c object into reduced array and prepare to
				// the next
				// interaction
				if (!auxArray.isEmpty()) {
					if (!reduced.contains(c))
						reduced.add(c);
					aArray = auxArray;
					bArray = relatedSets.get(aArray.get(0)).getElements();
					c = aArray.get(0);
				} else {
					reduced.add(aArray.get(0));
					finish = false;
				}
			}
		}
		// Update moi matrix with reduced array from i object
		ElementsGroup temp = new ElementsGroup();
		temp.setElements(aArray);
		return temp;
	}

	@Override
	public ElementsGroup call() throws Exception {
		return generateMOI();
	}
}
