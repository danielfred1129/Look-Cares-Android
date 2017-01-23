package thelookcompany.lookcares.nfc_handlers.nfc_handlers;

import thelookcompany.lookcares.nfc_handlers.Logger;
import thelookcompany.lookcares.nfc_handlers.NfcStatus;

public class MifareClassicFactory extends HandlerFactory {
	
	public MifareClassicFactory(Logger logger, NfcStatus status) {
		super(logger, status);
	}

	public IHandler createHandler() {
		return new MifareClassicHandler(mLogger, mStatus);
	}
	
}
