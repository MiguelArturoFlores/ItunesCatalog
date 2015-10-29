package com.mgl.catalogitunes;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mgl.catalogitunes.model.ItunesApp;
import com.mgl.catalogitunes.model.dao.ItemDAO;
import com.mgl.catalogitunes.view.adapter.CatalogListAdapter;
import com.mgl.util.DatabaseManager;
import com.mgl.util.ImageCacheSingleton;
import com.mgl.webservice.util.JsonParser;
import com.mgl.webservice.util.VolleyRequestQueueSingleton;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String ITUNES_TOP20_WEBSERVICE = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

	private ArrayList<ItunesApp> ituneList;
	private ListView list;
	private GridView grid;

	private CatalogListAdapter adapter;

	private boolean isTablet = false;

	public static final String ITUNES_SELECTED = "com.mgl.catalogitunes.ITUNES_SELECTED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			// initDB, copy from assets or upgrade or do nothing if already
			// exist
			verifyDatabase();

			// init ImageChacheSingleton
			ImageCacheSingleton.getInstance(this);

			// validate if is tablet or smartphone
			isTablet = getResources().getBoolean(R.bool.isTablet);
			if (isTablet) {
				initTablet();

			} else {
				initSmartphone();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}

			requestItunesTop();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initTablet() {
		try {

			setContentView(R.layout.activity_tablet_main);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			createGridView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSmartphone() {
		try {

			setContentView(R.layout.activity_main);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			createListView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// ===========================================================================

	private void verifyDatabase() {
		DatabaseManager db2 = new DatabaseManager(getContext(),
				DatabaseManager.DB_NAME, null, DatabaseManager.DB_VERSION);
		try {

			db2.openDataBase(getContext());
			db2.createDataBase(getContext());
			db2.getReadableDatabase();

			db2.close();

			db2.openDataBase(getContext());

			if (db2.isUpgrade()) {

				// TODO upgrade logic
			}

			db2.close();
			//

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buttonRequest(View view) {
		try {

			requestItunesTop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void requestItunesTop() {
		try {

			// not connected
			if (!isNetworkConnected()) {

				Toast.makeText(this,
						getResources().getString(R.string.no_connection),
						Toast.LENGTH_SHORT).show();

				loadItemsFromDB();
				//only load images from storage if we dont have internet connection otherwise we will download them, and keep it on memory
				loadImagesFromInternalStorage();
				
				updateListView();
				
				return;
			}

			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET, ITUNES_TOP20_WEBSERVICE,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							try {

								ituneList = JsonParser
										.parseItunesReques(response);
								updateListView();
								storeItemInDB();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("JSON ERROR");

						}
					});

			VolleyRequestQueueSingleton.getInstance(this).getRequestQueue()
					.add(jsObjRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createListView() {
		try {

			list = (ListView) findViewById(R.id.catalogList);
			ituneList = new ArrayList<>();
			adapter = new CatalogListAdapter(this, 0, ituneList, isTablet);
			list.setAdapter(adapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					try {

						ItunesApp itune = adapter.getItem(position);
						// begin dialog to show itune app details
						Intent intent = new Intent(getContext(),
								DetailActivity.class);
						intent.putExtra(ITUNES_SELECTED, itune);
						startActivity(intent);
						overridePendingTransition(R.anim.shake, R.anim.abc_fade_out);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateListView() {
		try {

			adapter.clear();
			adapter.addAll(ituneList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Context getContext() {

		return this;
	}

	private void createGridView() {
		try {

			grid = (GridView) findViewById(R.id.gridviewCatalog);
			ituneList = new ArrayList<>();
			adapter = new CatalogListAdapter(this, 0, ituneList, isTablet);
			grid.setAdapter(adapter);

			grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					try {

						ItunesApp itune = adapter.getItem(position);
						// begin dialog to show itune app details
						Intent intent = new Intent(getContext(),
								DetailActivity.class);
						intent.putExtra(ITUNES_SELECTED, itune);
						startActivity(intent);
						overridePendingTransition(R.anim.shake, R.anim.abc_fade_out);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// validate internet connection
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}

	private void storeItemInDB() {
		try {

			ItemDAO dao = new ItemDAO(getContext(), DatabaseManager.DB_NAME,
					null, DatabaseManager.DB_VERSION);
			
			dao.storeItemInDB(ituneList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void loadItemsFromDB() {
		try {

			ItemDAO dao = new ItemDAO(getContext(), DatabaseManager.DB_NAME,
					null, DatabaseManager.DB_VERSION);
			
			ituneList = dao.loadItemsFromDB();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadImagesFromInternalStorage() {
		try {
			
			ImageCacheSingleton.getInstance(this).loadImagesFromInternal(ituneList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
