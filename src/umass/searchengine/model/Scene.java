package umass.searchengine.model;

public class Scene {
	private String playId;
	private String sceneId;
	private int sceneNum;
	private String text;

	/**
	 * 
	 */
	public Scene() {
		super();
	}
	
	public Scene(String playId, String sceneId, int sceneNumber, String text) {
		super();
		this.playId = playId;
		this.sceneId = sceneId;
		this.sceneNum = sceneNumber;
		this.text = text;
	}

	/**
	 * @return the playId
	 */
	public String getPlayId() {
		return playId;
	}

	/**
	 * @return the sceneId
	 */
	public String getSceneId() {
		return sceneId;
	}

	/**
	 * @return the sceneNumber
	 */
	public int getSceneNum() {
		return sceneNum;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param playId the playId to set
	 */
	public void setPlayId(String playId) {
		this.playId = playId;
	}

	/**
	 * @param sceneId the sceneId to set
	 */
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	/**
	 * @param sceneNum the sceneNum to set
	 */
	public void setSceneNum(int sceneNum) {
		this.sceneNum = sceneNum;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Scene [playId=" + playId + ", sceneId=" + sceneId + ", sceneNum=" + sceneNum + ", text=" + text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((playId == null) ? 0 : playId.hashCode());
		result = prime * result + ((sceneId == null) ? 0 : sceneId.hashCode());
		result = prime * result + (int) (sceneNum ^ (sceneNum >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scene other = (Scene) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (playId == null) {
			if (other.playId != null)
				return false;
		} else if (!playId.equals(other.playId))
			return false;
		if (sceneId == null) {
			if (other.sceneId != null)
				return false;
		} else if (!sceneId.equals(other.sceneId))
			return false;
		if (sceneNum != other.sceneNum)
			return false;
		return true;
	}
	
}
