package models.qsim;

import java.util.ArrayList;
import java.util.List;

public class ElementsGroup {

	private List<Short> elements;
	
	public ElementsGroup(){
		super();
		this.elements = new ArrayList<Short>();
	}
	
	public List<Short> getElements() {
		return elements;
	}
	
	public void setElements(List<Short> elements) {
		this.elements = elements;
	}
	
	public int getSize(){
		return this.elements.size();
	}
}
