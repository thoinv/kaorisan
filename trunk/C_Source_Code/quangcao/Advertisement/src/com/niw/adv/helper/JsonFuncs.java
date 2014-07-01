// provide some functions with json

package com.niw.adv.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

public class JsonFuncs {
	
	private InputStream instream;
	private String url;
	private Context mContext;
	private StringBuilder stringBuilder;
	public JsonFuncs(Context context,String url) {
		mContext=context;
		this.url=url;
	}

	// note: do not use directly, instead using asynctask
	public String getJson() {
		
		// get json content from urls
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			instream = client.execute(httpPost).getEntity().getContent();
		} catch (Exception e) {
			Log.e(mContext.getPackageName(), e.toString());
		}

		// Write JSON
		try {
			stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			String Line = null;
			while ((Line = reader.readLine()) != null) {
				stringBuilder.append(Line + "\n");
			}
			instream.close();
		} catch (Exception e) {
			Log.e(mContext.getPackageName(), e.toString());
		}
		return stringBuilder.toString();
	}
}
