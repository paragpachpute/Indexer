package umass.searchengine.ranking;

public class CountScores implements Scorer {

	@Override
	public double score(int ni, int fi, int qfi, int cQi, int dl) {
		return fi;
	}

	@Override
	public boolean isOutputLogScaled() {
		return false;
	}

}
