package com.mgl.util;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	public static String DB_PATH = "/data/data/com.mgl.catalogitunes/databases/";
	public static String DB_NAME = "imaginamosDB";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	
	public static final int DB_VERSION = 1;

	private boolean isUpgrade = false;

	public DatabaseManager(Context contexto, String nombre, CursorFactory factory,
			int version) {

		super(contexto, nombre, factory, version);
		this.myContext = contexto;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// No hacemos nada aqui
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {

			Log.d("UPGRADING OLD DATABASE", "UPDATING from oldversion "
					+ oldVersion + " to " + newVersion);


			isUpgrade = true;
			Log.d("UPGRADING OLD DATABASE2", "UPDATING from oldversion "
					+ oldVersion + " to " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createDataBase(Context context) throws IOException {

		boolean dbExist = checkDataBase(context);

		Log.d("Creating database", "Creating database");

		if (dbExist) {
			// Si existe, no hacemos nada!

		} else {
			// Llamando a este método se crea la base de datos vacía en la ruta
			// por defecto del sistema de nuestra aplicación por lo que
			// podremos sobreescribirla con nuestra base de datos.
			// this.getReadableDatabase();

			try {

				copyDataBase(context);

			} catch (IOException e) {

				throw new Error("Error copiando database");
			}
		}
	}

	public void overrideDATABASE(Context context) throws IOException {

		boolean dbExist = checkDataBase(context);

		// Llamando a este método se crea la base de datos vacía en la ruta
		// por defecto del sistema de nuestra aplicación por lo que
		// podremos sobreescribirla con nuestra base de datos.
		this.getReadableDatabase();

		try {

			copyDataBase(context);

		} catch (IOException e) {

			throw new Error("Error copiando database");
		}

	}

	private boolean checkDataBase(Context context) {

		SQLiteDatabase checkDB = null;

		try {
			if(android.os.Build.VERSION.SDK_INT >= 17){
	            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
	        
			}
	        else{
	            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	        
	        }
			
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS
							| SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// Base de datos no creada todavia

		}

		if (checkDB != null) {

			checkDB.close();
		}

		return checkDB != null ? true : false;

	}

	public void openDataBase(Context context) throws SQLException {

		// Open the database
		try {

			if(android.os.Build.VERSION.SDK_INT >= 17){
	            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
	         
	            System.out.println("databse path >= 17 "+DB_PATH);
			}
	        else{
	            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	         
	            System.out.println("databse path < 17 "+DB_PATH);
	        }
			
			
			String myPath = DB_PATH + DB_NAME;
			
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS
							| SQLiteDatabase.OPEN_READONLY);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	private void copyDataBase(Context context) throws IOException {

		//SceneManagerSingleton.getInstance().getActivity().getDatabasePath(name)
		String dbPathAux = new String();
		if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            dbPathAux = context.getApplicationInfo().dataDir + "/databases";
            System.out.println("databse path >= 17 "+DB_PATH);
		}
        else{
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            dbPathAux = "/data/data/" + context.getPackageName() + "/databases";
            System.out.println("databse path < 17 "+DB_PATH);
        }
		File databaseFile = new File( dbPathAux);
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()){
            databaseFile.mkdir();
        }
	
		
            		//"/data/data/com.mgl.crappypigeon/databases/";
            
		OutputStream databaseOutputStream = new FileOutputStream("" + DB_PATH
				+ DB_NAME);
		InputStream databaseInputStream;

		byte[] buffer = new byte[1024];
		int length;

		databaseInputStream = myContext.getAssets().open(DB_NAME);
		while ((length = databaseInputStream.read(buffer)) > 0) {
			databaseOutputStream.write(buffer);
		}

		databaseInputStream.close();
		databaseOutputStream.flush();
		databaseOutputStream.close();
	}

	public static String getDB_PATH() {
		return DB_PATH;
	}

	public static void setDB_PATH(String dB_PATH) {
		DB_PATH = dB_PATH;
	}

	public static String getDB_NAME() {
		return DB_NAME;
	}

	public static void setDB_NAME(String dB_NAME) {
		DB_NAME = dB_NAME;
	}

	public SQLiteDatabase getMyDataBase() {
		return myDataBase;
	}

	public void setMyDataBase(SQLiteDatabase myDataBase) {
		this.myDataBase = myDataBase;
	}

	public boolean isUpgrade() {
		return isUpgrade;
	}

	public void setUpgrade(boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public Context getMyContext() {
		return myContext;
	}

}