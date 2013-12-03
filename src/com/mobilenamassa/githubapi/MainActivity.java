package com.mobilenamassa.githubapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.mobilenamassa.githubapi.model.MainCommit;

public class MainActivity extends Activity {

	private AlertDialog downloadMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setTitle("Github Commits");
		this.downloadMessage = createDialog("Downloading commits...", true);
	}

	public void getAllCommits(View view) {
		new AsyncTask<Void, Void, HttpRequest>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				downloadMessage.show();
			}

			@Override
			protected HttpRequest doInBackground(Void... params) {
				HttpRequest httpRequest = HttpRequest.get("https://api.github.com/repos/lcborges80/MobileNaMassa/commits", true, "since", "2013-12-01T02:00:00Z", "author", "lcborges80");
				httpRequest.basic("mnmworkshop", "mnm_pass2013");
				Log.i("MyTAG", httpRequest.header("X-RateLimit-Remaining"));
				return httpRequest;
			}
			
			@Override
			protected void onPostExecute(HttpRequest request) {
				downloadMessage.dismiss();
				String result = request.body();
				if (TextUtils.isEmpty(result)) {
					createDialog("Connection or JSON Parse error", false).show();
				} else {
					Gson gson = new Gson();
					MainCommit[] mainCommits = gson.fromJson(result, MainCommit[].class);
					StringBuilder bufferMessage = new StringBuilder();
					for (MainCommit mainCommit : mainCommits) {
						bufferMessage.append("date: ");
						bufferMessage.append(mainCommit.getCommit().getCommitter().getDate());
						bufferMessage.append('\n');
						bufferMessage.append("message: " + mainCommit.getCommit().getMessage());
						bufferMessage.append("\n\n");
					}
					createDialog(bufferMessage.toString(), false).show();
				}
			}

		}.execute();
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