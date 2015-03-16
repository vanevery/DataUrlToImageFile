package com.mobvcasting.DataUrlToImageFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

/**
 * DataUrlToImageFile.java
 *
 * @author Shawn Van Every <shawn@mobvcasting.com>
 */
public class DataUrlToImageFile extends CordovaPlugin {
	public static final String ACTION = "saveImageData";

	@Override
	public boolean execute(String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {

		if (action.equals(ACTION)) {

			String base64 = data.optString(0);
			if (base64.equals("")) // isEmpty() requires API level 9
				callbackContext.error("Missing base64 string");
				
			String format = data.optString(1);
			if (format.equals("")) 
				callbackContext.error("Missing format string");
			
			// Create the bitmap from the base64 string
			Log.d("DataUrlToImageFile", base64);
			byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
			Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			if (bmp == null) {
				callbackContext.error("The image could not be decoded");
			} else {
				// Save the image
				File imageFile = savePhoto(bmp, format);
				if (imageFile == null)
					callbackContext.error("Error while saving image");
				
				// Update image gallery
				scanPhoto(imageFile);
				
				callbackContext.success(imageFile.getAbsolutePath());
			}
			
			return true;
		} else {
			return false;
		}
	}

	private File savePhoto(Bitmap bmp, String format) {
		File retVal = null;
		
		try {
			Calendar c = Calendar.getInstance();
			String date = "" + c.get(Calendar.DAY_OF_MONTH)
					+ c.get(Calendar.MONTH)
					+ c.get(Calendar.YEAR)
					+ c.get(Calendar.HOUR_OF_DAY)
					+ c.get(Calendar.MINUTE)
					+ c.get(Calendar.SECOND);

			String deviceVersion = Build.VERSION.RELEASE;
			Log.i("DataUrlToImageFile", "Android version " + deviceVersion);
			int check = deviceVersion.compareTo("2.3.3");

			File folder;
			/*
			 * File path = Environment.getExternalStoragePublicDirectory(
			 * Environment.DIRECTORY_PICTURES ); //this throws error in Android
			 * 2.2
			 */
			if (check >= 1) {
				folder = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				
				if(!folder.exists()) {
					folder.mkdirs();
				}
			} else {
				folder = Environment.getExternalStorageDirectory();
			}
	
			File imageFile = null;
			
			String extension = ".jpg";
			Bitmap.CompressFormat compressionFormat = Bitmap.CompressFormat.JPEG;
						
			if (format.equals("image/png")) {
			
				extension = ".png";
				compressionFormat = Bitmap.CompressFormat.PNG;					
				
			} else if (format.equals("image/webp")) {
			
				extension = ".webp";
				compressionFormat = Bitmap.CompressFormat.WEBP;
								
			}

			imageFile = new File(folder, date.toString() + extension);
			FileOutputStream out = new FileOutputStream(imageFile);
			bmp.compress(compressionFormat, 100, out);

			out.flush();
			out.close();

			retVal = imageFile;
			
		} catch (Exception e) {
			Log.e("DataUrlToImageFile", "An exception occured while saving image: "
					+ e.toString());
		}
		return retVal;
	}
	
	/* Invoke the system's media scanner to add your photo to the Media Provider's database, 
	 * making it available in the Android Gallery application and to other apps. */
	private void scanPhoto(File imageFile)
	{
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(imageFile);
	    mediaScanIntent.setData(contentUri);	      		  
	    cordova.getActivity().sendBroadcast(mediaScanIntent);
	} 
}
