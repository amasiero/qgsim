package models.qsim;

public class Item {

	protected String nameItem;
	protected int codeItem;

	/**
	 * @return the codeItem
	 */
	public int getCodeItem() {
		return codeItem;
	}

	/**
	 * @return the nameItem
	 */
	public String getNameItem() {
		return nameItem;
	}

	/**
	 * @param codeItem
	 *            the codeItem to set
	 */
	public void setCodeItem(int codeItem) {
		this.codeItem = codeItem;
	}

	/**
	 * @param nameItem
	 *            the nameItem to set
	 */
	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

}
