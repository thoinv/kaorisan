package com.kaorisan.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.kaorisan.R;
import com.kaorisan.activity.NewRequestActivity;
//import com.kaorisan.activity.RequestActivity;
import com.kaorisan.common.DebugLog;

public class TextRequestDialog extends Dialog {
	private Activity activity;
	public static boolean isVip = false;
	List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	public TextRequestDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		setContentView(R.layout.pop_up_text_request);

		radioButtons.add((RadioButton) findViewById(R.id.rdoNormal));
		radioButtons.add((RadioButton) findViewById(R.id.rdoUrgent));

		for (RadioButton button : radioButtons) {

			button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked)
						processRadioButtonClick(buttonView);
				}
			});

		}

		findViewById(R.id.btnContinue).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						RadioButton rdoNormal = (RadioButton) findViewById(R.id.rdoNormal);
						RadioButton rdoUrgent = (RadioButton) findViewById(R.id.rdoUrgent);

						if (rdoNormal.isChecked()) {
							DebugLog.logd("Radio Normal checked");
							isVip = false;
							RedirectRequestActivity();

						} else if (rdoUrgent.isChecked()) {
							DebugLog.logd("Radio Urgentt checked");
							isVip = true;
							RedirectRequestActivity();
						}
						return;
					}
				});

	}

	private void processRadioButtonClick(CompoundButton buttonView) {

		for (RadioButton button : radioButtons) {

			if (button != buttonView)
				button.setChecked(false);
		}

	}

	private void RedirectRequestActivity() {
		DebugLog.logd("Redirect to request activity");
		Intent accountIntent = new Intent(getContext(), NewRequestActivity.class);
		activity.startActivity(accountIntent);
		dismiss();
	}

}
