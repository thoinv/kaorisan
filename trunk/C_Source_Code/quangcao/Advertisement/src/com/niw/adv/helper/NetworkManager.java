// network functions
package com.niw.adv.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	private NetworkInfo networkInfo;

	public NetworkManager(Context context) {
		ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo=connectivityManager.getActiveNetworkInfo();
	}
	
	public boolean NetworkState(){
		return networkInfo!=null && networkInfo.isConnected();
	}
}
