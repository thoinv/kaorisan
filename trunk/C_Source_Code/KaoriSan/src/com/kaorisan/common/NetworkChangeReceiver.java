package com.kaorisan.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String status = NetworkUtil.getConnectivityStatusString(context);

		Utils.showToast(context, status);

	}
}
