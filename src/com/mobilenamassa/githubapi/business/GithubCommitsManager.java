package com.mobilenamassa.githubapi.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class GithubCommitsManager {

	public static final int DAILY_PERIOD = 0;
	public static final int WEEKLY_PERIOD = 1;
	public static final int MONTHLY_PERIOD = 2;
	
	public String getDateISO8601Format(int period) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String result = simpleDateFormat.format(getStartDateFromPeriod(period));		
		Log.i("MyTag", result);
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
	
	public Date getStartDateFromPeriod(int period) {
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
	
}