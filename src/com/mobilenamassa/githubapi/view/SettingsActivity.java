package com.mobilenamassa.githubapi.view;

import com.mobilenamassa.githubapi.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	public static final String MY_SHAREDPREFERENCES = "MySharedPreferences";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String AUTHOR = "author";
	public static final String REPOSITORY = "reposotory";
	public static final String PERIOD = "period";
	public static final String FREQUENCY = "frequency";

	private SharedPreferences sharedPreferences;
	private EditText username;
	private EditText password;
	private EditText repository;
	private EditText author;
	private Spinner period;
	private EditText frequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		this.sharedPreferences = getSharedPreferences(MY_SHAREDPREFERENCES, MODE_PRIVATE);
		
		this.username = (EditText) findViewById(R.id.username);
		this.password = (EditText) findViewById(R.id.password);
		saveUsernameAndPasswordWithDefaultValues();
		
		this.author = (EditText) findViewById(R.id.author);
		this.repository = (EditText) findViewById(R.id.respository);
		this.period = (Spinner) findViewById(R.id.period);
		this.frequency = (EditText) findViewById(R.id.frequency);
		loadValues();
	}
	
	private void saveUsernameAndPasswordWithDefaultValues() {
		SharedPreferences.Editor editor = this.sharedPreferences.edit();
		editor.putString(USERNAME, this.username.getText().toString());
		editor.putString(PASSWORD, this.password.getText().toString());
		editor.commit();		
	}

	private void loadValues() {
		this.username.setText(this.sharedPreferences.getString(USERNAME, ""));
		this.password.setText(this.sharedPreferences.getString(PASSWORD, ""));
		this.author.setText(this.sharedPreferences.getString(AUTHOR, ""));
		this.repository.setText(this.sharedPreferences.getString(REPOSITORY, ""));
		this.period.setSelection(this.sharedPreferences.getInt(PERIOD, 0));
		this.frequency.setText(this.sharedPreferences.getString(FREQUENCY, ""));
	}

	public void save(View view) {
		if (TextUtils.isEmpty(this.username.getText()) || TextUtils.isEmpty(this.password.getText()) || TextUtils.isEmpty(this.author.getText()) || TextUtils.isEmpty(this.repository.getText()) || TextUtils.isEmpty(this.frequency.getText())) {
			Toast.makeText(this, getResources().getString(R.string.usernamePasswordRepositoryFrequencyMandatoryMessage), Toast.LENGTH_LONG).show();
		} else {
			SharedPreferences.Editor editor = this.sharedPreferences.edit();
			editor.putString(USERNAME, this.username.getText().toString());
			editor.putString(PASSWORD, this.password.getText().toString());
			editor.putString(AUTHOR, this.author.getText().toString());
			editor.putString(REPOSITORY, this.repository.getText().toString());
			editor.putInt(PERIOD, this.period.getSelectedItemPosition());
			editor.putString(FREQUENCY, this.frequency.getText().toString());
			editor.commit();
			this.finish();
		}
	}

}