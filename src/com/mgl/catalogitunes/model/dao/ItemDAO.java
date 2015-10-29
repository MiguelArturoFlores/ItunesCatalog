package com.mgl.catalogitunes.model.dao;

import java.util.ArrayList;

import com.mgl.catalogitunes.model.ItunesApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ItemDAO extends SQLiteOpenHelper {

	// String sqlCreate = "CREATE TABLE Level(id INTEGER, name TEXT)";

	public ItemDAO(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionA, int versionB) {

	}

	public void storeItemInDB(ArrayList<ItunesApp> ituneList) {
			SQLiteDatabase db = this.getReadableDatabase();
			try {

				String SQL = " delete from item ";
				db.execSQL(SQL);
				
				for(ItunesApp app : ituneList){
					
					ContentValues values = new ContentValues();
					values.put("id", app.getId());
					values.put("name",app.getName());
					values.put("image75",app.getImage75());
					values.put("image100",app.getImage100());
					values.put("company",app.getCompany());
					values.put("summary",app.getSummary());
					values.put("price",app.getPrice());
					values.put("currency",app.getCurrency());
					values.put("type",app.getType());
					values.put("category",app.getCategory());
					values.put("id",app.getId());
					
					db.insert("item", null, values);
					
				}

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				db.close();
			}
			
	}

	public ArrayList<ItunesApp> loadItemsFromDB() {
		ArrayList<ItunesApp> ituneList = new ArrayList<>();

		try {
			String SQL = " select a.name, a.image75, a.image100, a.company, a.summary, a.price, a.currency, a.type, a.category, a.id, a.image53 from item a";
			SQLiteDatabase db = this.getReadableDatabase();
			if (db == null) {
				return null;
			}
			Cursor c = this.getReadableDatabase().rawQuery(SQL, null);
			
			if (c.moveToFirst()) {
				do {
					ItunesApp app =new ItunesApp();

					app.setName(c.getString(0));
					app.setImage75(c.getString(1));
					app.setImage100(c.getString(2));
					app.setCompany(c.getString(3));
					app.setSummary(c.getString(4));
					app.setPrice(c.getString(5));
					app.setCurrency(c.getString(6));
					app.setType(c.getString(7));
					app.setCategory(c.getString(8));
					app.setId(Long.valueOf(c.getString(9)));
					app.setImage53(c.getString(10));
					
					ituneList.add(app);
				} while (c.moveToNext());
			}

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return ituneList;
	}

}
