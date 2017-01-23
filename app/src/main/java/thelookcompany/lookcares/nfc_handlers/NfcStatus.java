package thelookcompany.lookcares.nfc_handlers;

import java.util.List;
import java.util.Vector;

public class NfcStatus {

	public interface IStatusListener {
		
		void update(String text);
	}
	
	private String mText = null;
	private List<IStatusListener> mListeners = null;
	
	public NfcStatus()
	{
		mText = new String();
		mListeners = new Vector<IStatusListener>();
	}
	
	public void setStatus(String status)
	{
		mText = status;
		
    	for(IStatusListener l : mListeners) {
    		l.update(mText);
    	}
	}
	
    public void registerListener(IStatusListener listener) {
    	mListeners.add(listener);
    }
    
    public final String getText() {
    	return mText;
    }
    
    public void unregisterListener(Logger.ILoggerListener listener) {
    	mListeners.remove(listener);
    }
}
