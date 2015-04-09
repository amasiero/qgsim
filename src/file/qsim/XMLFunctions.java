package file.qsim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.qsim.Attribute;
import models.qsim.AttributeType;
import models.qsim.Item;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLFunctions {

	public static List<Attribute> readXML(String fileName) {
		try {
			List<Attribute> attributes = new ArrayList<Attribute>();
			File file = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("attribute");

			for (int i = 0; i < nList.getLength(); i++) {

				Attribute temp = new Attribute();
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					temp.setNameAttribute(eElement.getElementsByTagName("name")
							.item(0).getTextContent());

					switch (eElement.getElementsByTagName("type").item(0)
							.getTextContent().toLowerCase()) {
							case "number":
								temp.setType(AttributeType.NUMBER);
								break;
							case "decimal":
								temp.setType(AttributeType.DECIMAL);
								break;
							case "class":
								temp.setType(AttributeType.CLASS);
								break;
					}

					if (eElement.getElementsByTagName("type").item(0)
							.getTextContent().toLowerCase().equals("class")) {
						NodeList nListAttributes = eElement
								.getElementsByTagName("item");
						List<Item> itensTemp = new ArrayList<Item>();

						for (int j = 0; j < nListAttributes.getLength(); j++) {
							Node nNodeItem = nListAttributes.item(j);
							Item itemTemp = new Item();

							if (nNodeItem.getNodeType() == Node.ELEMENT_NODE) {
								Element eElementItem = (Element) nNodeItem;
								itemTemp.setNameItem(eElementItem
										.getElementsByTagName("itemValue")
										.item(0).getTextContent());
								itemTemp.setCodeItem(Integer
										.parseInt(eElementItem
												.getElementsByTagName(
														"codValue").item(0)
														.getTextContent()));

							}

							itensTemp.add(itemTemp);
						}
						temp.setItens(itensTemp);
					}
				}
				attributes.add(temp);
			}

			return attributes;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
