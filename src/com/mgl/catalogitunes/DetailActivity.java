package com.mgl.catalogitunes;

import com.mgl.catalogitunes.model.ItunesApp;
import com.mgl.util.DownloadImageTask;
import com.squareup.picasso.Picasso;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	private ItunesApp itune;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		try {
			
			//validate if is tablet or smartphone
			boolean isTablet = getResources().getBoolean(R.bool.isTablet);
			if(isTablet){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}else{
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			
			//retrieve inten information
			Intent intent = getIntent();
			itune = (ItunesApp) intent.getSerializableExtra(MainActivity.ITUNES_SELECTED);
			
			inflateDetailView();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
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
	
	//==============================================================================
	
	private void inflateDetailView() {
		try {
			
			ImageView img = (ImageView) findViewById(R.id.detailImage);
			Picasso.with(getBaseContext()).load(itune.getImage100()).into(img);
			
			TextView title = (TextView) findViewById(R.id.detailItemName);
			title.setText(itune.getName());
			
			TextView company = (TextView) findViewById(R.id.detailCompanyName);
			company.setText(itune.getCompany());
			
			TextView type = (TextView) findViewById(R.id.detailType);
			type.setText(itune.getType()+",");
			
			TextView category = (TextView) findViewById(R.id.detailCategory);
			category.setText(itune.getCategory());
			
			TextView price = (TextView) findViewById(R.id.detailPrice);
			try {
				if(Float.parseFloat(itune.getPrice()) == 0 ){
					price.setText(getResources().getString(R.string.free));
				}else{
					price.setText(Float.parseFloat(itune.getPrice()) + " " +itune.getCurrency());
				}
					
			} catch (Exception e) {
				price.setText(itune.getCurrency());
			}
			
			TextView description = (TextView) findViewById(R.id.detailDescription);
			description.setText(itune.getSummary());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
