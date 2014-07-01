package com.kaorisan.common;

import java.util.Date;

import com.kaorisan.R;

import android.content.Context;
import android.content.res.Resources;

public class TimeUtils {

	protected Context context;

	public TimeUtils(Context context) {
		this.context = context;
	}

	public String timeAgo(Date date) {
		return timeAgo(date.getTime());
	}

	public String timeAgo(long millis) {
		long diff = new Date().getTime() - millis;

		Resources r = context.getResources();

		String prefix = r.getString(R.string.time_ago_prefix);
		String suffix = r.getString(R.string.time_ago_suffix);

		double seconds = Math.abs(diff) / 1000;
		double minutes = seconds / 60;
		double hours = minutes / 60;
		double days = hours / 24;
		double years = days / 365;

		String words;

		if (seconds < 60) {
			words = r.getString(R.string.time_ago_seconds, Math.round(seconds));
		} else if (minutes < 45) {
			words = r.getString(R.string.time_ago_minutes, Math.round(minutes));
		} else if (hours < 24) {
			words = r.getString(R.string.time_ago_hours, Math.round(hours));
		} else if (hours < 42) {
			words = r.getString(R.string.time_ago_day, 1);
		} else if (days < 30) {
			words = r.getString(R.string.time_ago_days, Math.round(days));
		} else if (days < 365) {
			words = r
					.getString(R.string.time_ago_months, Math.round(days / 30));
		} else {
			words = r.getString(R.string.time_ago_years, Math.round(years));
		}

		StringBuilder sb = new StringBuilder();

		if (prefix != null && prefix.length() > 0) {
			sb.append(prefix).append(" ");
		}

		sb.append(words);

		if (suffix != null && suffix.length() > 0 && seconds > 60) {
			sb.append(" ").append(suffix);
		}

		return sb.toString().trim();
	}
}
