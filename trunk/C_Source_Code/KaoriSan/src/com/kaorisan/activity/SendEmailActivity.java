package com.kaorisan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.common.DebugLog;

@SuppressLint("ShowToast")
public class SendEmailActivity extends Activity {
	TextView btnSend;
	EditText txtTo;
	EditText txtCc;
	EditText txtEmailContent;
	EditText txtSubject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);

		btnSend = (TextView) findViewById(R.id.btnSend);
		txtTo = (EditText) findViewById(R.id.txtTo);
		txtCc = (EditText) findViewById(R.id.txtCc);
		txtSubject = (EditText) findViewById(R.id.txtSubject);
		txtEmailContent = (EditText) findViewById(R.id.txtEmailContent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_email, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnSend:
			DebugLog.logd("Button Send click");
			String to = txtTo.getText().toString();
			String subject = txtSubject.getText().toString();
			String message = txtEmailContent.getText().toString();

			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });

			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, message);

			email.setType("message/rfc822");

			this.startActivity(Intent.createChooser(email, getResources().getString(R.string.txt_email_support)));

			break;

		case R.id.btnCancel:
			DebugLog.logd("Button Cancel click");
			onBackPressed();
			break;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
