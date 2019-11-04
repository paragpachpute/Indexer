package umass.searchengine.ranking;

public class BM25 implements Scorer {
	private double k1, k2, K, avdl, b;
	private int N; // numOfDocs

	/**
	 * @param k1
	 * @param k2
	 * @param n
	 */
	public BM25(double k1, double k2, double b, double avdl, int n) {
		super();
		this.k1 = k1;
		this.k2 = k2;
		this.b = b;
		this.avdl = avdl;
		this.N = n;
	}

	@Override
	public double score(int ni, int fi, int qfi, int cQi, int dl) {
		K = k1 * ((1-b) + b*dl/avdl);
		double firstTerm = Math.log((N - ni + 0.5) / (ni + 0.5));
		double secondTerm = ((k1 + 1) * fi / (K + fi));
		double thirdTerm = ((k2 + 1) * qfi / (k2 + qfi));
		return firstTerm * secondTerm * thirdTerm;
	}

	@Override
	public boolean isOutputLogScaled() {
		return false;
	}

}
