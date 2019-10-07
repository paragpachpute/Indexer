package umass.searchengine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {
	
	private List<Scene> corpus;

	public Map<String, List<Integer>> getScenesForPlay() {
		Map<String, List<Integer>> playSceneMap = new HashMap<>();
		for (Scene scene : corpus) {
			if (playSceneMap.containsKey(scene.getPlayId())) 
				playSceneMap.get(scene.getPlayId()).add(scene.getSceneNum());
			else {
				List<Integer> list = new ArrayList<>();
				list.add(scene.getSceneNum());
				playSceneMap.put(scene.getPlayId(), list);
			}
				
		}
		return playSceneMap;
	}
	
	public String getSceneNameFromNumber(int num) {
		return corpus.stream().filter(scene -> scene.getSceneNum() == num).map(scene -> scene.getSceneId()).findFirst().get();
	}

	/**
	 * @return the scenes
	 */
	public List<Scene> getCorpus() {
		return corpus;
	}

	/**
	 * @param scenes the scenes to set
	 */
	public void setCorpus(List<Scene> scenes) {
		this.corpus = scenes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Corpus [scenes=" + corpus + "]";
	}
}
