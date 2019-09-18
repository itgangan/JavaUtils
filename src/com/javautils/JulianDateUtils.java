package com.javautils.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;

/**
 * 登机牌航班日期工具类
 */
public class FltDateUtils {

    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * 计算登机牌中一唯码的航班日期
     *
     * @param nowDate    当前时间
     * @param fltDateStr 航班日期（例03，该月的第三天）
     * @return
     */
    public static String calculateBarCodeFltDate(Date nowDate, String fltDateStr) {
        int days = Integer.parseInt(fltDateStr) - 1;
        DateTime now = new DateTime(nowDate);

        DateTime fltDate = now.monthOfYear().roundFloorCopy().plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }

        // 处理当前是2019-08-31晚上，真实航班日期2019-09-01，但计算出来的fltDate是2019-08-01日的问题
        fltDate = now.monthOfYear().roundFloorCopy().plusMonths(1).plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }

        // 处理当前是2019-09-01凌晨，真实航班日期2019-08-31，但计算出来的fltDate是2019-09-31日的问题
        fltDate = now.monthOfYear().roundFloorCopy().plusMonths(-1).plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }
        return "";
    }

    //

    /**
     * 将朱莉安日期转为yyyyMMdd
     *
     * @param nowDate    当前日期
     * @param julianDate 该年中的第多少天
     * @return
     */
    public static String calculateQrcodeFltDate(Date nowDate, String julianDate) {
        int days = Integer.parseInt(julianDate) - 1;
        DateTime now = new DateTime(nowDate);

        DateTime fltDate = now.yearOfEra().roundFloorCopy().plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }

        // 处理当前是2019-12-31晚上，真实航班日期2020-01-01，但计算出来的fltDate是2019-01-01日的问题
        fltDate = now.yearOfEra().roundFloorCopy().plusYears(1).plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }

        // 处理当前是2020-01-01凌晨，真实航班日期2019-12-31，但计算出来的fltDate是2020-12-31日的问题
        fltDate = now.yearOfEra().roundFloorCopy().plusYears(-1).plusDays(days);
        if (checkFltDate(now, fltDate)) {
            return fltDate.toString(DATE_FORMAT);
        }
        return "";
    }

    // 检查当前日期是否为给定日期前后3天之内(这里now需要传，为的是避免两次now不致在极端情况下会有问题)
    private static boolean checkFltDate(DateTime now, DateTime fltDate) {
        Interval interval = new Interval(now.plusDays(-3), now.plusDays(3));
        return interval.contains(fltDate);
    }
}
