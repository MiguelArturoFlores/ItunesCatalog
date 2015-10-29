package com.mgl.webservice.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mgl.catalogitunes.model.ItunesApp;

public class JsonParser {

	public static ArrayList<ItunesApp> parseItunesReques(JSONObject response){
		ArrayList<ItunesApp> itunesList = new ArrayList<>();
		try {
			
			JSONArray jsonArray = response
					.getJSONObject("feed").getJSONArray("entry");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				ItunesApp itunesApp = new ItunesApp();
				itunesApp.setId(Long.valueOf(i));
				String name = jsonArray.getJSONObject(i).getJSONObject("im:name").getString("label");
				System.out.println("name "+name);
				
				JSONArray imgArray = jsonArray.getJSONObject(i).getJSONArray("im:image");
				for (int j = 0; j < imgArray.length(); j++) {
					String image = imgArray.getJSONObject(j).getString("label");
					itunesApp.addImg(image,j);
					System.out.println("image "+ image);
				}
				
				String summary = jsonArray.getJSONObject(i).getJSONObject("summary").getString("label");
				//System.out.println("summary "+summary);
				
				String price = jsonArray.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("amount");
				String currency = jsonArray.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("currency");
				//System.out.println("price "+price +" "+currency);
				
				String category = jsonArray.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label");
				//System.out.println("category "+category);
				
				String company =  jsonArray.getJSONObject(i).getJSONObject("im:artist").getString("label");
				
				String type =  jsonArray.getJSONObject(i).getJSONObject("im:contentType").getJSONObject("attributes").getString("label");
				
				itunesApp.setName(name);
				itunesApp.setCategory(category);
				itunesApp.setPrice(price);
				itunesApp.setCurrency(currency);
				itunesApp.setSummary(summary);
				itunesApp.setCompany(company);
				itunesApp.setType(type);
				
				itunesList.add(itunesApp);
				
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return itunesList;
	}
	
	
}
