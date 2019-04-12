package com.mht.modules.ident.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mht.common.statistics.StatLineDataBean;
import com.mht.common.statistics.StatSeriesDataBean;
import com.mht.common.statistics.StatTimeDataBean;
import com.mht.modules.ident.entity.AutRecord;
import com.mht.modules.ident.entity.AutRecordListData;
import com.mht.modules.ident.service.AutRecordService;
import com.mht.modules.sys.service.UserStatService;

/**
 * 概况Controller
 * 
 * @author 王杰
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/identity/general")
public class GeneralController {

    @Autowired
    private AutRecordService autRecordService;

    @Autowired
    private UserStatService userStatService;

    private List<String> xAxisData;
    private Map<String, List<Double>> yAxisData;
    private Map<String, Object> orientData;

    @RequestMapping(value = "")
    public String general(HttpServletRequest request, HttpServletResponse response, Model model) {

        List<String> xxAxisData = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            xxAxisData.add(i + "");
        }
        request.setAttribute("xAxisData", getxAxisData()); // crud横坐标
        request.setAttribute("yAxisData", getyAxisData());// 今日账号操作概况数值
        request.setAttribute("xxAxisData", xxAxisData); // 0~23 横坐标
        request.setAttribute("yyAxisData", getyyAxisData());// 今日账号认证概况数值
        request.setAttribute("orientData", getorientData()); // 服务器概况
        return "modules/sys/managerHome";
    }

    public List<String> getxAxisData() {

        StatTimeDataBean timeBean = new StatTimeDataBean();
        timeBean.setTimeType(StatTimeDataBean.CUT_DAY);
        StatLineDataBean statLineDataBean = userStatService.statLine(timeBean);
        xAxisData = new ArrayList<String>();
        xAxisData.addAll(statLineDataBean.getLegend());
        return xAxisData;
    }

    public Map<String, List<Double>> getyyAxisData() {

        List<Double> date1 = new ArrayList<>();
        List<Double> date2 = new ArrayList<>();
        AutRecord autRecord = new AutRecord();
        autRecord.setSuccessDate(new Date());
        AutRecordListData dataSuccess = autRecordService.findAutRecordListData(autRecord);

        autRecord = new AutRecord();
        autRecord.setFaultDate(new Date());
        AutRecordListData dataFault = autRecordService.findAutRecordListData(autRecord);
        List<Integer> fault = dataFault.getValue();
        for (Integer integer1 : fault) {
            date1.add(integer1 + 0.0);
        }
        List<Integer> success = dataSuccess.getValue();
        for (Integer integer2 : success) {
            date2.add(integer2 + 0.0);
        }

        Map<String, List<Double>> cc = new HashMap<>();
        cc.put("失败", date1);
        cc.put("成功", date2);
        return cc;
    }

    public Map<String, List<Double>> getyAxisData() {
        yAxisData = new HashMap<String, List<Double>>();

        StatTimeDataBean timeBean = new StatTimeDataBean();
        timeBean.setTimeType(StatTimeDataBean.CUT_DAY);
        StatLineDataBean statLineDataBean = userStatService.statLine(timeBean);
        List<Double> data1 = new ArrayList<>();

        List<StatSeriesDataBean> series = statLineDataBean.getSeries();
        for (StatSeriesDataBean statSeriesDataBean : series) {
            Long data = (Long) statSeriesDataBean.getData().get(0);
            Double object = data.doubleValue();
            data1.add(object);
        }

        yAxisData.put("", data1);

        return yAxisData;
    }

    public Map<String, Object> getorientData() {
        orientData = new HashMap<String, Object>();
        Random random = new Random();
        orientData.put("正常", random.nextInt(400));
        orientData.put("异常", random.nextInt(20));
        return orientData;
    }

}
