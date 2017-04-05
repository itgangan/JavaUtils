package com.javautils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public final class IDCardUtils {
	/**
	 * 身份证号码验证<br/>
	 * 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 <br/>
	 * 2、地址码(前六位数） 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 <br/>
	 * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。<br/>
	 * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
	 * 顺序码的奇数分配给男性，偶数分配给女性。 <br/>
	 * 5、校验码（第十八位数） <br/>
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 <br/>
	 * （2）计算模 Y = mod(S, 11)<br/>
	 * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
	 */
	private static SimpleDateFormat sdf;

	static {
		sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);
	}

	/**
	 * 校验是否为真实的身份证号
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isLegal(String idcard) {
		if (StringUtils.isNotBlank(idcard)) {
			idcard = to18(idcard);

			boolean verifyCheckCode = verifyCheckCode(idcard);
			boolean verifyAge = verifyAge(idcard);
			boolean verifyAddress = verifyAddress(idcard);

			return verifyCheckCode && verifyAge && verifyAddress;
		}

		return false;
	}

	public static boolean verifyCheckCode(String idcard) {
		char checkCode = calcCheckCode(idcard);
		return checkCode == idcard.toLowerCase().charAt(17);
	}

	public static boolean verifyAge(String idcard) {
		int age = getAge(idcard);
		return (age >= 0 && age <= 150);
	}

	public static boolean verifyAddress(String idcard) {
		int province = getProvince(idcard);
		return (provinces.get(Integer.valueOf(province)) != null);
	}

	/**
	 * 是否为女姓
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isFemale(String idcard) {
		idcard = to18(idcard);
		return (getSerial(idcard) % 2) == 0;
	}

	/**
	 * 获取身份证序列号（第14-17位）
	 * 
	 * @param idcard
	 * @return
	 */
	public static int getSerial(String idcard) {
		idcard = to18(idcard);
		return Integer.valueOf(idcard.substring(14, 17));
	}

	public static int getYear(String idcard) {
		idcard = to18(idcard);
		return Integer.valueOf(idcard.substring(6, 10));
	}

	public static int getMonth(String idcard) {
		idcard = to18(idcard);
		return Integer.valueOf(idcard.substring(10, 12));
	}

	public static int getDay(String idcard) {
		idcard = to18(idcard);
		return Integer.valueOf(idcard.substring(12, 14));
	}

	public static String getBirthday(String idcard) {
		idcard = to18(idcard);
		return idcard.substring(6, 14);
	}

	public static int getProvince(String idcard) {
		return Integer.valueOf(idcard.substring(0, 2));
	}

	public static int getAge(String idcard) {
		idcard = to18(idcard);
		return getAge(getYear(idcard), getMonth(idcard), getDay(idcard));
	}

	public static int getAge(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		int age = cal.get(Calendar.YEAR) - year;
		int dmonth = 1 + cal.get(Calendar.MONTH) - month;
		if (dmonth < 0)
			return age - 1;
		if (dmonth > 0)
			return age;
		int dday = cal.get(Calendar.DATE) - day;
		if (dday < 0)
			return age - 1;
		return age;
	}

	public static int dayOfMonth(int year, int month) {
		switch (month) {
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return isLeapYear(year) ? 29 : 28;
		default:
			return 31;
		}
	}

	private static final int[] w = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	private static final char[] c = new char[] { '1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2' };

	public static char calcCheckCode(String idcard) {
		idcard = to18(idcard);
		int sum = 0;
		int checkCodeIndex = 17;
		for (int i = 0; i < checkCodeIndex; ++i) {
			sum += w[i] * (Character.digit(idcard.charAt(i), 10));
		}
		int idx = sum % 11;

		return c[idx];
	}

	public static boolean isLeapYear(int year) {
		return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
	}

	private static final Map<Integer, String> provinces = new HashMap<Integer, String>();
	static {
		provinces.put(Integer.valueOf(11), "北京市");
		provinces.put(Integer.valueOf(12), "天津市");
		provinces.put(Integer.valueOf(13), "河北省");
		provinces.put(Integer.valueOf(14), "山西省");
		provinces.put(Integer.valueOf(15), "内蒙古自治区");

		provinces.put(Integer.valueOf(21), "辽宁省");
		provinces.put(Integer.valueOf(22), "吉林省");
		provinces.put(Integer.valueOf(23), "黑龙江省");

		provinces.put(Integer.valueOf(31), "上海市");
		provinces.put(Integer.valueOf(32), "江苏省");
		provinces.put(Integer.valueOf(33), "浙江省");
		provinces.put(Integer.valueOf(34), "安徽省");
		provinces.put(Integer.valueOf(35), "福建省");
		provinces.put(Integer.valueOf(36), "江西省");
		provinces.put(Integer.valueOf(37), "山东省");

		provinces.put(Integer.valueOf(41), "河南省");
		provinces.put(Integer.valueOf(42), "湖北省");
		provinces.put(Integer.valueOf(43), "湖南省");
		provinces.put(Integer.valueOf(44), "广东省");
		provinces.put(Integer.valueOf(45), "广西壮族自治区");
		provinces.put(Integer.valueOf(46), "海南省");

		provinces.put(Integer.valueOf(50), "重庆市");
		provinces.put(Integer.valueOf(51), "四川省");
		provinces.put(Integer.valueOf(52), "贵州省");
		provinces.put(Integer.valueOf(53), "云南省");
		provinces.put(Integer.valueOf(54), "西藏自治区");

		provinces.put(Integer.valueOf(61), "陕西省");
		provinces.put(Integer.valueOf(62), "甘肃省");
		provinces.put(Integer.valueOf(63), "青海省");
		provinces.put(Integer.valueOf(64), "宁夏回族自治区");
		provinces.put(Integer.valueOf(65), "新疆维吾尔自治区");
	};

	public static String to18(String idcard) {
		if (StringUtils.isNotBlank(idcard)) {
			switch (idcard.length()) {
			case 15:
				String id17 = idcard.substring(0, 6) + "19" + idcard.substring(6);
				return idcard = id17 + calcCheckCode(id17);
			case 18:
				return idcard;
			default:
				return idcard;
			}
		}
		return idcard;
	}

	public static void main(String[] args) {
		String idcard = "11010719790212342X";
		System.out.println("isLegal:" + isLegal(idcard));
		System.out.println("isFemale:" + isFemale(idcard));
		System.out.println("getAge:" + getAge(idcard));
	}

}
