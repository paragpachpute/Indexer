package umass.searchengine.ranking;

public class Dirichlet implements Scorer {
	private double mu;
	private int C; // collection count

	/**
	 * @param mu
	 * @param c
	 */
	public Dirichlet(double mu, int c) {
		super();
		this.mu = mu;
		C = c;
	}

	@Override
	public double score(int ni, int fi, int qfi, int cQi, int dl) {
		return Math.log((fi + mu * cQi / C) / (dl + mu));
	}

	@Override
	public boolean isOutputLogScaled() {
		return true;
	}
}
