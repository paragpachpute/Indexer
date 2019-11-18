package umass.searchengine.ranking;

public class Dirichlet implements Scorer {
	private double mu;
	private long C; // collection count

	/**
	 * @param mu
	 * @param c
	 */
	public Dirichlet(double mu, long c) {
		super();
		this.mu = mu;
		this.C = c;
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
