package models.qsim;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	protected String nameAttribute;
	protected AttributeType type;
	protected List<Item> itens;
	
	public Attribute(){
		super();
		this.itens = new ArrayList<Item>();
	}

	/**
	 * @return the nameAttribute
	 */
	public String getNameAttribute() {
		return nameAttribute;
	}

	/**
	 * @param nameAttribute the nameAttribute to set
	 */
	public void setNameAttribute(String nameAttribute) {
		this.nameAttribute = nameAttribute;
	}

	/**
	 * @return the type
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AttributeType type) {
		this.type = type;
	}

	/**
	 * @return the itens
	 */
	public List<Item> getItens() {
		return itens;
	}

	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<Item> itens) {
		this.itens = itens;
	}
	
}
