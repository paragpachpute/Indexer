package umass.searchengine.ranking;

public class JelinekMercer implements Scorer {
	private double lambda;
	private int C; // collection count

	/**
	 * @param lambda
	 * @param c
	 */
	public JelinekMercer(double lambda, int c) {
		super();
		this.lambda = lambda;
		C = c;
	}

	@Override
	public double score(int ni, int fi, int qfi, int cQi, int dl) {
		return Math.log((1 - lambda) * fi / dl + lambda * cQi / C);
	}

	@Override
	public boolean isOutputLogScaled() {
		return true;
	}

}
