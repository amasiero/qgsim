package models.qsim;

import java.util.ArrayList;
import java.util.List;

public class ElementsGroup {

	private List<Short> elements;

	public ElementsGroup() {
		super();
		elements = new ArrayList<Short>();
	}

	public List<Short> getElements() {
		return elements;
	}

	public int getSize() {
		return elements.size();
	}

	public void setElements(List<Short> elements) {
		this.elements = elements;
	}

	public boolean contains(short i) {
		return elements.contains(i);
	}
}
