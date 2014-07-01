package com.kaorisan.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.kaorisan.beans.AccountDashBoard;
import com.kaorisan.common.DebugLog;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.service.NetworkService;

public class AccountDashBoardManager {
	public interface OnGetAccountDashBoardResult {
		void onGetDashBoardResultMethod(boolean isSuccess, AccountDashBoard accountDashBoard, String message);
	}

	OnGetAccountDashBoardResult callbackOnGetAccountDashBoardResult = null;

	public void getAccountDashBoard(String token, OnGetAccountDashBoardResult onGetAccountDashBoardResult) {

		callbackOnGetAccountDashBoardResult = onGetAccountDashBoardResult;

		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, AccountDashBoard>() {

			@Override
			protected AccountDashBoard doInBackground(String... params) {
				
				JSONObject jsonResult = service.getAccountDashboard(params[0]);
				AccountDashBoard accountDashBoard = new AccountDashBoard();
				try {
					if (jsonResult != null) {
						if (jsonResult.has("id")) {
							accountDashBoard.setId(jsonResult.getInt("id"));
						}
						
						if (jsonResult.has("name")) {
							accountDashBoard.setName(jsonResult.getString("name"));
						}
						
						if (jsonResult.has("plan")) {
							accountDashBoard.setPlanName(jsonResult.getString("plan"));
						}
						
						if (jsonResult.has("created_at")) {
							accountDashBoard.setCreateAt(jsonResult
									.getInt("created_at"));
						}

						if (jsonResult.has("user_requests")) {
							accountDashBoard.setUserRequest(jsonResult.getInt("user_requests"));
						}

						if (jsonResult.has("total_requests")) {
							accountDashBoard.setTotalRequest(jsonResult.getInt("total_requests"));
						}

						if (jsonResult.has("email")) {
							accountDashBoard.setEmail(jsonResult.getString("email"));
						}

						if (jsonResult.has("image_url")) {accountDashBoard.setAvatar(jsonResult.getString("image_url"));
						}

						CacheData.getInstant().setAccountDashBoard(accountDashBoard);
						
						return accountDashBoard;
					}
				} catch (JSONException e) {
					DebugLog.loge("Exception: " + e.getMessage());
				}
				return null;
			}

			protected void onPostExecute(AccountDashBoard accountDashBoard) {
				if (accountDashBoard != null) {
					callbackOnGetAccountDashBoardResult.onGetDashBoardResultMethod(true, accountDashBoard, null);
				} else {
					callbackOnGetAccountDashBoardResult.onGetDashBoardResultMethod(false, new AccountDashBoard(), "Disconnect");
				}
			}
		}.execute(token);

	}

}
