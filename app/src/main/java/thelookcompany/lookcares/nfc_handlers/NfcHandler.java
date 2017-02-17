package thelookcompany.lookcares.nfc_handlers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import thelookcompany.lookcares.R;

public class NfcHandler {

	public interface ITagHandleListener {
		void tagUpdated(TagInfo tagInfo);
	}

	private Activity mActivity = null;
	private NfcAdapter mAdapter = null;

	private PendingIntent mPendingIntent = null;
	private IntentFilter[] mIntentFilters = null;
	private String[][] mTechLists = null;

	private NfcStatus mStatus = null;
	private Logger mLogger = null;

	private Map<String, IHandlerFactory> mFactoryMap = null;

	private TagInfo mTagInfo = null;

	private List<ITagHandleListener> mListeners = null;

	public NfcHandler(Activity activity, NfcStatus status, Logger logger) {
		mActivity = activity;
		mStatus = status;
		mLogger = logger;

		// Acquairing adapter
		mAdapter = (NfcAdapter) NfcAdapter.getDefaultAdapter(activity);

		if (!mAdapter.isEnabled()) {
			mStatus.setStatus("NFC is not enabled.");
		} else {
			mStatus.setStatus("Scan NFC tag");
		}

		// Creating PendingIndent for foreground dispatching
		mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(
				activity, activity.getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}

		mIntentFilters = new IntentFilter[] { ndef };
		mTechLists = new String[][] { getTechList(activity.getResources()) };

		// Handlers
		mFactoryMap = new HashMap<String, IHandlerFactory>();
		mFactoryMap.put("android.nfc.tech.MifareClassic",
				new MifareClassicFactory(mLogger, mStatus));
//		mFactoryMap.put("android.nfc.tech.NfcA", new NfcAFactory(mLogger,
//				mStatus));

		mListeners = new Vector<ITagHandleListener>();
	}

	public void onResume() {
		mAdapter.enableForegroundDispatch(mActivity, mPendingIntent,
				mIntentFilters, mTechLists);
	}

	public void onPause() {
		mAdapter.disableForegroundDispatch(mActivity);
	}

	public boolean isNfcIntent(Intent intent) {
		return NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction());
	}

	public void handleNfcIntent(Intent intent) {
		mStatus.setStatus("Handling tag...");

		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		byte[] tagId = tag.getId();

		mTagInfo = new TagInfo(tag.getTechList());

		for (String tech : tag.getTechList()) {
			mStatus.setStatus("Reading " + tech);

			if (mFactoryMap.containsKey(tech)) {
				IHandler handler = mFactoryMap.get(tech).createHandler();
				handler.handleTag(mActivity, tag);
			}
		}

		mStatus.setStatus("Done");

		for (ITagHandleListener l : mListeners) {
			l.tagUpdated(mTagInfo);
		}
	}

	public void registerTagHandleListener(ITagHandleListener listener) {
		mListeners.add(listener);
	}

	public void unregisterTagHandleListener(ITagHandleListener listener) {
		mListeners.remove(listener);
	}

	private static String[] getTechList(Resources resources) {
		ArrayList<String> list = new ArrayList<String>();
		XmlResourceParser xml = resources.getXml(R.xml.techlist);

		// Assumed simple parsing because of simple file structure
		for (;;) {
			int eventType = XmlPullParser.END_DOCUMENT;
			try {
				eventType = xml.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (eventType == XmlPullParser.TEXT) {
				list.add(xml.getText());
			} else if (eventType == XmlPullParser.END_DOCUMENT) {
				break;
			}

		}

		return list.toArray(new String[list.size()]);
	}
}
