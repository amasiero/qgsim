package tests.qsim;

import org.junit.Test;

import similarity.qsim.EuclideanSimilarity;
import algorithm.qsim.QSim;

public class SmallDatabaseTests {
	@Test
	public void SmallSerialTest() {
		new QSim(0.8f, new EuclideanSimilarity(), 1,
				"test-data/iris-config.xml", "test-data/iris-edited.txt",
				"test-results.txt").execute();
	}

	@Test
	public void SmallParallelTest() {
		new QSim(0.8f, new EuclideanSimilarity(), 50,
				"test-data/iris-config.xml", "test-data/iris-edited.txt",
				"test-results.txt").execute();

	}

	@Test
	public void MultipleQValuesTest() {
		for (float q = 0.1f; q <= 1f; q += 0.1f) {
			new Thread(new QSim(q, new EuclideanSimilarity(), 1,
					"test-data/iris-config.xml", "test-data/iris-edited.txt",
					"test-results.txt")).start();
		}
	}
}