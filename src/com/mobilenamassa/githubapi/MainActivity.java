package com.mobilenamassa.githubapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
    
    public void getCommits(View view) {
    	 new AsyncTask<Void, Void, String>() {

    		@Override
    		protected void onPreExecute() {
    			super.onPreExecute();
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
				if (TextUtils.isEmpty(result)) {
					showDialog("Connection error");
				} else {
					showDialog(result);
				}
			}
			
		}.execute();
    }
    
    private void showDialog(String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Message");
    	builder.setMessage(message);
    	builder.create().show();
    }

}
