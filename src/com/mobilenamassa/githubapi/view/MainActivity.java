package com.mobilenamassa.githubapi.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilenamassa.githubapi.R;
import com.mobilenamassa.githubapi.business.GithubCommitsManager;
import com.mobilenamassa.githubapi.model.Commits;

public class MainActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private TextView numberOfCommitsForPeriod;
	private AlertDialog downloadMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		this.sharedPreferences = getSharedPreferences(SettingsActivity.MY_SHAREDPREFERENCES, MODE_PRIVATE);
		this.numberOfCommitsForPeriod = (TextView) findViewById(R.id.numberOfCommitsForPeriod);
		this.downloadMessage = createDialog("Downloading commits...", true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuSettings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
	}

	public void verifyCommits(View view) {
		if (this.sharedPreferences.getAll().isEmpty()) {
			Toast.makeText(this, getResources().getString(R.string.noSettingsMessage), Toast.LENGTH_LONG).show();
		} else {
			GithubCommitsManager githubCommitsManager = new GithubCommitsManager(this);
			int period = this.sharedPreferences.getInt(SettingsActivity.PERIOD, 0);
			String username = this.sharedPreferences.getString(SettingsActivity.USERNAME, "");
			String password = this.sharedPreferences.getString(SettingsActivity.PASSWORD, "");
			String repository = this.sharedPreferences.getString(SettingsActivity.REPOSITORY, "");
			String startDate = githubCommitsManager.getStartDateISO8601Format(period);
			String url = githubCommitsManager.createUrl(username, repository);
			
			try {
				Commits[] commits = githubCommitsManager.getCommitsForPeriod(url, password, startDate);
				this.numberOfCommitsForPeriod.setText(String.valueOf(commits.length));
			} catch (Exception exception) {
				this.numberOfCommitsForPeriod.setText(exception.toString());
			}
			
			
			
//			new AsyncTask<Void, Void, HttpRequest>() {
//
//				@Override
//				protected void onPreExecute() {
//					super.onPreExecute();
//					downloadMessage.show();
//				}
//
//				@Override
//				protected HttpRequest doInBackground(Void... params) {
//					HttpRequest httpRequest = HttpRequest.get("https://api.github.com/repos/lcborges80/MobileNaMassa/commits", true, "since", "2013-12-01T02:00:00Z", "author", "lcborges80");
//					httpRequest.basic("mnmworkshop", "mnm_pass2013");
//					Log.i("MyTAG", httpRequest.header("X-RateLimit-Remaining"));
//					return httpRequest;
//				}
//
//				@Override
//				protected void onPostExecute(HttpRequest request) {
//					downloadMessage.dismiss();
//					String result = request.body();
//					if (TextUtils.isEmpty(result)) {
//						createDialog("Connection or JSON Parse error", false).show();
//					} else {
//						Gson gson = new Gson();
//						MainCommit[] mainCommits = gson.fromJson(result, MainCommit[].class);
//						StringBuilder bufferMessage = new StringBuilder();
//						for (MainCommit mainCommit : mainCommits) {
//							bufferMessage.append("date: ");
//							bufferMessage.append(mainCommit.getCommit().getCommitter().getDate());
//							bufferMessage.append('\n');
//							bufferMessage.append("message: " + mainCommit.getCommit().getMessage());
//							bufferMessage.append("\n\n");
//						}
//						createDialog(bufferMessage.toString(), false).show();
//					}
//				}
//
//			}.execute();

		}

	}

	private AlertDialog createDialog(String message, boolean isDownloadDialog) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Message");
		AlertDialog alertDialog;
		if (isDownloadDialog) {
			View view = getLayoutInflater().inflate(R.layout.data_download, null);
			((TextView) view.findViewById(R.id.downloadMessage)).setText(message);
			alertDialog = builder.create();
			alertDialog.setView(view);
		} else {
			builder.setMessage(message);
			alertDialog = builder.create();
		}
		return alertDialog;
	}

}