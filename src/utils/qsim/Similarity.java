package utils.qsim;

import java.util.List;

public abstract class Similarity {
	public abstract Object centroid(List<Integer> aux, List<?> data);

	public abstract double[][] similarity(List<?> data);
}
