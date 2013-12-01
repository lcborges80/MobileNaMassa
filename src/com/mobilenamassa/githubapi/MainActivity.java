package com.mobilenamassa.githubapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
    	 new AsyncTask<Void, Void, String>() {

    		@Override
    		protected void onPreExecute() {
    			super.onPreExecute();
    			downloadMessage.show();
    		}
    		
			@Override
			protected String doInBackground(Void... params) {
				InputStream inputStream = null;
				HttpsURLConnection httpsURLConnection = null;
				try {
					URL url = new URL("https://api.github.com/repos/lcborges80/MobileNaMassa/commits");
					httpsURLConnection = (HttpsURLConnection) url.openConnection();
					inputStream = httpsURLConnection.getInputStream();
					byte[] data = IOUtils.toByteArray(inputStream);					
					return new String(data);
				} catch (Exception e) {
					return null;
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
						}
					}
					if (httpsURLConnection != null) {
						httpsURLConnection.disconnect();
					}
				}				
			}
			
			protected void onPostExecute(String result) {
				downloadMessage.dismiss();
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