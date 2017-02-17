package thelookcompany.lookcares.nfc_handlers;

public abstract class HandlerFactory implements IHandlerFactory {

	public Logger mLogger = null;
	public NfcStatus mStatus = null;
	
	public HandlerFactory(Logger logger, NfcStatus status)
	{
		mLogger = logger;
		mStatus = status;
	}
}
