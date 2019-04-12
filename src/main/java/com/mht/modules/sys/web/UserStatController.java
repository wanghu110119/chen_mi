package com.mht.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.persistence.Page;
import com.mht.common.statistics.StatLineDataBean;
import com.mht.common.statistics.StatTimeDataBean;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.UserStat;
import com.mht.modules.sys.service.UserStatService;

/**
 * @ClassName: UserStatController
 * @Description: 账号统计Controller
 * @author com.mhout.sx
 * @date 2017年3月24日 下午3:20:45
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/userStat")
public class UserStatController extends BaseController {

    @Autowired
    private UserStatService userStatService;

    /**
     * @Title: index
     * @Description: 账号统计页面
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "index", "" })
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/userStat";
    }

    /**
     * @Title: getStatForLine
     * @Description: ajax请求统计结果
     * @param statTimeDataBean：请求参数
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "getStatForLine", "" })
    @ResponseBody
    public StatLineDataBean getStatForLine(StatTimeDataBean statTimeDataBean, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        StatLineDataBean statLineDataBean = userStatService.statLine(statTimeDataBean);
        return statLineDataBean;
    }

    @RequestMapping(value = { "list", "" })
    public String list(UserStat userStat, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<UserStat> page = userStatService.findUserStat(new Page<UserStat>(request, response), userStat);
        model.addAttribute("page", page);
        return "modules/sys/userStatList";
    }

}
