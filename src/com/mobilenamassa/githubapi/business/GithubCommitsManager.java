package com.mobilenamassa.githubapi.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.mobilenamassa.githubapi.R;
import com.mobilenamassa.githubapi.model.Commits;

public class GithubCommitsManager {

	public static final int DAILY_PERIOD = 0;
	public static final int WEEKLY_PERIOD = 1;
	public static final int MONTHLY_PERIOD = 2;
	private Context context;
	private String username = "mnmworkshop";
	private String pasword = "mnm_pass2013";
	private ProgressDialog progressDialog;

	public static final String TAG = "MyTag";

	public GithubCommitsManager(Context context) {
		this.context = context;
		this.progressDialog = createProgressDialog();
	}

	private ProgressDialog createProgressDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this.context);
		progressDialog.setTitle(R.string.dialogTitle);
		progressDialog.setMessage(this.context.getString(R.string.dialogDownload));
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}

	public String getStartDateISO8601Format(int period) throws IllegalArgumentException {
		if (period < DAILY_PERIOD || period > MONTHLY_PERIOD) {
			throw new IllegalArgumentException(this.context.getString(R.string.periodIllegalArgumentException));
		} 
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(getStartDateFromPeriod(period));
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

	public String createUrl(String owner, String repository) throws IllegalArgumentException {
		if (TextUtils.isEmpty(owner) || TextUtils.isEmpty(repository)) {
			throw new IllegalArgumentException(this.context.getString(R.string.urlIllegalArgumentException));
		}
		StringBuilder urlBuffer = new StringBuilder("https://api.github.com/repos/");
		urlBuffer.append(owner);
		urlBuffer.append('/');
		urlBuffer.append(repository);
		urlBuffer.append("/commits");
		return urlBuffer.toString();
	}

	public Commits[] getCommitsForPeriod(String url, String author, String startDate) throws IllegalArgumentException, Exception {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(author) || TextUtils.isEmpty(startDate)) {
			throw new IllegalArgumentException(this.context.getString(R.string.urlIllegalArgumentException));
		}
		HttpRequest httpRequest = HttpRequest.get(url, true, "since", startDate, "author", author);
		httpRequest.basic(this.username, this.pasword);
		String result = httpRequest.body();
		Log.i(TAG, "url: " + url);
		Log.i(TAG, "response code: " + httpRequest.code());
		Log.i(TAG, "response headers: " + httpRequest.headers());
		Log.i(TAG, "response body: " + result);
		Gson gson = new Gson();
		return gson.fromJson(result, Commits[].class);
	}

	public void showCommitsForPeriod(final TextView textView, final String url, final String author, final String startDate) {
		new AsyncTask<Void, Void, Commits[]>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.show();
			}

			@Override
			protected Commits[] doInBackground(Void... params) {
				try {
					return getCommitsForPeriod(url, author, startDate);
				} catch (final Exception exception) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(final Commits[] commits) {
				if (commits == null) {
					textView.setText(R.string.connectionError);
				} else {
					textView.setText(String.valueOf(commits.length));
				}
				Log.i(TAG, "result on textView: " + textView.getText().toString());
				progressDialog.dismiss();
			}

		}.execute();

	}

}