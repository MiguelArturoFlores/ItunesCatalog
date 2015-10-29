package com.mgl.webservice.util;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import android.content.Context;

public class VolleyRequestQueueSingleton {

	private static VolleyRequestQueueSingleton instance = null;

	private static Context context;
	private RequestQueue queue;

	private VolleyRequestQueueSingleton(Context context){
		try {
			
			this.context = context;
			queue = getRequestQueue();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized VolleyRequestQueueSingleton getInstance(
			Context context) {
		try {
			if (instance == null) {
				instance = new VolleyRequestQueueSingleton(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public RequestQueue getRequestQueue() {
		try {
			
		
		if (queue == null) {
			Cache cache = new DiskBasedCache(context.getCacheDir(),
					10 * 1024 * 1024);
			Network network = new BasicNetwork(new HurlStack());
			queue = new RequestQueue(cache, network);
			// Don't forget to start the volley request queue
			queue.start();
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return queue;
	}

}
