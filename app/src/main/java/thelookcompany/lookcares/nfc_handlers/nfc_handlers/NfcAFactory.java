package thelookcompany.lookcares.nfc_handlers.nfc_handlers;

import thelookcompany.lookcares.nfc_handlers.Logger;
import thelookcompany.lookcares.nfc_handlers.NfcStatus;

public class NfcAFactory extends HandlerFactory {

	public NfcAFactory(Logger logger, NfcStatus status) {
		super(logger, status);
	}

	public IHandler createHandler() {
		return new NfcAHandler(mLogger, mStatus);
	}

}
