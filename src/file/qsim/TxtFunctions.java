package file.qsim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.qsim.Attribute;
import models.qsim.AttributeType;
import models.qsim.DataRegister;
import models.qsim.Item;

public class TxtFunctions {

	public static List<DataRegister> readTxt(String fileName,
			List<Attribute> attributes) {
		try {
			// Load file
			File file = new File(fileName);
			// Prepare the file to be read
			FileReader fr = new FileReader(file);
			// Prepare the reading buffer of the file
			BufferedReader br = new BufferedReader(fr);
			// Initialize
			List<DataRegister> data = new ArrayList<DataRegister>();
			String line = new String();

			while ((line = br.readLine()) != null) {
				List<Float> register = new ArrayList<Float>();
				for (int i = 0; i < line.split(",").length; i++) {
					if ((attributes.get(i).getType() == AttributeType.DECIMAL)
							|| (attributes.get(i).getType() == AttributeType.NUMBER)) {
						register.add(Float.parseFloat(line.split(",")[i]));
					} else {
						for (Item item : attributes.get(i).getItens()) {
							if (item.getNameItem().equals(line.split(",")[i])) {
								register.add((float) item.getCodeItem());
							}
						}
					}
				}
				DataRegister dr = new DataRegister();
				dr.setRegister(register);
				data.add(dr);
			}

			fr.close();
			br.close();
			return data;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
