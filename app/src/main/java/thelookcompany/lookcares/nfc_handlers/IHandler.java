package thelookcompany.lookcares.nfc_handlers;

import android.app.Activity;
import android.nfc.Tag;

public interface IHandler {
	
	public void handleTag(Activity activity, Tag tag);
}
