package com.mgl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.mgl.catalogitunes.model.ItunesApp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageCacheSingleton {

	private static ImageCacheSingleton instance = null;
	private Context context;

	private HashMap<String, Bitmap> imageHash;

	private ImageCacheSingleton(Context context) {
		this.context = context;
		imageHash = new HashMap<>();
	}

	public static ImageCacheSingleton getInstance(Context context) {
		try {

			if (instance == null) {
				instance = new ImageCacheSingleton(context);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public Bitmap getImage(Long urldisplay) {
		try {

			return imageHash.get(urldisplay+".png");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addImage(String url, Bitmap result, String id) {
		try {

			imageHash.put(id+".png", result);
			saveToInternalSorage(result, id);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void saveToInternalSorage(Bitmap bitmapImage, String id) {

		try {
			ContextWrapper cw = new ContextWrapper(context);
			// path to /data/data/com.mgl.catalogitunes/app_data/imageDir
			File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
			// Create imageDir
			File mypath = new File(directory, id + ".png");

			FileOutputStream fos = null;

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return directory.getAbsolutePath();
	}

	public void loadImagesFromInternal(ArrayList<ItunesApp> appList) {
		try {
			String path = "/data/data/com.mgl.catalogitunes/app_imageDir";

			for (ItunesApp app : appList) {
				File f = new File(path, app.getId() + ".png");
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
				imageHash.put(app.getId()+".png", b);
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
