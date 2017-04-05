package com.jar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class CommonLang3 {
	public static void main(String[] args) throws ParseException {
		Iterator<Calendar> iter = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_CENTER);
		print(iter);
		iter = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_MONDAY);
		print(iter);
		iter = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_RELATIVE);
		print(iter);
		iter = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_SUNDAY);
		print(iter);
		
		StringEscapeUtils.escapeHtml4("\"<\"html>你好</html>");
		
		System.err.println('\u039A' + "," + "\u0393".charAt(0));
		
		Date date = DateUtils.round(DateUtils.parseDate("2002-03-30 02:10:00", "yyyy-MM-dd HH:mm:ss"), Calendar.HOUR_OF_DAY);
		System.out.println(date);

	}

	public static void print(Iterator<Calendar> iter) {
		System.out.println("===========================");
		while (iter.hasNext()) {
			System.out.println(DateFormatUtils.format(iter.next(), "yyyy-MM-dd HH:mm:ss"));
		}
	}
}
