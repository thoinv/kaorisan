package com.niw.adv.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
import com.niw.adv.adapter.DetailAdapter;
import com.niw.adv.helper.JsonFuncs;
import com.niw.adv.helper.LoadDialog;
import com.niw.adv.helper.LoadFooter;
import com.niw.adv.helper.NetworkManager;
import com.niw.adv.object.CatalogObj;
import com.niw.adv.object.DetailObj;
import com.niw.adv.object.DistributorObj;

public class DetailActivity extends SherlockActivity {

	protected static final String LINK_DETAIL = "keylink";
	private List<DetailObj> objects;
	private ActionBar actionbar;
	private SharedPreferences sharepref;
	private TextView catalog_num;
	private TextView distributor_num;
	private TextView detail_num;
	private LoadDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		// load banner from cache
		ImageView banner = (ImageView) findViewById(R.id.banner_img);
		LoadFooter footer = new LoadFooter(this, banner);
		footer.loadCache();
		
		// load header sharepref
		sharepref=getSharedPreferences("com.niw.adv", Context.MODE_PRIVATE);
		catalog_num=(TextView)findViewById(R.id.catalog_num);
		catalog_num.setText(sharepref.getString(CatalogObj.CATALOG_NUM, ""));
		distributor_num=(TextView)findViewById(R.id.distributor_num);
		distributor_num.setText(sharepref.getString(DistributorObj.DISTRIBUTOR_NUM, ""));
		detail_num=(TextView)findViewById(R.id.detail_num);
		detail_num.setText(sharepref.getString(DetailObj.DETAIL_NUM, ""));
		
		// load dialog
		if(dialog==null) dialog=new LoadDialog(this);
		
		objects = new ArrayList<DetailObj>();
		
		Intent intent = getIntent();
		int d_ID = intent.getIntExtra(DistributorActivity.DISTRIBUTOR_ID_KEY, -1);
		if (d_ID != -1) {
			Log.d(getPackageName(), "retive distributor ID " + d_ID);
		} else {
			Log.e(getPackageName(), "Error get distributor id ");
		}
		
		// List view
		final ListView lDetails = (ListView) findViewById(R.id.list_detail);
		// IconDistributor path from Distributor activity
		final String iconDistributor = getIntent().getStringExtra(
				DistributorActivity.DISTRIBUTOR_ICON_CACHE);
		Log.d(getPackageName(), "Details acti: get icon cache: "
				+ iconDistributor);

		lDetails.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {

				// put link -> Fullscreen to load
				Intent i = new Intent(DetailActivity.this, FullscreenActivity.class);
				i.putExtra(LINK_DETAIL, objects.get(pos).getLink());
				Log.d(getPackageName(), "Detail send link:"
						+ objects.get(pos).getLink());
				startActivity(i);
			}
		});
		
		// check network state
		NetworkManager nw = new NetworkManager(this);
		if (nw.NetworkState()) {

			// set value from json
			// retrive distributor_id to query DB , simple add to link
			// Json
			final JsonFuncs jf = new JsonFuncs(this,
					"http://websitebanhang.net/advserver/index.php/getDetails?distributor_id="+ d_ID);
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
						for (int i = 0; i < array.length(); i++) {
							objects.add(new DetailObj(array.getJSONObject(i)
									.getString(DetailObj.TITLE_COLUMN), array
									.getJSONObject(i).getString(
											DetailObj.LINK_COLUMN), array
									.getJSONObject(i).getString(
											DetailObj.EXPIRE_COLUMN)));
						}
						lDetails.setAdapter(new DetailAdapter(
								DetailActivity.this, R.layout.detail_item,
								objects, iconDistributor));

						if (dialog.getDialog() != null) {
							dialog.getDialog().dismiss();
							dialog.setDialog(null);
						}

						if (objects.size() == 1) {
							// put link -> Fullscreen to load (if 1 child)
							Intent i = new Intent(DetailActivity.this,
									FullscreenActivity.class);
							i.putExtra(LINK_DETAIL, objects.get(0).getLink());

							Log.d(getPackageName(), "Detail send link:"
									+ objects.get(0).getLink());

							startActivity(i);
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
		
		// actionbar
		actionbar = getSupportActionBar();
		actionbar.setTitle("");
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
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

	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		} else if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(DetailActivity.this, CatalogActivity.class));
			finish();
		}
		return true;
	}
}
