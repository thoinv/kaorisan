package com.kaorisan.activity;

import static com.kaorisan.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.kaorisan.CommonUtilities.EXTRA_MESSAGE;
import static com.kaorisan.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jeremiemartinez.refreshlistview.RefreshListView;
import com.github.jeremiemartinez.refreshlistview.RefreshListView.OnRefreshListener;
import com.google.android.gcm.GCMRegistrar;
import com.kaorisan.ConnectionDetector;
import com.kaorisan.R;
import com.kaorisan.ServerUtilities;
import com.kaorisan.WakeLocker;
import com.kaorisan.adapter.RecommendTaskAdapter;
import com.kaorisan.adapter.TaskAdapter;
import com.kaorisan.beans.DashBoard;
import com.kaorisan.beans.RecommendTask;
import com.kaorisan.beans.Task;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dialog.TextRequestDialog;
import com.kaorisan.lazyload.ImageLoader;
import com.kaorisan.manager.DashBoardManager;
import com.kaorisan.manager.TaskManager;

public class TaskActivity extends Activity implements OnScrollListener {
	public static boolean isRecommendTask = false;
	public static Activity taskActivity = null;
	private final String PLATFORM = "android";
	private static final int START_INDEX_SUB_LIST = 0;
	private static final int JUMP_ITEM_NUMBER = 2;

	private boolean isFullListTask = false;

	private static int PAGE = 1;
	private static int RESULT_PER_PAGE = 25;

	ProgressDialog progressDialog = null;
	ImageView imgAvatar;
	Button btnLogOutFacebook;
	ImageLoader imgLoader;
	TextView btnSeeMoreRequest;

	AsyncTask<Void, Void, Void> mRegisterTask;

	AlertDialogManager alert = new AlertDialogManager();
	TaskManager taskManager = null;

	ConnectionDetector cd;

	private TaskAdapter taskAdapter;

	RecommendTaskAdapter recommendTaskAdapter;

	ListView listRecommendTaskView;

	RefreshListView listTaskView;
	private final String EMPTY_STRING = "";
	private final String NUMBER_PREFIX = "0";

	ProgressDialog showProcess = null;
	int scrollStateOfListView = 0;
	private int progress_count = 2;

	private boolean scrollEnd = false;

	int accountDashBoardRequestCode = 10;

	TextView txtOpenTask;
	TextView txtClosedTask;
	TextView txtAvailableTask;
	ListView listRecommendTask;

	private RelativeLayout footerListViewLayout = null;
//	private LinearLayout taskLayout;

	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// DebugLog.loge("onRestart");
	// if (NewRequestActivity.isSubmit || AudioReviewActivity.isSubmit) {
	// bindDataListViewTask(null);
	// }
	//
	// if (listTaskView.findViewWithTag(footerListViewLayout) != null) {
	// btnSeeMoreRequest.setClickable(false);
	// } else {
	// btnSeeMoreRequest.setClickable(true);
	// }
	//
	// // int count = 0;
	// // int index = 0;
	// // Random random = new Random();
	// //
	// // ArrayList<RecommendTask> recommendTaskTmp = new
	// // ArrayList<RecommendTask>();
	// // ArrayList<RecommendTask> recommendTaskCache = CacheData
	// // .getInstant().getRecommendTasks();
	// //
	// // while (true) {
	// // count++;
	// // if (count != 0) {
	// // int indexTemp = random.nextInt(4) - 0;
	// //
	// // if (indexTemp != index) {
	// // index = indexTemp;
	// // } else {
	// // continue;
	// // }
	// // recommendTaskTmp.add(recommendTaskCache.get(index));
	// // if (recommendTaskTmp.size() == 2) {
	// // break;
	// // }
	// // }
	// // }
	//
	// // recommendTaskAdapter = new RecommendTaskAdapter(
	// // TaskActivity.this, R.layout.recommend_task_item,
	// // recommendTaskTmp);
	// // recommendTaskAdapter = new RecommendTaskAdapter(
	// // TaskActivity.this, R.layout.recommend_task_item,
	// // CacheData.getInstant().getRecommendTasks());
	// // listRecommendTaskView.setAdapter(recommendTaskAdapter);
	// // progress_count--;
	// // Log.i("Countprogress", "" + progress_count);
	//
	// };

