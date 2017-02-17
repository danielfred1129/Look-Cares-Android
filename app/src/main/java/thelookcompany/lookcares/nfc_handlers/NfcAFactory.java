package thelookcompany.lookcares.nfc_handlers;

public class NfcAFactory extends HandlerFactory {

	public NfcAFactory(Logger logger, NfcStatus status) {
		super(logger, status);
	}

	public IHandler createHandler() {
		return new NfcAHandler(mLogger, mStatus);
	}

}
