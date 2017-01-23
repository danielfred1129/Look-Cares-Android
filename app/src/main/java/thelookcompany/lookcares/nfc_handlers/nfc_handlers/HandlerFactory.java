package thelookcompany.lookcares.nfc_handlers.nfc_handlers;


import thelookcompany.lookcares.nfc_handlers.Logger;
import thelookcompany.lookcares.nfc_handlers.NfcStatus;

public abstract class HandlerFactory implements IHandlerFactory {

	public Logger mLogger = null;
	public NfcStatus mStatus = null;
	
	public HandlerFactory(Logger logger, NfcStatus status)
	{
		mLogger = logger;
		mStatus = status;
	}
}
