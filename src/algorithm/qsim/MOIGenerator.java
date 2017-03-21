package algorithm.qsim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import models.qsim.ElementsGroup;
import utils.qsim.Misc;

/**
 * Generates maximum object intersections for Q-SIM related sets. This class
 * implements the {@linkplain java.util.concurrent.Callable} interface, which
 * means it can be executed in parallel in case there is the need to calculate
 * intersections for a large group of related sets
 * 
 * @author Douglas De Rizzo meneghetti
 */
public class MOIGenerator implements Callable<ElementsGroup> {

	private List<ElementsGroup> relatedSets;
	private short index;

	public short getIndex() {
		return index;
	}

	public MOIGenerator(List<ElementsGroup> relatedSets, short index) {
		this.relatedSets = relatedSets;
		this.index = index;
	}

	public ElementsGroup generateMOI() {
		System.out.println("Generating MOI for related set " + index);
		// Recovery index from all related objects
		List<Short> aArray = relatedSets.get(index).getElements();

		// Checks if the list aArray is empty
		if (!aArray.isEmpty()) {

			// Recovery index from all related objects of the first
			// object
			// in aArray
			List<Short> bArray = relatedSets.get(aArray.get(0)).getElements();
			
			List<Short> cArray;
			
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
					cArray = Misc.intersection(aArray, bArray);
					if (auxArray.size() < cArray.size()) {
						// Save a great intersection
						auxArray = cArray;
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
		System.out.println("Generated MOI for related set " + index);
		return temp;
	}

	@Override
	public ElementsGroup call() throws Exception {
		return generateMOI();
	}
}
