package com.javautils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.joda.time.DateTime;

/**
 * 判断一个日期是否为工作日
 * 
 * @author ganxiangyong
 * @date 2015年11月16日 下午6:03:12
 */
public class WorkdayUtils {
	private static final String DATE_FORMATE = "yyyy-MM-dd";
	private static final Set<String> workdaySet = new HashSet<String>(); // 工作日
	private static final Set<String> freedaySet = new HashSet<String>(); // 休息日

	static {
		init();
	}

	private WorkdayUtils() {
	}

	/**
	 * 是否是工作日
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWorkday(Date date) {
		String dt = new DateTime(date).toString(DATE_FORMATE);
		return (isWeekday(date) && !freedaySet.contains(dt))
				|| (!isWeekday(date) && workdaySet.contains(dt));
	}

	/**
	 * 是否是节假日
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isFreeday(Date date) {
		return !isWorkday(date);
	}

	// 初始化缓存集合set
	private static void init() {
		String path = WorkdayUtils.class.getResource("/").getPath();
		String fileName = "date.xml";
		try {
			Document doc = XMLUtils.loadXML(path + fileName);
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> years = (List<Element>) root.elements("year");
			for (Element year : years) {
				initDays(year, DaySetType.WORKDAY);
				initDays(year, DaySetType.FREEDAY);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	enum DaySetType {
		WORKDAY, FREEDAY;
	}

	// 将工作日配置信息填充到缓存set中
	private static void initDays(Element year, DaySetType type) {
		int y = Integer.parseInt(year.attributeValue("value"));
		Element e = DaySetType.WORKDAY.equals(type) ? year.element("workday")
				: year.element("freeday");
		@SuppressWarnings("unchecked")
		List<Element> months = (List<Element>) e.elements("month");
		for (Element m : months) {
			int month = Integer.parseInt(m.attributeValue("value"));
			String daysStr = m.getTextTrim();
			if (daysStr != null && !"".equals(daysStr)) {
				String[] days = daysStr.split(",");
				for (String d : days) {
					int day = Integer.parseInt(d);
					DateTime dt = new DateTime(y, month, day, 0, 0);
					if (DaySetType.WORKDAY.equals(type)) {
						workdaySet.add(dt.toString(DATE_FORMATE));
					} else {
						freedaySet.add(dt.toString(DATE_FORMATE));
					}
				}
			}
		}
	}

	// 是否为周一到周五
	private static boolean isWeekday(Date date) {
		DateTime dt = new DateTime(date);
		return dt.getDayOfWeek() < 6;
	}

	public static void main(String[] args) {
		System.out.println(isWorkday(new DateTime(2016, 1, 4, 0, 0).toDate()));
		System.out.println(isFreeday(new DateTime(2016, 1, 4, 0, 0).toDate()));
		System.out.println(isWorkday(new DateTime(2016, 2, 16, 0, 0).toDate()));
		System.out.println(isFreeday(new DateTime(2016, 2, 16, 0, 0).toDate()));

		System.out.println(isWorkday(new DateTime(2016, 6, 9, 0, 0).toDate()));
		System.out.println(isFreeday(new DateTime(2016, 6, 9, 0, 0).toDate()));

		System.out.println(isWorkday(new DateTime(2016, 6, 8, 0, 0).toDate()));
		System.out.println(isFreeday(new DateTime(2016, 6, 8, 0, 0).toDate()));
	}
}
