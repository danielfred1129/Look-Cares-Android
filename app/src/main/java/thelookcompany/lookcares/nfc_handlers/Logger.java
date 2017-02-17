package thelookcompany.lookcares.nfc_handlers;

import java.util.List;
import java.util.Vector;
import com.bugfender.sdk.Bugfender;

public class Logger {
	
	public interface ILoggerListener {
		
		void update(String text);
	}
	
	private String mText = null;
	private List<ILoggerListener> mListeners = null;
	
	public Logger() {
		mText = new String();
		mListeners = new Vector<ILoggerListener>();
	}
	
    public void pushStatus(final String text) {
    	mText += "\n" + text;

		Bugfender.sendIssue("Log", text);

		for(ILoggerListener l : mListeners) {
    		l.update(mText);
    	}
    }
     
    public void registerListener(ILoggerListener listener) {
    	mListeners.add(listener);
    }
    
    public void unregisterListener(ILoggerListener listener) {
    	mListeners.remove(listener);
    }
    
    public final String getText() {
    	return mText;
    }
}
