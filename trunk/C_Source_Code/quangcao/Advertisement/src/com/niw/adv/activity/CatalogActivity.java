package com.niw.adv.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.adv.activity.R;
import com.niw.adv.adapter.CatalogAdapter;
import com.niw.adv.helper.JsonFuncs;
import com.niw.adv.helper.LoadDialog;
import com.niw.adv.helper.LoadFooter;
import com.niw.adv.helper.NetworkManager;
import com.niw.adv.object.CatalogObj;
import com.niw.adv.object.DetailObj;
import com.niw.adv.object.DistributorObj;

public class CatalogActivity extends SherlockActivity {
	public static String CATALOG_ID_KEY = "catalog_id";
	private boolean preExit = false;
	private ActionBar actionbar;
	private TextView catalog_num;
	private TextView distributor_num;
	private TextView detail_num;
	private Editor sharepref;
	private LoadDialog dialog;
	private SharedPreferences spref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);
	
		// load banner
		ImageView banner=(ImageView)findViewById(R.id.banner_img);
		LoadFooter footer=new LoadFooter(this,banner);
		footer.execute("http://websitebanhang.net/advserver/index.php/getFooter");
		
		// retrive previous value
		spref=getSharedPreferences("com.niw.adv", Context.MODE_PRIVATE);
		catalog_num=(TextView)findViewById(R.id.catalog_num);
		catalog_num.setText(spref.getString(CatalogObj.CATALOG_NUM, ""));
		
		distributor_num=(TextView)findViewById(R.id.distributor_num);
		distributor_num.setText(spref.getString(DistributorObj.DISTRIBUTOR_NUM, ""));
		
		detail_num=(TextView)findViewById(R.id.detail_num);
		detail_num.setText(spref.getString(DetailObj.DETAIL_NUM, ""));
		
		sharepref=spref.edit();
		Log.d(getPackageName(), getPackageName());
		
		// load dialog
		if(dialog==null) dialog=new LoadDialog(this);
		
		// Listview 
		final ListView lCatalog = (ListView) findViewById(R.id.list_catalog);
		final List<CatalogObj> objects = new ArrayList<CatalogObj>();

		// listview onclick item
		lCatalog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				
				// send catalog id -> distribtor
				Intent i = new Intent(CatalogActivity.this, DistributorActivity.class);
				i.putExtra(CATALOG_ID_KEY, objects.get(pos).getId());
				startActivity(i);
				Log.d(getPackageName(), "Catalog ->Send ID:"
						+ objects.get(pos).getId());
			}
		});
		
		// check network state
		NetworkManager nw = new NetworkManager(this);

		if (nw.NetworkState()) {
			// Json
			final JsonFuncs jf = new JsonFuncs(this,
					"http://websitebanhang.net/advserver/index.php/getCatalog");
			new AsyncTask<Void, Integer, String>() {
				protected void onPreExecute() {
					dialog.getDialog().setCancelable(false);
					dialog.getDialog().show();
				};

				@Override
				protected String doInBackground(Void... params) {
					return jf.getJson();
				}

				protected void onPostExecute(String result) {
					Log.d(getPackageName(), result);

					// get all data from json
					// json format: [ {}, {},....]
					try {
						JSONArray array = new JSONArray(result);
						JSONObject jsonOBJ;
						for (int i = 0; i < array.length(); i++) {
							jsonOBJ=array.getJSONObject(i);
							objects.add(new CatalogObj(jsonOBJ.getInt(CatalogObj.ID_COLUMN),
									jsonOBJ.getString(CatalogObj.NAME_COLUMN), 
									jsonOBJ.getString(CatalogObj.ICON_COLUMN),
									jsonOBJ.getInt(CatalogObj.RECORDS_COLUMN)));
						}
						lCatalog.setAdapter(new CatalogAdapter(	
								getApplicationContext(), R.layout.base_item, objects));

						if (dialog.getDialog() != null) {
							dialog.getDialog().dismiss();
							dialog.setDialog(null);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};

			}.execute();

			// json header -> share preference
			final JsonFuncs jheader = new JsonFuncs(this,
					"http://websitebanhang.net/advserver/index.php/getRownum");
			new AsyncTask<Void, Integer, String>() {

				@Override
				protected String doInBackground(Void... params) {
					return jheader.getJson();
				}

				protected void onPostExecute(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);

						catalog_num.setText(jsonObject
								.getString(CatalogObj.CATALOG_NUM));
						distributor_num.setText(jsonObject
								.getString(DistributorObj.DISTRIBUTOR_NUM));
						detail_num.setText(jsonObject
								.getString(DetailObj.DETAIL_NUM));

						sharepref.putString(CatalogObj.CATALOG_NUM,
								jsonObject.getString(CatalogObj.CATALOG_NUM));
						sharepref
								.putString(
										DistributorObj.DISTRIBUTOR_NUM,
										jsonObject
												.getString(DistributorObj.DISTRIBUTOR_NUM));
						sharepref.putString(DetailObj.DETAIL_NUM,
								jsonObject.getString(DetailObj.DETAIL_NUM));
						sharepref.commit();

					} catch (JSONException e) {
						Log.e(getPackageName(),
								"Parse string -> json object failed:"
										+ e.toString());
					}
				};
			}.execute();
		} else {
			Toast toast=Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER	, 0, 0);
			toast.show();
		}
		
		// action bar setting
		actionbar = getSupportActionBar();
		actionbar.setTitle("");
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_title);	//custom title
	}

	@Override
	protected void onResume() {
		super.onResume();
		preExit = false;
	}

	@Override
	public void onBackPressed() {

		if (preExit) {
			super.onBackPressed();
			return;
		}
		preExit = true;
		Toast.makeText(CatalogActivity.this, "Ấn lại lần nữa để thoát",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		menu.findItem(R.id.action_home).setIcon(R.drawable.close);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		if(item.getItemId()==R.id.action_home){
			this.finish();
		}
		return true;
	}
}
