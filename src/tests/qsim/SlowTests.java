package tests.qsim;

import org.junit.Test;

import similarity.qsim.EuclideanSimilarity;
import algorithm.qsim.QSim;

public class SlowTests {
	@Test
	public void BigSerialTest() {
		new QSim(0.8f, new EuclideanSimilarity(), "test-data/yeast-config.xml",
				"test-data/yeast-edited.txt", "test-results.txt").execute();

	}

	@Test
	public void BigParallelTest() {
		new QSim(0.8f, new EuclideanSimilarity(), "test-data/yeast-config.xml",
				"test-data/yeast-edited.txt", "test-results.txt").execute();
	}
}