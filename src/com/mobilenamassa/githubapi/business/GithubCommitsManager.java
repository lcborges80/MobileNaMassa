package com.mobilenamassa.githubapi.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.mobilenamassa.githubapi.R;
import com.mobilenamassa.githubapi.model.Commits;

public class GithubCommitsManager {

	public static final int DAILY_PERIOD = 0;
	public static final int WEEKLY_PERIOD = 1;
	public static final int MONTHLY_PERIOD = 2;
	private Context context;
	private String username;

	public GithubCommitsManager(Context context) {
		this.context = context;
	}

	public String getStartDateISO8601Format(int period) throws IllegalArgumentException {
		if (period < DAILY_PERIOD || period > MONTHLY_PERIOD) {
			throw new IllegalArgumentException(this.context.getResources().getString(R.string.periodIllegalArgumentException));
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String result = simpleDateFormat.format(getStartDateFromPeriod(period));
		return result;
	}

	private Calendar getCalendarWithZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	private Date getStartDateFromPeriod(int period) {
		Calendar calendar = null;
		switch (period) {
		case DAILY_PERIOD:
			return getCalendarWithZeroTime().getTime();

		case WEEKLY_PERIOD:
			calendar = getCalendarWithZeroTime();
			int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
			int year = calendar.get(Calendar.YEAR);
			calendar.clear();
			calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			calendar.set(Calendar.YEAR, year);
			return calendar.getTime();

		case MONTHLY_PERIOD:
			calendar = getCalendarWithZeroTime();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		}
		return null;
	}

	public String createUrl(String username, String repository) throws IllegalArgumentException {
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(repository)) {
			throw new IllegalArgumentException(this.context.getResources().getString(R.string.urlIllegalArgumentException));
		}
		this.username = username;
		StringBuilder urlBuffer = new StringBuilder("https://api.github.com/repos/");
		urlBuffer.append(this.username);
		urlBuffer.append('/');
		urlBuffer.append(repository);
		urlBuffer.append("/commits");
		return urlBuffer.toString();
	}

	public Commits[] getCommitsForPeriod(String url, String password, String startDate) throws IllegalArgumentException, Exception {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(password) || TextUtils.isEmpty(startDate)) {
			throw new IllegalArgumentException(this.context.getResources().getString(R.string.urlIllegalArgumentException));
		}
		try {
			HttpRequest httpRequest = HttpRequest.get(url, true, "since", startDate, "author", "");
			httpRequest.basic(this.username, password);
			String result = httpRequest.body();
			Log.i("MyTag", "response code: " + httpRequest.code());
			Log.i("MyTag", "response headers: " + httpRequest.headers());
			Gson gson = new Gson();
			return gson.fromJson(result, Commits[].class);
		} catch (Exception exception) {
			throw exception;
		}
	}

}