package thelookcompany.lookcares.nfc_handlers;

public class TagInfo {

	private String[] mTechList = null;
	
	public TagInfo(String[] techList) {
		mTechList = techList;
	}
	
	public String[] getTechList() {
		return mTechList;
	}
	
}
