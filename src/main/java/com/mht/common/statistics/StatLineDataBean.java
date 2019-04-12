package com.mht.common.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.mht.common.utils.DateUtils;

/**
 * @ClassName: StatLineDataBean
 * @Description: 折线图或柱状图ajax请求返回对象结构
 * @author com.mhout.sx
 * @date 2017年3月24日 上午9:38:57
 * @version 1.0.0
 */
public class StatLineDataBean {
    private String text;// 对应echart中option的option.title.text 图标标题
    private List<String> legend;// 对应echart中option的option.legend.data 分类
    private List<Object> xAxis;// 对应echart中option的option.xAxis.data 横坐标尺度
    private List<StatSeriesDataBean> series;// 对应echart中option的option.series 数据
    private List<String[]> times;// 查询时间分割

    public StatLineDataBean() {

    }

    /**
     * 通过前台参数构造时间分割
     * 
     * @param statTimeDataBean
     */
    public StatLineDataBean(StatTimeDataBean statTimeDataBean) {
        this.times = new ArrayList<String[]>();
        this.xAxis = new ArrayList<Object>();
        if (StatTimeDataBean.YEARS.equals(statTimeDataBean.getTimeType())) {
            createTimesByYears(statTimeDataBean);
        }
        if (StatTimeDataBean.YEAR.equals(statTimeDataBean.getTimeType())) {
            createTimesByYear(statTimeDataBean);
        }
        if (StatTimeDataBean.MONTH.equals(statTimeDataBean.getTimeType())) {
            createTimesByMonth(statTimeDataBean);
        }
        if (StatTimeDataBean.CUT_WEEK.equals(statTimeDataBean.getTimeType())) {
            createTimesByCutWeek(statTimeDataBean);
        }
        if (StatTimeDataBean.CUT_DAY.equals(statTimeDataBean.getTimeType())) {
            createTimesByCutDay(statTimeDataBean);
        }
    }

    private void createTimesByCutDay(StatTimeDataBean statTimeDataBean) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        String timeStart = DateUtils.formatDate(cal.getTime()) + " 00:00:00";
        this.xAxis.add(timeStart);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        String timeEnd = DateUtils.formatDate(cal.getTime()) + " 00:00:00";
        String[] timeStartEnd = { timeStart, timeEnd };
        this.times.add(timeStartEnd);
    }

    private void createTimesByCutWeek(StatTimeDataBean statTimeDataBean) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        // System.out.println(DateUtils.formatDate(cal.getTime()));
        String timeStart;
        String timeEnd;
        for (int i = 1; i <= 7; i++) {
            timeStart = DateUtils.formatDate(cal.getTime()) + " 00:00:00";
            cal.add(Calendar.DAY_OF_YEAR, 1);
            timeEnd = DateUtils.formatDate(cal.getTime()) + " 00:00:00";
            String[] timeStartEnd = { timeStart, timeEnd };
            this.times.add(timeStartEnd);
            if (i == 7) {
                this.xAxis.add("周日");
            } else {
                this.xAxis.add("周" + i);
            }
        }

    }

    /**
     * @Title: createTimesByMonth
     * @Description: TODO通过年月构造该月每天时间分割
     * @param statTimeDataBean
     * @author com.mhout.sx
     */
    private void createTimesByMonth(StatTimeDataBean statTimeDataBean) {
        int monthYear = statTimeDataBean.getMonthYear();
        int month = statTimeDataBean.getMonth();
        String timeStart;
        String timeEnd;
        String monthStr = this.getStr(month);

        // 获取当月天数
        String date = monthYear + "-" + monthStr + "-01";
        Calendar cal = new GregorianCalendar();
        cal.setTime(DateUtils.parseDate(date));
        int num = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= num; i++) {
            timeStart = monthYear + "-" + monthStr + "-" + this.getStr(i) + " 00:00:00";
            if (month != 12) {
                if (i != num) {
                    timeEnd = monthYear + "-" + monthStr + "-" + this.getStr(i + 1) + " 00:00:00";
                } else {
                    timeEnd = monthYear + "-" + this.getStr(month + 1) + "-01 00:00:00";
                }
            } else {
                if (i != num) {
                    timeEnd = monthYear + "-" + monthStr + "-" + this.getStr(i + 1) + " 00:00:00";
                } else {
                    timeEnd = (monthYear + 1) + "-01-01 00:00:00";
                }
            }
            String[] timeStartEnd = { timeStart, timeEnd };
            this.times.add(timeStartEnd);
            this.xAxis.add(i);
        }
    }

    /**
     * @Title: getStr
     * @Description: TODO月数、天数转字符串
     * @param i
     * @return
     * @author com.mhout.sx
     */
    private String getStr(int i) {
        String str;
        if (i < 10) {
            str = "0" + i;
        } else {
            str = "" + i;
        }
        return str;
    }

    /**
     * @Title: createTimesByYear
     * @Description: TODO按年获取所有月份的时间分割
     * @param statTimeDataBean
     * @author com.mhout.sx
     */
    private void createTimesByYear(StatTimeDataBean statTimeDataBean) {
        int year = statTimeDataBean.getYear();
        String timeStart;
        String timeEnd;
        for (int i = 1; i <= 12; i++) {
            if (i != 12) {
                timeStart = year + "-" + getStr(i) + "-01 00:00:00";
                timeEnd = year + "-" + getStr((i + 1)) + "-01 00:00:00";
            } else {
                timeStart = year + "-" + getStr(i) + "-01 00:00:00";
                timeEnd = (year + 1) + "-01-01 00:00:00";
            }
            String[] timeStartEnd = { timeStart, timeEnd };
            this.times.add(timeStartEnd);
            this.xAxis.add(i + "月");
        }
    }

    /**
     * @Title: createTimesByYears
     * @Description: TODO按年份获取每年的时间分割
     * @param statTimeDataBean
     * @author com.mhout.sx
     */
    private void createTimesByYears(StatTimeDataBean statTimeDataBean) {
        int yearsStart = statTimeDataBean.getYearsStart();
        int yearsEnd = statTimeDataBean.getYearsEnd();
        String timeStart;
        String timeEnd;
        for (int i = yearsStart; i <= yearsEnd; i++) {
            timeStart = i + "-01-01 00:00:00";
            timeEnd = (i + 1) + "-01-01 00:00:00";
            String[] timeStartEnd = { timeStart, timeEnd };
            this.times.add(timeStartEnd);
            this.xAxis.add(i + "年");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<Object> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<Object> xAxis) {
        this.xAxis = xAxis;
    }

    public List<StatSeriesDataBean> getSeries() {
        return series;
    }

    public void setSeries(List<StatSeriesDataBean> series) {
        this.series = series;
    }

    public List<String[]> getTimes() {
        return times;
    }

    public void setTimes(List<String[]> times) {
        this.times = times;
    }

}
