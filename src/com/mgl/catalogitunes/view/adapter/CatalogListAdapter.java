package com.mgl.catalogitunes.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mgl.catalogitunes.R;
import com.mgl.catalogitunes.model.ItunesApp;
import com.mgl.util.ImageCacheSingleton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CatalogListAdapter extends ArrayAdapter<ItunesApp> {

	private boolean isTablet = false;
	
	//List to store picasso targets to not been collected by garbage
	private ArrayList<Target> targetList;
	
	
	public CatalogListAdapter(Context context, int resource,
			List<ItunesApp> objects, boolean isTablet) {
		super(context, resource, objects);
		this.isTablet = isTablet;
		targetList = new ArrayList<>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// getting iflater
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// save list reference
		View listItemView = convertView;

		try {

			// if current view doesnt exist, inflate with my item view
			if (null == convertView) {
				listItemView = inflater.inflate(R.layout.catalog_item, parent,
						false);
			}

			// get image
			final ImageView image = (ImageView) listItemView
					.findViewById(R.id.catalogListImage);
			// get current item and download image from url
			final ItunesApp item = getItem(position);

			// Prefer use picasso, a library more tested than my own
			loadFromStorePicasso(item, image, item.getImage100());
			
			// fill android textViews
			TextView title = (TextView) listItemView
					.findViewById(R.id.catalogItemName);

			TextView description = (TextView) listItemView
					.findViewById(R.id.catalogShortDescription);

			title.setText(item.getName());
			String shortDescription = new String("Description ...");
			try {
				shortDescription = item.getSummary().substring(0, 100);
				shortDescription = shortDescription + "...";
			} catch (Exception e) {
				shortDescription = new String("Description ...");
			}
			description.setText(shortDescription);

			//adjust gridview textsize
			if(isTablet){
				//a little bit small than the details view
				title.setTextSize(getContext().getResources().getDimension(R.dimen.title_text_tablet));
				description.setTextSize(getContext().getResources().getDimension(R.dimen.description_text_tablet));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listItemView;

	}

	private void loadFromStorePicasso(final ItunesApp item, final ImageView image, final String imageUrl) {
		try {
			
			if( !isNetworkConnected()){
				//load store image on disk
				Bitmap bitmap = ImageCacheSingleton.getInstance(getContext()).getImage(item.getId());
				if(bitmap==null){
					image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_image));
				}else{
					image.setImageBitmap(bitmap);
				}
				return;
			}

			Target target = new Target() {
				
				@Override
				public void onPrepareLoad(Drawable arg0) {
				}
				
				@Override
				public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1) {
					try {
						image.setImageBitmap(bitmap);
						//possible save async
						ImageCacheSingleton.getInstance(getContext()).addImage(imageUrl, bitmap, item.getId()+"");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onBitmapFailed(Drawable arg0) {
					
				}
			};
			
			targetList.add(target);
			Picasso.with(getContext()).load(imageUrl).placeholder(getContext().getResources().getDrawable(R.drawable.empty_image)).into(target);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// validate internet connection
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}

}
