package com.niw.adv.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.adv.activity.R;
import com.niw.adv.adapter.DistributorAdapter;
import com.niw.adv.helper.JsonFuncs;
import com.niw.adv.helper.LoadDialog;
import com.niw.adv.helper.LoadFooter;
import com.niw.adv.helper.NetworkManager;
import com.niw.adv.object.CatalogObj;
import com.niw.adv.object.DetailObj;
import com.niw.adv.object.DistributorObj;

public class DistributorActivity extends SherlockActivity {

	protected static final String DISTRIBUTOR_ID_KEY = "distributorID";
	protected static final String DISTRIBUTOR_ICON_CACHE = "distributorIconCache";
	private List<DistributorObj> objects;
	private ActionBar actionbar;
	private int catalog_id;
	private TextView detail_num;
	private TextView distributor_num;
	private TextView catalog_num;
	private SharedPreferences sharepref;
	private LoadDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distributors);
		
		// load banner from cache
		ImageView banner=(ImageView)findViewById(R.id.banner_img);
		LoadFooter footer=new LoadFooter(this,banner);
		footer.loadCache();
		
		// load header sharepref
		sharepref = getSharedPreferences("com.niw.adv", Context.MODE_PRIVATE);
		catalog_num = (TextView) findViewById(R.id.catalog_num);
		catalog_num.setText(sharepref.getString(CatalogObj.CATALOG_NUM, ""));
		distributor_num = (TextView) findViewById(R.id.distributor_num);
		distributor_num.setText(sharepref.getString(DistributorObj.DISTRIBUTOR_NUM, ""));
		detail_num = (TextView) findViewById(R.id.detail_num);
		detail_num.setText(sharepref.getString(DetailObj.DETAIL_NUM, ""));
		
		//Load
		if(dialog==null) dialog=new LoadDialog(this);
		
		//  catalog_id to query DB
		// get catalogID
		Intent retrive = getIntent();
		Log.d(getPackageName(), "Distributor <- get ID:" + retrive.getIntExtra(CatalogActivity.CATALOG_ID_KEY, -1));
		catalog_id=retrive.getIntExtra(CatalogActivity.CATALOG_ID_KEY, -1);

		//  set value from Json
		// fix link chuan

		objects=new ArrayList<DistributorObj>();
		final ListView lDistributor = (ListView) findViewById(R.id.list_distributor);
		lDistributor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long id) {
				Intent i = new Intent(DistributorActivity.this,
						DetailActivity.class);

				// send distributor -> detail
				i.putExtra(DISTRIBUTOR_ID_KEY, objects.get(pos).getId());

				// send icon cache path -> detail to get image logo from cache
				i.putExtra(DISTRIBUTOR_ICON_CACHE, objects.get(pos).getIcon());

				Log.d(getPackageName(), "Distributor Activity: send ID : "
						+ objects.get(pos).getId());
				Log.d(getPackageName(),
						"Distributor Activity: send Icon cache : "
								+ objects.get(pos).getIcon());
				startActivity(i);
			}
		});
		
		// check network state
		NetworkManager nw = new NetworkManager(this);
		if (nw.NetworkState()) {

			// get catalog_id => add to link
			// Json
			final JsonFuncs jf = new JsonFuncs(this,
					"http://websitebanhang.net/advserver/index.php/getDistributors?catalog_id="
							+ catalog_id);
			new AsyncTask<Void, Integer, String>() {

				protected void onPreExecute() {
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
							objects.add(new DistributorObj(jsonOBJ.getInt(DistributorObj.ID_COLUMN), 
									jsonOBJ.getString(DistributorObj.NAME_COLUMN),
									jsonOBJ.getString(DistributorObj.ICON_COLUMN), 
									catalog_id,
									jsonOBJ.getInt(DistributorObj.RECORDS_COLUMN)));
						}
						lDistributor.setAdapter(new DistributorAdapter(
								getApplicationContext(), R.layout.base_item,
								objects));

						if (dialog.getDialog() != null) {
							dialog.getDialog().dismiss();
							dialog.setDialog(null);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};

			}.execute();
		} else {
			Toast toast=Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER	, 0, 0);
			toast.show();
		}
		
		actionbar = getSupportActionBar();
		actionbar.setTitle("");
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setCustomView(R.layout.actionbar_title);	//custom title
		
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// event select item action bar
	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		} else if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(DistributorActivity.this, CatalogActivity.class));
			finish();
		}
		return true;
	}
}
