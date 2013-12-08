package com.mobilenamassa.githubapi.view;

import com.mobilenamassa.githubapi.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private static final String MY_SHAREDPREFERENCES = "MySharedPreferences";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String REPOSITORY = "reposotory";
	
	private EditText username;
	private EditText password;
	private EditText repository;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		this.username = (EditText) findViewById(R.id.username);
		this.password = (EditText) findViewById(R.id.password);
		this.repository = (EditText) findViewById(R.id.respository);
		this.sharedPreferences = getSharedPreferences(MY_SHAREDPREFERENCES, MODE_PRIVATE);
		loadValues();
	}
	
	private void loadValues() {
		this.username.setText(this.sharedPreferences.getString(USERNAME, ""));
		this.password.setText(this.sharedPreferences.getString(PASSWORD, ""));
		this.password.setText(this.sharedPreferences.getString(REPOSITORY, ""));
	}
	
	public void save(View view) {
		if (TextUtils.isEmpty(this.username.getText()) || TextUtils.isEmpty(this.password.getText()) || TextUtils.isEmpty(this.repository.getText())) {
			Toast.makeText(this, getResources().getString(R.string.usernamePasswordRepositoryMandatoryMessage), Toast.LENGTH_LONG).show();
		} else {
			SharedPreferences.Editor editor = this.sharedPreferences.edit();
			editor.putString(USERNAME, this.username.getText().toString());
			editor.putString(PASSWORD, this.password.getText().toString());
			editor.putString(REPOSITORY, this.repository.getText().toString());
			editor.commit();
		}
	}
	
}