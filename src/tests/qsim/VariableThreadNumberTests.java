package tests.qsim;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import similarity.qsim.EuclideanSimilarity;
import algorithm.qsim.QSim;

public class VariableThreadNumberTests {

	@Test
	public void MultipleThreadNumbersTest() {
		List<QSim> qsims = new ArrayList<QSim>();

		for (int i = 1; i <= 10; i++) {
			QSim aux = new QSim(0.8f, new EuclideanSimilarity(), i * 10,
					"test-data/yeast-config.xml", "test-data/yeast-edited.txt",
					"test-results.txt");
			aux.execute();
			qsims.add(aux);
		}

		for (QSim qsim : qsims)
			System.out.println("T " + qsim.getThreads() + " S "
					+ (qsim.getExecutionTime() / 1000));
	}
}
