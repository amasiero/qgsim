package utils.qsim;

import java.util.List;

public abstract class Similarity {
	public abstract double[][] similarity(List<?> data);

	public abstract Object centroid(List<Integer> aux, List<?> data);
}
