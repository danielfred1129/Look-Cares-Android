package thelookcompany.lookcares.nfc_handlers;

public class MifareClassicFactory extends HandlerFactory {
	
	public MifareClassicFactory(Logger logger, NfcStatus status) {
		super(logger, status);
	}

	public IHandler createHandler() {
		return new MifareClassicHandler(mLogger, mStatus);
	}
	
}
