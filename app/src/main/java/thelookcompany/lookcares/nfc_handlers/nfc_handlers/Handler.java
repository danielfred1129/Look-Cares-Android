package thelookcompany.lookcares.nfc_handlers.nfc_handlers;


import thelookcompany.lookcares.nfc_handlers.Logger;
import thelookcompany.lookcares.nfc_handlers.NfcStatus;

public abstract class Handler implements IHandler {

	protected Logger mLogger = null;
	protected NfcStatus mStatus = null;
	
	protected Handler(Logger logger, NfcStatus status)
	{
		mLogger = logger;
		mStatus = status;
	}

}
