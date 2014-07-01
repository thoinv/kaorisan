package com.kaorisan.activity;

import android.app.Activity;

public class CommonRequestActivity extends Activity{
	/*
	TextView btnSettings;
	TextView txtTitleCommonRequest;
	TextView txtItemTextOfExpandListView;
	private ExpandableListView mExpandableList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DebugLog.logd("Common Request activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_request);

		btnSettings = (TextView) findViewById(R.id.btnSettings);
		
		txtItemTextOfExpandListView = (TextView)findViewById(R.id.list_item_text_child);
		
		mExpandableList = (ExpandableListView) findViewById(R.id.expandable_list);
		
		bindDataCommonRequestToExpandList(mExpandableList);
	
	}

	@SuppressLint("UseSparseArrays")
	private void bindDataCommonRequestToExpandList(
			ExpandableListView expandableListView) {

		ArrayList<CommonRequest> commonRequests = new ArrayList<CommonRequest>();

		CommonRequest commonRequest1 = new CommonRequest();
		commonRequest1.setTitle("General");
		commonRequest1.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.general_array)));
		commonRequests.add(commonRequest1);

		CommonRequest commonRequest2 = new CommonRequest();
		commonRequest2.setTitle("Restaurant");
		commonRequest2.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.restaural_array)));
		commonRequests.add(commonRequest2);

		CommonRequest commonRequest3 = new CommonRequest();
		commonRequest3.setTitle("Research");
		commonRequest3.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.research_array)));
		commonRequests.add(commonRequest3);

		CommonRequest commonRequest4 = new CommonRequest();
		commonRequest4.setTitle("Weddings");
		commonRequest4.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.weddings_array)));
		commonRequests.add(commonRequest4);

		CommonRequest commonRequest5 = new CommonRequest();
		commonRequest5.setTitle("Travel");
		commonRequest5.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.travel_array)));
		commonRequests.add(commonRequest5);

		CommonRequest commonRequest6 = new CommonRequest();
		commonRequest6.setTitle("Kids");
		commonRequest6.setArrayChildren(stringArrayToArrayList(getResources().getStringArray(R.array.kids_array)));
		commonRequests.add(commonRequest6);

		mExpandableList.setAdapter(new CommonRequestAdapter(this, commonRequests));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common_request, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnSettings:
			DebugLog.logd("Button Settings click");
			onBackPressed();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	// content child
	public ArrayList<String> stringArrayToArrayList(String[] s){
		ArrayList<String> l=new ArrayList<String>();
		l.clear();
		for (String string : s) {
			l.add(string);
		}
		return l;
	}
*/
}
