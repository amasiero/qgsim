package tests.qsim;

import org.junit.Test;

import similarity.qsim.EuclideanSimilarity;
import similarity.qsim.PNorm;
import algorithm.qsim.QSim;

public class QuickTests {
	@Test
	public void BaseTest() {
		new QSim(0.8f, new EuclideanSimilarity(), "test-data/iris-config.xml",
				"test-data/iris-edited.txt").execute();

	}

	@Test
	public void MultipleQValuesTest() {
		for (float q = 0.1f; q <= 1f; q += 0.1f) {
			new QSim(q, new EuclideanSimilarity(), "test-data/iris-config.xml",
					"test-data/iris-edited.txt").execute();
		}
	}

	@Test
	public void MultiplePValuesTest() {
		for (int p = 1; p <= 5; p += 1) {
			new QSim(0.8f, new PNorm(p), "test-data/iris-config.xml",
					"test-data/iris-edited.txt").execute();
		}
	}
}