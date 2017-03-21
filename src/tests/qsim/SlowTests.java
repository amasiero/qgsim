package tests.qsim;

import org.junit.Test;

import similarity.qsim.EuclideanSimilarity;
import similarity.qsim.PNorm;
import algorithm.qsim.QSim;

public class SlowTests {

	@Test
	public void BigDataTest() {
		new QSim(0.8f, new EuclideanSimilarity(), "test-data/yeast-config.xml",
				"test-data/yeast-edited.txt").execute();
	}

	@Test
	public void MultipleQPValuesTest() {
		for (float q = 0.1f; q <= 1f; q += 0.1f) {
			for (int p = 1; p <= 5; p += 1) {
				new QSim(q, new PNorm(p), "test-data/iris-config.xml",
						"test-data/iris-edited.txt").execute();
			}
		}
	}
}