package com.kaorisan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.AccountDashBoard;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils.Social;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dialog.PopUpMessage;
import com.kaorisan.lazyload.ImageLoader;
import com.kaorisan.manager.AccountDashBoardManager;
import com.kaorisan.manager.AuthenticationManager;
import com.kaorisan.supportActivity.LogoutConfirm;
import com.kaorisan.supportActivity.LogoutConfirm.OnLogoutYesClicked;

@SuppressLint("InlinedApi")
public class AccountDashboardActivity extends Activity implements OnClickListener {
	
	ProgressDialog showProcess = null;
	
	TextView txtEmail;
	TextView txtName;
	TextView txtPlanName;
	TextView btnRateUs;
	TextView txtRequestUse;
	
	Button btnLogout;

	private ImageView imgAvatar;
	private Button btnDone;
	
	Resources resource = null;
	
	private final int STARNDARD_WIDTH = 720;
	private int requestLeft;
	
	private final String EMPTY_STRING = "";
	
	float values[] = new float[2];
	
	private RelativeLayout chartLayoutProgress = null;
	
	RelativeLayout frameChart = null;

	Context context = getBaseContext();

	CacheData cacheData = null;
	private boolean flag = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_dashboard);
		
		resource = this.getResources();
		
		cacheData = CacheData.getInstant();
		
		txtRequestUse = (TextView) findViewById(R.id.txtRequestUse);
		
		btnLogout = (Button)findViewById(R.id.btnLogout);
		
		txtEmail = (TextView) findViewById(R.id.txtEmail);

		txtName = (TextView) findViewById(R.id.txtName);

		txtPlanName = (TextView) findViewById(R.id.txtPlanName);

		btnRateUs = (TextView) findViewById(R.id.btnRate);

		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);

		frameChart = (RelativeLayout) findViewById(R.id.linear);

		bindData();
		
	}

	private float[] calculateData(float[] data) {
		float total = 0;
		for (int i = 0; i < data.length; i++) {
			total += data[i];
		}
		for (int i = 0; i < data.length; i++) {
			if(total != 0){
				data[i] = 360 * (data[i] / total);
				flag = true;
			}else{
				data[i] = 360;
			}
		}
		return data;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnRateUs:
			DebugLog.logd("Button Rate Us click");
			
			final String appPackageName = "com.facebook.katana&hl=vi"; 
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
			}
			break;
			/*
		case R.id.btnCommonRequest:
			DebugLog.logd("Button Common Request click");
			Intent intentCommonRequest = new Intent(this, CommonRequestActivity.class);
			startActivity(intentCommonRequest);
			break;
			*/
		case R.id.btnTearmAndCondition:
			DebugLog.logd("Button Tearm And Condition click");
			Intent intentTearmAndCondition = new Intent(this,TermsAndConditionsActivity.class);
			startActivity(intentTearmAndCondition);
			break;
			
		case R.id.btnEmailSupport:
			DebugLog.logd("Button Email Support click");
			
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.email_support) });
			email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_title));
			email.setType("message/rfc822");
			
			this.startActivity(Intent.createChooser(email, "Email Support:"));

			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_dashboard, menu);
		return true;
	}

	public void onBtnLogOutClicked(View v) {

		switch (v.getId()) {
		case R.id.btnLogout:
			DebugLog.logd("btn logout clicked");
			LogoutConfirm logoutConfirm = null;
			if (cacheData.getTokenKaorisan() != null) {

				// show logout confirm before
				logoutConfirm = new LogoutConfirm((Activity) this, new OnLogoutYesClicked() {
							public void onLogOutClicked(final Activity behind) {
								try {

//									LoginActivity.isLogout = 1;
//									Intent loginIntent = new Intent(Splash.getInstant(), LoginActivity.class);
//									loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//									startActivity(loginIntent);
//									if (behind != null) {
//										behind.finish();
//									}
									AuthenticationManager manager = new AuthenticationManager();
									manager.logout(CacheData.getInstant().getTokenKaorisan(),"android",CacheData.getInstant().getCurrentUser().getPushToken(), new AuthenticationManager.OnLogout() {
										@Override
										public void onLogoutMethod(boolean isSuccess, String message) {
											if(isSuccess){
												LoginActivity.isLogout = 1;
												if(LoginActivity.currentSocial == Social.GOOGLE){
													if(LoginActivity.mPlusClient.isConnected()){
														LoginActivity.mPlusClient.clearDefaultAccount();
													}
												}
												Intent loginIntent = null;
												
												if(Splash.getInstant() != null){
													loginIntent = new Intent(Splash.getInstant(), LoginActivity.class);
												}else{
													loginIntent = new Intent(AccountDashboardActivity.this, LoginActivity.class);
												}
												
												loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(loginIntent);
												if (behind != null) {
													behind.finish();
												}
											}else{
												PopUpMessage popup = new PopUpMessage(AccountDashboardActivity.this,
														"<h2> " + resource.getString(R.string.network_error_message) + " </h2>",
														true, 3000, false);
												popup.show();
											}
										}
									});
								} catch (Exception e) {
									DebugLog.loge("Logout exception: " + e.getMessage());
								}
							}
						});

			} else {
				logoutConfirm = new LogoutConfirm((Activity) this, new OnLogoutYesClicked() {
							public void onLogOutClicked(Activity behind) {
								LoginActivity.isLogout = -1;
								if (behind != null) {
									behind.finish();
								}
							}
						});
			}
			logoutConfirm.show();
			break;
			
		case R.id.btnDone:
			finish();
		
		}

	}

	@SuppressWarnings("deprecation")
	private void bindData() {
		btnLogout.setClickable(false);
		
		if (cacheData.getCurrentUser() != null) {
			if (cacheData.getCurrentUser().getAvatar() != null) {

				ImageLoader imageLoader = new ImageLoader(AccountDashboardActivity.this);

				imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
				imageLoader.DisplayImage(cacheData.getCurrentUser().getAvatar(), imgAvatar);
			}

			txtName.setText(cacheData.getCurrentUser().getFullName());
		}

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		chartLayoutProgress = (RelativeLayout) layoutInflater.inflate(R.layout.progress_layout, null, false);

		chartLayoutProgress.setLayoutParams(new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		chartLayoutProgress.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		frameChart.addView(chartLayoutProgress);

		AccountDashBoardManager accountDashBoardManager = new AccountDashBoardManager();

		accountDashBoardManager.getAccountDashBoard(cacheData.getTokenKaorisan(), new AccountDashBoardManager.OnGetAccountDashBoardResult() {

					@Override
					public void onGetDashBoardResultMethod(boolean isSuccess, AccountDashBoard accountDashBoard, String message) {
						if (isSuccess) {
							Log.i("Tesst", "Plan: " + accountDashBoard.getPlanName());
							txtPlanName.setText(accountDashBoard.getPlanName());
							txtEmail.setText(accountDashBoard.getEmail());
							if(cacheData.getDashBoard() != null){
								txtRequestUse.setText(String.format( resource.getString(R.string.request_use_text_format), accountDashBoard.getUserRequest()
										+ cacheData.getDashBoard().getClosedTask(), accountDashBoard.getTotalRequest() 
										+ cacheData.getDashBoard().getAvailableTask() + EMPTY_STRING));

								requestLeft = (accountDashBoard.getTotalRequest() +	cacheData.getDashBoard().getAvailableTask()) 
										- (accountDashBoard.getUserRequest() + cacheData.getDashBoard().getClosedTask());
		
								values[0] = accountDashBoard.getUserRequest() + cacheData.getDashBoard().getClosedTask();
		
								values[1] = requestLeft;
								
								Log.i("values0", ""+values[0]);
								Log.i("values1", ""+values[1]);
								values = calculateData(values);
		
								MyGraphview graphview = new MyGraphview(AccountDashboardActivity.this, values);
							
								frameChart.addView(graphview);
								
								frameChart.removeView(chartLayoutProgress);
							}
							
						} else {
							DebugLog.loge("Get account dashboard failed!");
						}
						btnLogout.setClickable(true);
					}
					
				});
	}

	public class MyGraphview extends View {
		public int getWidthScreen() {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int width = displaymetrics.widthPixels;
			DebugLog.logd("Width: " + width);
			return width;
		}

		private int widthScreen = getWidthScreen();

		
		private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private float[] value_degree;
		
		float temp = 0;

		RectF rectf = new RectF(0, 0, 200 * widthScreen / STARNDARD_WIDTH, 200 * widthScreen / STARNDARD_WIDTH);
		
		@SuppressLint("NewApi")
		public MyGraphview(Context context, float[] values) {
			super(context);
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
				this.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
				DebugLog.logd("Set software acceleration!");
			}
			value_degree = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				value_degree[i] = values[i];
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			DebugLog.logd("onDraw");

			for (int i = 0; i < value_degree.length; i++) {
				if (i == 0) {
					paint.setColor(Color.parseColor("#c8c8c8"));
					canvas.drawArc(rectf, 270, value_degree[i], true, paint);

				} else {
					if(flag == true){
						temp += value_degree[i - 1];
						paint.setColor(Color.parseColor("#e23a80"));
						canvas.drawArc(rectf, temp + 270, value_degree[i], true, paint);
					}
				}

			}

			paint.setColor(Color.parseColor("#ffffff"));
			
			canvas.drawCircle(100 * widthScreen / STARNDARD_WIDTH, 100 * widthScreen / STARNDARD_WIDTH, 55 * widthScreen / STARNDARD_WIDTH, paint);
			
			paint.setColor(Color.parseColor("#000000"));
			paint.setTextSize(35f * widthScreen / STARNDARD_WIDTH);
			paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

			
			canvas.drawText(requestLeft < 10 ? "0" + requestLeft : requestLeft + "", 80 * widthScreen / STARNDARD_WIDTH, 90 * widthScreen / STARNDARD_WIDTH, paint);
			paint.setColor(Color.parseColor("#cccccc"));
			paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
			paint.setTextSize(30f * widthScreen / STARNDARD_WIDTH);
			
			canvas.drawText("LEFT", 70 * widthScreen / STARNDARD_WIDTH, 130	* widthScreen / STARNDARD_WIDTH, paint);

		}
	}

	@Override
	public void onClick(View view) {
		DebugLog.logd("Button Settings click");
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
