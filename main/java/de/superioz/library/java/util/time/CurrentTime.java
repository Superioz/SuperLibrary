package de.superioz.library.java.util.time;

import lombok.Getter;

import java.util.Calendar;

/**
 * Created on 05.03.2016.
 */
@Getter
public class CurrentTime {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int millis;

	public CurrentTime(int year, int month, int day, int hour, int minute, int second, int millis){
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millis = millis;
	}

	/**
	 * Gets the current time (with year, month, day, hour ...)
	 * @return The object of time
	 */
	public static CurrentTime now(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		int millis = now.get(Calendar.MILLISECOND);

		return new CurrentTime(year, month, day, hour, minute, second, millis);
	}


}