	private void bindListRecommendTask(final RefreshListView listView) {

		// if (CacheData.getInstant().getRecommendTasks() == null) {
		taskManager.getTaskRecommend(CacheData.getInstant().getTokenKaorisan(),
				"5", new TaskManager.OnGetTaskRecommend() {

					@Override
					public void onGetTaskRecommend(boolean isSuccess,
							ArrayList<RecommendTask> listRecommendTask,
							String message) {
						if (isSuccess) {
							CacheData.getInstant().setRecommendTasks(
									listRecommendTask);
							recommendTaskAdapter = new RecommendTaskAdapter(
									TaskActivity.this,
									R.layout.recommend_task_item,
									listRecommendTask);
							listRecommendTaskView
									.setAdapter(recommendTaskAdapter);
							progress_count--;

							Log.i("Countprogress", "" + progress_count);

							if (progress_count == 0 || progress_count < 0) {
								showProcess.dismiss();
								if (listView != null) {
									DebugLog.logd("Finish refreshing");
									listView.finishRefreshing();
									showProcess = null;
								}
							}
						} else {
							DebugLog.logd("Can't get list task recommend!");
						}
					}
				});
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		taskActivity = this;

//		taskLayout=(LinearLayout)findViewById(R.id.task_layout);
		
		setContentView(R.layout.activity_task);
		taskManager = new TaskManager();

		DebugLog.logd("Task activity oncreate");

		cd = new ConnectionDetector(getApplicationContext());

		GCMRegistrar.checkDevice(this);

		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			DebugLog.logd(SENDER_ID + " sender id");
		} else {
			if (CacheData.getInstant().getCurrentUser() != null) {
				CacheData.getInstant().getCurrentUser().setPushToken(regId);
			}
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				DebugLog.logd("Already registered with GCM");
			} else {
				Log.i("", "Try register------------------------");
				ServerUtilities.register(this, CacheData.getInstant()
						.getTokenKaorisan(), PLATFORM, regId);

			}
		}

		listRecommendTaskView = (ListView) findViewById(R.id.listRecommendTask);
		listRecommendTaskView.setScrollContainer(false);
		listRecommendTaskView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		});

		listRecommendTaskView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isRecommendTask = true;

				RecommendTask recommendTask = (RecommendTask) parent
						.getItemAtPosition(position);
				Intent intentCreateTaskActivity = new Intent(TaskActivity.this,
						NewRequestActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("title", recommendTask.getTitle());
				bundle.putString("requestContent",
						recommendTask.getRequestContent());
				intentCreateTaskActivity.putExtras(bundle);

				startActivity(intentCreateTaskActivity);
			}

		});

		progressDialog = new ProgressDialog(this);

		btnSeeMoreRequest = (TextView) findViewById(R.id.btnSeeMoreRequest);

		listTaskView = (RefreshListView) findViewById(R.id.listTask);
		listTaskView.setScrollbarFadingEnabled(true);
		listTaskView.setEnabledDate(true);

		// listTaskView.setChoiceMode(ListView.CHOICE_MODE_NONE);

		listTaskView.setRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(RefreshListView listView) {
				DebugLog.logd("onRefresh");
				bindDataListViewTask(listView);
			}
		});

		txtOpenTask = (TextView) findViewById(R.id.txtNumberOpen);
		txtClosedTask = (TextView) findViewById(R.id.txtNumberClose);
		txtAvailableTask = (TextView) findViewById(R.id.txtAvailableTask);

		imgLoader = new ImageLoader(this);
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
		imgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				imgAvatarClicked();
			}
		});

		try {
			if (CacheData.getInstant().getCurrentUser() != null
					&& CacheData.getInstant().getCurrentUser().getAvatar() != null) {
				imgLoader.DisplayImage(CacheData.getInstant().getCurrentUser()
						.getAvatar(), imgAvatar);
			}
			if (CacheData.getInstant().getListTask() != null
					|| !CacheData.getInstant().getRecommendTasks().isEmpty()) {
				if (CacheData.getInstant().getListTask().size() >= 5) {
					taskAdapter = new TaskAdapter(TaskActivity.this,
							R.layout.task_item, CacheData.getInstant()
									.getListTask().subList(0, 5));
				} else {
					taskAdapter = new TaskAdapter(TaskActivity.this,
							R.layout.task_item, CacheData.getInstant()
									.getListTask());
				}

				listTaskView.setAdapter(taskAdapter);
				displayDashBoard(CacheData.getInstant().getDashBoard());

				recommendTaskAdapter = new RecommendTaskAdapter(
						TaskActivity.this, R.layout.recommend_task_item,
						CacheData.getInstant().getRecommendTasks());
				listRecommendTaskView.setAdapter(recommendTaskAdapter);
			} else {
				bindDataListViewTask(null);

			}

		} catch (Exception exception) {
			exception.printStackTrace();
			DebugLog.loge("Get data exception: " + exception.getMessage());
			redirectLoginPage();
		}
	}

	private void redirectLoginPage() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Utils.resetCacheData();
		finish();
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			DebugLog.logd("New Message: " + newMessage);

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	private void bindDataListViewTask(final RefreshListView listView) {

		DashBoardManager dashBoardManager = new DashBoardManager();

		PAGE = 1;
		isFullListTask = false;

		if (listView == null) {
			showProcess = new ProgressDialog(this);
			showProcess.setCancelable(false);
			showProcess.setTitle("Loading...");
			CacheData.getInstant().setCurrentProgressDialog(showProcess);
			showProcess.show();
		}

		bindListRecommendTask(listView);

		taskManager.getListTask(CacheData.getInstant().getTokenKaorisan(), PAGE
				+ EMPTY_STRING, RESULT_PER_PAGE + EMPTY_STRING,
				new TaskManager.OnGetListTaskResult() {

					@Override
					public void onGetListTaskResultMethod(boolean isSuccess,
							List<Task> listTask, String message) {

						if (isSuccess) {
							if (listTask.size() >= 5) {
								taskAdapter = new TaskAdapter(
										TaskActivity.this, R.layout.task_item,
										listTask.subList(0, 5));
							} else {
								taskAdapter = new TaskAdapter(
										TaskActivity.this, R.layout.task_item,
										listTask);
							}

							taskAdapter.notifyDataSetChanged();
							listTaskView.setAdapter(taskAdapter);
							CacheData.getInstant().setListTask(listTask);
							DebugLog.logd("Update listview thanh cong");
							DebugLog.logd("adapter size"
									+ (listTaskView.getCount() - 1));
							DebugLog.logd("visibleChildCount" + listTask.size());
						}
						progress_count--;
						Log.i("Countprogress", "" + progress_count);
						if (progress_count == 0 || progress_count < 0) {
							Utils.dismissCurrentProgressDialog();
							showProcess = null;
							if (listView != null) {
								DebugLog.logd("Finish refreshing");
								listView.finishRefreshing();
								showProcess = null;
							}
						}

					}
				});

		progress_count = 3;
		dashBoardManager.getDashBoard(
				CacheData.getInstant().getTokenKaorisan(),
				new DashBoardManager.OnGetDashBoardResult() {

					@Override
					public void onGetDashBoardResultMethod(boolean isSuccess,
							DashBoard dashBoard, String message) {
						if (isSuccess) {
							CacheData.getInstant().setDashBoard(dashBoard);
							displayDashBoard(dashBoard);
							progress_count--;
							Log.i("Countprogress", "" + progress_count);
							if (progress_count == 0 || progress_count < 0) {
								Utils.dismissCurrentProgressDialog();
								if (listView != null) {
									DebugLog.logd("Finish refreshing");
									listView.finishRefreshing();
									showProcess = null;
								}
							}

						} else {
							if (CacheData.getInstant()
									.getCurrentProgressDialog() != null) {
								CacheData.getInstant()
										.getCurrentProgressDialog().dismiss();
							}
							CacheData.getInstant().setDashBoard(null);
						}

						DebugLog.logd((dashBoard == null) + "");
						CheckDashBoardData();
					}

				});

		listTaskView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DebugLog.logd("click");
				Task task = CacheData.getInstant().getListTask().get(arg2);

				Intent intent = new Intent(TaskActivity.this,
						TaskDetailActivity.class);
				CacheData.getInstant().setCurrentTask(task);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == accountDashBoardRequestCode
				&& LoginActivity.isLogout == 1) {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
		}
	}

	private void displayDashBoard(DashBoard dashBoard) {
		txtOpenTask.setText(dashBoard.getOpenTask() < 10 ? NUMBER_PREFIX
				+ dashBoard.getOpenTask() : dashBoard.getOpenTask()
				+ EMPTY_STRING);

		txtClosedTask.setText(dashBoard.getClosedTask() < 10 ? NUMBER_PREFIX
				+ dashBoard.getClosedTask() : dashBoard.getClosedTask()
				+ EMPTY_STRING);

		txtAvailableTask
				.setText(dashBoard.getAvailableTask() < 10 ? NUMBER_PREFIX
						+ dashBoard.getAvailableTask() : dashBoard
						.getAvailableTask() + EMPTY_STRING);
	}

	// private void refreshDashBoard() {
	// txtOpenTask
	// .setText(CacheData.getInstant().getDashBoard().getOpenTask() < 10 ?
	// NUMBER_PREFIX
	// + CacheData.getInstant().getDashBoard().getOpenTask()
	// : CacheData.getInstant().getDashBoard().getOpenTask()
	// + EMPTY_STRING);
	// txtClosedTask.setText(CacheData.getInstant().getDashBoard()
	// .getClosedTask() < 10 ? NUMBER_PREFIX
	// + CacheData.getInstant().getDashBoard().getClosedTask()
	// : CacheData.getInstant().getDashBoard().getClosedTask()
	// + EMPTY_STRING);
	// txtAvailableTask.setText(CacheData.getInstant().getDashBoard()
	// .getAvailableTask() < 10 ? NUMBER_PREFIX
	// + CacheData.getInstant().getDashBoard().getAvailableTask()
	// : CacheData.getInstant().getDashBoard().getAvailableTask()
	// + EMPTY_STRING);
	// }

	public void imgAvatarClicked() {
		DebugLog.logd("imageAvatar clicked");
		DebugLog.logd("SrollEnd " + scrollEnd);
		Intent accountIntent = new Intent(this, AccountDashboardActivity.class);
		startActivityForResult(accountIntent, accountDashBoardRequestCode);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnCreatAVoice:

			progressDialog.setTitle("Processing...");
			progressDialog.setCancelable(false);
			progressDialog.show();

			new Thread() {
				public void run() {
					try {
						sleep(2000);
					} catch (Exception e) {
						Log.e("tag", e.getMessage());
					}
					Intent intent = new Intent(TaskActivity.this,
							AudioRecordingActivity.class);
					startActivity(intent);
					progressDialog.dismiss();
				}
			}.start();

			break;

		case R.id.btnCreatAText:
			isRecommendTask = false;
			TextRequestDialog textRequestActivity = new TextRequestDialog(this);
			textRequestActivity.show();
			break;

		case R.id.btnSeeMoreRequest:
			DebugLog.logd("Button See more request click");
			List<Task> listTask = CacheData.getInstant().getListTask();

			if (listTask != null && !listTask.isEmpty()) {
				int visibleChildCount = listTaskView.getAdapter().getCount() - 1;

				DebugLog.logd("adapter size"
						+ (listTaskView.getAdapter().getCount() - 1));
				DebugLog.logd("visibleChildCount" + listTask.size());

				if (visibleChildCount < listTask.size()) {
					if (listTaskView.getAdapter().getCount() + JUMP_ITEM_NUMBER < listTask
							.size()) {
						taskAdapter = new TaskAdapter(TaskActivity.this,
								R.layout.task_item, CacheData
										.getInstant()
										.getListTask()
										.subList(
												START_INDEX_SUB_LIST,
												(listTaskView.getAdapter()
														.getCount() - 1)
														+ JUMP_ITEM_NUMBER));

					} else if (listTaskView.getAdapter().getCount()
							+ JUMP_ITEM_NUMBER > listTask.size()) {
						taskAdapter = new TaskAdapter(TaskActivity.this,
								R.layout.task_item, CacheData
										.getInstant()
										.getListTask()
										.subList(
												START_INDEX_SUB_LIST,
												(listTaskView.getAdapter()
														.getCount() - 1)
														+ JUMP_ITEM_NUMBER - 1));
						// btnSeeMoreRequest.setClickable(false);
					} else if (listTaskView.getAdapter().getCount()
							+ JUMP_ITEM_NUMBER == listTask.size()) {
						taskAdapter = new TaskAdapter(TaskActivity.this,
								R.layout.task_item, CacheData.getInstant()
										.getListTask());
					}

					taskAdapter.notifyDataSetChanged();
					listTaskView.setAdapter(taskAdapter);
					listTaskView.setSelection(listTaskView.getAdapter()
							.getCount() - 1);
				} else {
					if (isFullListTask == false) {

						PAGE += 1;
						// RESULT_PER_PAGE = 2;
						// ProgressDialog progressDialog = new
						// ProgressDialog(this);
						// progressDialog.setTitle("Loading...");
						// progressDialog.setCancelable(false);
						// progressDialog.show();
						// CacheData.getInstant().setCurrentProgressDialog(progressDialog);
						DebugLog.logd("page: " + PAGE);
						LayoutInflater layoutInflater = LayoutInflater
								.from(this);
						footerListViewLayout = (RelativeLayout) layoutInflater
								.inflate(R.layout.progress_layout, null, false);
						footerListViewLayout.setClickable(false);
						footerListViewLayout.setTag(footerListViewLayout);

						listTaskView.addFooterView(footerListViewLayout);
						footerListViewLayout.setPadding(0, 30, 10, 30);
						listTaskView.setSelection(listTaskView.getAdapter()
								.getCount() - 1);
						btnSeeMoreRequest.setClickable(false);

						taskManager.getListTask(CacheData.getInstant()
								.getTokenKaorisan(), PAGE + EMPTY_STRING,
								RESULT_PER_PAGE + EMPTY_STRING,
								new TaskManager.OnGetListTaskResult() {

									@Override
									public void onGetListTaskResultMethod(
											boolean isSuccess,
											List<Task> listTask, String message) {
										if (isSuccess && listTask != null) {
											if (CacheData.getInstant()
													.getListTask() != null) {
												if (listTask.isEmpty()) {
													Log.i("Vao day khong",
															"Vao day di");
													isFullListTask = true;
													btnSeeMoreRequest
															.setClickable(false);
												} else {
													CacheData.getInstant()
															.getListTask()
															.addAll(listTask);
													taskAdapter = new TaskAdapter(
															TaskActivity.this,
															R.layout.task_item,
															CacheData
																	.getInstant()
																	.getListTask()
																	.subList(
																			0,
																			(listTaskView
																					.getCount() - 1)
																					+ JUMP_ITEM_NUMBER));
												}
											} else {
												taskAdapter = new TaskAdapter(
														TaskActivity.this,
														R.layout.task_item,
														listTask);
												CacheData.getInstant()
														.setListTask(listTask);
											}

											taskAdapter.notifyDataSetChanged();
											listTaskView
													.setAdapter(taskAdapter);
											listTaskView
													.setSelection(listTaskView
															.getAdapter()
															.getCount() - 1);
											// CacheData.getInstant().setListTask(listTask);
											DebugLog.logd("Update listview thanh cong");
											listTaskView
													.removeFooterView(footerListViewLayout);
											btnSeeMoreRequest
													.setClickable(true);
										} else {
											PAGE -= 1;
											listTaskView
													.removeFooterView(footerListViewLayout);
											btnSeeMoreRequest
													.setClickable(true);
										}

										Log.i("Countprogress", EMPTY_STRING
												+ progress_count);

									}
								});
					}
				}

			}

			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		DebugLog.logd("Back To Home Android");
		if (LoginActivity.loginActivity != null) {
			LoginActivity.loginActivity.finish();
		}
		finish();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		scrollEnd = false;
		DebugLog.logd("OnScroll");

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		DebugLog.logd("OnScrollStateChanged " + scrollState);
		scrollStateOfListView = scrollState;

		if (scrollState != 0) {
			scrollEnd = false;
		} else {
			scrollEnd = true;
		}

		DebugLog.logd("State " + scrollEnd);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DebugLog.logd("onResume");
		
	//	listTaskView.clearFocus();
	//	taskLayout.requestLayout();
		new Thread() {
			public void run() {
				try {
					if (CacheData.getInstant().getCurrentProgressDialog() != null) {
						while (CacheData.getInstant()
								.getCurrentProgressDialog().isShowing()) {
							try {
								sleep(2000);
							} catch (Exception e) {
								Log.e("tag", e.getMessage());
							}
							if (CacheData.getInstant()
									.getCurrentProgressDialog() == null) {
								break;
							}
						}
						CheckDashBoardData();
					}
				} catch (Exception e) {
					DebugLog.loge("Exception: " + e.getMessage());
				}

			}
		}.start();
		if (NewRequestActivity.isSubmit || AudioReviewActivity.isSubmit
				|| ReplyActivity.isReply) {
			Log.i("refresh list", "listtask");
			bindDataListViewTask(null);
			NewRequestActivity.isSubmit = false;
			AudioReviewActivity.isSubmit = false;
			ReplyActivity.isReply = false;
		}

	}

	private void CheckDashBoardData() {
		DebugLog.logd((CacheData.getInstant().getDashBoard() == null) + "");
		if (CacheData.getInstant().getDashBoard() == null
				&& CacheData.getInstant().getCurrentProgressDialog() != null) {
			if (!CacheData.getInstant().getCurrentProgressDialog().isShowing()) {
				DebugLog.logd("Finish activity because get data failed!");
				Utils.resetCacheData();
				Utils.showToast(TaskActivity.this,
						getResources().getString(R.string.get_data_failed_msg));
				finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	}

}
