package com.kaorisan.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.kaorisan.beans.DashBoard;
import com.kaorisan.beans.User;
import com.kaorisan.common.DebugLog;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.service.NetworkService;

public class DashBoardManager {
	public interface OnGetDashBoardResult {
		void onGetDashBoardResultMethod(boolean isSuccess, DashBoard dashBoard,
				String message);
	}

	OnGetDashBoardResult callbackOnGetDashBoardResult = null;

	public interface OnGetInfoUserRsult {
		void onGetInfoUserMethod(boolean isSuccess, User user, String message);
	}

	OnGetInfoUserRsult callbackOnGetInfoUserRsult = null;

	public void getDashBoard(String token, OnGetDashBoardResult onGetDashBoardResult) {

		callbackOnGetDashBoardResult = onGetDashBoardResult;

		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, DashBoard>() {

			@Override
			protected DashBoard doInBackground(String... params) {
				
				JSONObject jsonResult = service.getDashBoard(params[0]);
				DashBoard dashBoard = null;
				try {
					
					if (jsonResult != null) {
						
						dashBoard = new DashBoard();
						if (jsonResult.has("open_task")) {
							dashBoard.setOpenTask(jsonResult.getInt("open_task"));
						}
						
						if (jsonResult.has("closed_task")) {
							dashBoard.setClosedTask(jsonResult.getInt("closed_task"));
						}
						
						if (jsonResult.has("available_task")) {
							dashBoard.setAvailableTask(jsonResult.getInt("available_task"));
						}
						
						CacheData.getInstant().setDashBoard(dashBoard);
						
						return dashBoard;
					} else {
						CacheData.getInstant().setDashBoard(null);
					}
				} catch (JSONException e) {
					DebugLog.loge(e.getMessage());
				}
				return null;
			}

			protected void onPostExecute(DashBoard dashBoard) {
				if (dashBoard != null) {
					callbackOnGetDashBoardResult.onGetDashBoardResultMethod(true, dashBoard, null);
				} else {
					callbackOnGetDashBoardResult.onGetDashBoardResultMethod(false, new DashBoard(), "Disconnect");
				}
			}
		}.execute(token);

	}

}
