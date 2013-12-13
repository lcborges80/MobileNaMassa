package com.mobilenamassa.githubapi.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilenamassa.githubapi.R;
import com.mobilenamassa.githubapi.business.GithubCommitsManager;
import com.mobilenamassa.githubapi.model.Commits;

public class MainActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private Button verifyCommits;
	private TextView apiResult;
	private LinearLayout resultContainer;
	private TextView numberOfCommitsForPeriod;
	private TextView commitsResult;
	private AlertDialog downloadMessage;
	private int numberOfCommits;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		this.sharedPreferences = getSharedPreferences(SettingsActivity.MY_SHAREDPREFERENCES, MODE_PRIVATE);
		this.apiResult = (TextView) findViewById(R.id.apiResult);
		this.verifyCommits = (Button) findViewById(R.id.verifyCommits);
		this.resultContainer = (LinearLayout) findViewById(R.id.resultContainer);
		this.numberOfCommitsForPeriod = (TextView) findViewById(R.id.numberOfCommitsForPeriod);
		this.commitsResult = (TextView) findViewById(R.id.commitsResult);
		this.downloadMessage = createDialog(getResources().getString(R.string.dialogDownload));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menuSettings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
//		}
//		return false;
	}

	public void verifyCommits(View view) {
		if (this.sharedPreferences.getAll().isEmpty()) {
			Toast.makeText(this, getResources().getString(R.string.noSettingsMessage), Toast.LENGTH_LONG).show();
		} else {
			
			int period = sharedPreferences.getInt(SettingsActivity.PERIOD, 0);
			String owner = sharedPreferences.getString(SettingsActivity.OWNER, "");
			String repository = sharedPreferences.getString(SettingsActivity.REPOSITORY, "");
			
			String author = sharedPreferences.getString(SettingsActivity.AUTHOR, "");

			GithubCommitsManager githubCommitsManager = new GithubCommitsManager(this);

			String startDate = githubCommitsManager.getStartDateISO8601Format(period);
			String url = githubCommitsManager.createUrl(owner, repository);
			githubCommitsManager.showCommitsForPeriod(this.apiResult, url, author, startDate);
			
		}

		// if (this.sharedPreferences.getAll().isEmpty()) {
		// Toast.makeText(this,
		// getResources().getString(R.string.noSettingsMessage),
		// Toast.LENGTH_LONG).show();
		// } else {
		//
		// new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected void onPreExecute() {
		// super.onPreExecute();
		// verifyCommits.setEnabled(false);
		// downloadMessage.show();
		// }
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		//
		// GithubCommitsManager githubCommitsManager = new
		// GithubCommitsManager(MainActivity.this);
		// int period = sharedPreferences.getInt(SettingsActivity.PERIOD, 0);
		// String username =
		// sharedPreferences.getString(SettingsActivity.USERNAME, "");
		// String password =
		// sharedPreferences.getString(SettingsActivity.PASSWORD, "");
		// String author = sharedPreferences.getString(SettingsActivity.AUTHOR,
		// "");
		// String repository =
		// sharedPreferences.getString(SettingsActivity.REPOSITORY, "");
		// String startDate =
		// githubCommitsManager.getStartDateISO8601Format(period);
		// String url = githubCommitsManager.createUrl(repository);
		//
		// try {
		// final Commits[] commits =
		// githubCommitsManager.getCommitsForPeriod(url, author, startDate);
		// numberOfCommitsForPeriod.post(new Runnable() {
		//
		// public void run() {
		// numberOfCommits = commits == null ? -1 : commits.length;
		// numberOfCommitsForPeriod.setText(numberOfCommits == -1 ? "null" :
		// String.valueOf(numberOfCommits));
		// }
		//
		// });
		//
		// } catch (final Exception exception) {
		// numberOfCommitsForPeriod.post(new Runnable() {
		//
		// public void run() {
		// numberOfCommits = -1;
		// numberOfCommitsForPeriod.setText(exception.toString());
		// }
		//
		// });
		//
		// }
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// downloadMessage.dismiss();
		// if (numberOfCommits > -1) {
		// int frequency =
		// Integer.parseInt(sharedPreferences.getString(SettingsActivity.FREQUENCY,
		// "0"));
		// if (numberOfCommits == frequency) {
		// commitsResult.setText(R.string.equalsThanCommitsLimit);
		// commitsResult.setTextColor(getResources().getColor(R.color.yellow));
		// } else if (numberOfCommits > frequency) {
		// commitsResult.setText(R.string.moreThanCommitsLimit);
		// commitsResult.setTextColor(getResources().getColor(R.color.green));
		// } else {
		// commitsResult.setText(R.string.lessThanCommitsLimit);
		// commitsResult.setTextColor(getResources().getColor(R.color.red));
		// }
		// }
		// verifyCommits.setEnabled(true);
		// resultContainer.setVisibility(View.VISIBLE);
		// }
		//
		// }.execute();
		//
		// }

	}

	private AlertDialog createDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialogTitle);
		builder.setMessage(message);
		return builder.create();
	}

}