package com.mht.common.statistics;

import java.util.List;

/**
 * @ClassName: StatSeriesDataBean
 * @Description: echart ajax数据对象
 * @author com.mhout.sx
 * @date 2017年3月24日 上午9:56:11
 * @version 1.0.0
 */
public class StatSeriesDataBean {
    private String name;
    private List<Object> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

}
