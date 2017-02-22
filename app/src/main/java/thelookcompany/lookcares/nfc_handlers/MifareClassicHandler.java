package thelookcompany.lookcares.nfc_handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import java.io.IOException;

import thelookcompany.lookcares.FrameSelectionActivity;
import thelookcompany.lookcares.utils.UserUtils;

public class MifareClassicHandler extends Handler {

	public MifareClassicHandler(Logger logger, NfcStatus status) {
		super(logger, status);
	}

	public void handleTag(Activity activity, Tag tag) {
		
		MifareClassic mfc = MifareClassic.get(tag);
		String serialKey = null;//0ee7cef5d20804006263646566676869

		try {
			mfc.connect();

			mStatus.setStatus("Connected");

			mLogger.pushStatus("");
			mLogger.pushStatus("== MifareClassic Info == ");
			mLogger.pushStatus("Size: " + mfc.getSize());
			mLogger.pushStatus("Timeout: " + mfc.getTimeout());
			mLogger.pushStatus("Type: " + mfc.getType());
			mLogger.pushStatus("BlockCount: " + mfc.getBlockCount());
			mLogger.pushStatus("MaxTransceiveLength: " + mfc.getMaxTransceiveLength());
			mLogger.pushStatus("SectorCount: " + mfc.getSectorCount());

			for(int i = 0; i < mfc.getSectorCount(); ++i) {
				mLogger.pushStatus("BlockCount in sector " + i + ": " + mfc.getBlockCountInSector(i));
			}

			mStatus.setStatus("Reading sectors...");

			for(int i = 0; i < mfc.getSectorCount(); ++i) {

				if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
					mLogger.pushStatus("Authorization granted to sector " + i + " with MAD key");
				} else if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
					mLogger.pushStatus("Authorization granted to sector " + i + " with DEFAULT key");
				} else if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_NFC_FORUM)) {
					mLogger.pushStatus("Authorization granted to sector " + i + " with NFC_FORUM key");
				} else {
					mLogger.pushStatus("Authorization denied to sector " + i);
					continue;
				}


				for(int k = 0; k < mfc.getBlockCountInSector(i); ++k)
				{
					int block = mfc.sectorToBlock(i) + k;

					byte[] data = null;

					try {

						data = mfc.readBlock(block);
					} catch (IOException e) {
						mLogger.pushStatus("Block " + block + " data: " + e.getMessage());
						continue;
					}

					String blockData = Common.getHexString(data);
					mLogger.pushStatus("Block " + block + " data: " + blockData);

                    if ((data.length > 0) && (serialKey == null)) {
                        serialKey = blockData;
                    }
                }
			}
			if (serialKey != null) {
				handleResult(activity, serialKey);
			}

			mfc.close();

			mLogger.pushStatus("");

		} catch (IOException e) {
			mLogger.pushStatus(e.getMessage());
		} finally {
			try {
				mfc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void handleResult(final Activity activity, final String serialKey) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Serial Number");
		builder.setMessage(serialKey);

		builder.setPositiveButton("Use this scan", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//TODO
				UserUtils.storeSelectedBarcode(activity, serialKey);
				if (((FrameSelectionActivity)activity).value.equals("FRAME"))
					((FrameSelectionActivity)activity).getFrameWithSerialNumber(serialKey);
				else if (((FrameSelectionActivity)activity).value.equals("FABRIC"))
					((FrameSelectionActivity)activity).getFabricWithSerialNumber(serialKey);
			}
		});
		builder.setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//TODO
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
