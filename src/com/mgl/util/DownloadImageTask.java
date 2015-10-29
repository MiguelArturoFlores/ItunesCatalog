package com.mgl.util;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView bmImage;

	private boolean storeInCache = false;
	private String url;
	private Long idImage;
	
	public DownloadImageTask(ImageView bmImage, boolean storeInCache, Long idImage, int t) {
		this.bmImage = bmImage;
		this.storeInCache = storeInCache;
		this.idImage = idImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		url = urldisplay;
		Bitmap mIcon11 = null;
		try {
			
			//search bitmap in cache, if its in cache return 
			mIcon11 = ImageCacheSingleton.getInstance(null).getImage(idImage);
			if(mIcon11!=null){
				return mIcon11;
			}
			
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		try {
			bmImage.setImageBitmap(result);
			if(storeInCache){
				ImageCacheSingleton.getInstance(null).addImage(url,result,this.idImage+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
