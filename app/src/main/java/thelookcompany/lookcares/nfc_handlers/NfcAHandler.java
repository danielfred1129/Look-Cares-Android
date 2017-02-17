package thelookcompany.lookcares.nfc_handlers;

import android.app.Activity;
import android.nfc.Tag;
import android.nfc.tech.NfcA;

import java.io.IOException;

public class NfcAHandler extends Handler {

	public NfcAHandler(Logger logger, NfcStatus status) {
		super(logger, status);
	}
	
	public void handleTag(Activity activity, Tag tag) {
		NfcA nfca = NfcA.get(tag);
		
		try {
			nfca.connect();
			
			mStatus.setStatus("Connected");
			
			mLogger.pushStatus("");
			mLogger.pushStatus("== NfcA Info == ");
			mLogger.pushStatus("Sak: " + nfca.getSak());
			mLogger.pushStatus("Atqa:\n" + Common.getHexString(nfca.getAtqa()));

		} catch (IOException e) {
			mStatus.setStatus(e.getMessage());
			
			try {
				nfca.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
