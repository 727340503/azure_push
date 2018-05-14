package payme.notification.test;

import java.text.*;
import java.util.*;

public class DateTransformer {
	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter, TimeZone sourceTimeZone,
			TimeZone targetTimeZone) {
		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
		return DateTransformer.getTime(new Date(targetTime), formatter);
	}
	
	public static Date dateTransformBetweenTimeZone(Date sourceDate) {
		TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone targetTimeZone = TimeZone.getTimeZone("UTC");
		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
		return new Date(targetTime);
	}

	public static String getTime(Date date, DateFormat formatter) {
		return formatter.format(date);
	}

	public static void main(String[] args) {
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date date = Calendar.getInstance().getTime();
		System.out.println("date->"+date);
		TimeZone srcTimeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone destTimeZone = TimeZone.getTimeZone("UTC");
		System.out.println(DateTransformer.dateTransformBetweenTimeZone(date, formatter, srcTimeZone, destTimeZone));
	}
}
