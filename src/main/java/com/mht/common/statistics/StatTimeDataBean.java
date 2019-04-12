package com.mht.common.statistics;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: StatTimeDataBean
 * @Description: 按时间统计，时间参数bean
 * @author com.mhout.sx
 * @date 2017年3月24日 上午9:32:41
 * @version 1.0.0
 */
public class StatTimeDataBean {
    // 时间类型
    public static final String YEARS = "years";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String CUT_WEEK = "cutWeek";
    public static final String CUT_DAY = "cutDay";

    @NotNull(message = "时间类型不能为空")
    private String timeType;// 时间类型
    private int yearsStart;// 开始年份
    private int yearsEnd;// 结束年份
    private int year;// 年份
    private int monthYear;// 某年某月中的某年
    private int month;// 某月

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public int getYearsStart() {
        return yearsStart;
    }

    public void setYearsStart(int yearsStart) {
        this.yearsStart = yearsStart;
    }

    public int getYearsEnd() {
        return yearsEnd;
    }

    public void setYearsEnd(int yearsEnd) {
        this.yearsEnd = yearsEnd;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(int monthYear) {
        this.monthYear = monthYear;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

}
