package thelookcompany.lookcares.nfc_handlers;

public abstract class Handler implements IHandler {

	protected Logger mLogger = null;
	protected NfcStatus mStatus = null;
	
	protected Handler(Logger logger, NfcStatus status)
	{
		mLogger = logger;
		mStatus = status;
	}

}
