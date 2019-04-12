package com.mht.modules.unifiedauth.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mht.common.json.AjaxJson;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.service.AuthOfficeService;

/**
 * @ClassName: AuthOfficeController
 * @Description:
 * @author com.mhout.sx
 * @date 2017年3月29日 下午3:25:14
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/unifiedAuth/office")
public class AuthOfficeController extends BaseController {

    @Autowired
    private AuthOfficeService authOfficeService;

    /**
     * @Title: tree
     * @Description: TODO获取组织机构树，树结构懒加载
     * @param id
     * @param request
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = "/tree", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object tree(@RequestParam(value = "id", required = false) String id,
    		@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        return this.authOfficeService.getTree(id, type);
    }

    /**
     * @Title: index
     * @Description: TODO部门授权index页面
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:office:index")
    @RequestMapping(value = { "" })
    public String index(Model model) {
        return "modules/unifiedauth/officeIndex";
    }

    /**
     * @Title: list
     * @Description: 应用权限列表页面
     * @param authOffice
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:office:index")
    @RequestMapping(value = { "list" })
    public String list(AuthOffice authOffice, Model model) {
        List<AuthOffice> list = this.authOfficeService.findListByOffice(authOffice);
        model.addAttribute("office", authOffice.getOffice());
        model.addAttribute("apps", list);
        return "modules/unifiedauth/officeAppList";
    }

    /**
     * @Title: save
     * @Description: ajax保存应用权限
     * @param authOffices
     * @param officeId
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:office:save")
    @RequestMapping(value = { "save" })
    @ResponseBody
    public AjaxJson save(String authOffices, String closeOffices,  String officeId)
            throws JsonParseException, JsonMappingException, IOException {
        authOffices = StringEscapeUtils.unescapeHtml4(authOffices);
        closeOffices = StringEscapeUtils.unescapeHtml4(closeOffices);
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        jacksonMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JavaType javaType = null;
        javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, AuthOffice.class);
        List<AuthOffice> list = jacksonMapper.readValue(authOffices, javaType);
        List<AuthOffice> closelist = jacksonMapper.readValue(closeOffices, javaType);
        this.authOfficeService.saveAuths(list, closelist, officeId);
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        json.setCode("0");
        json.setMsg("保存成功");
        return json;
    }

    /**
     * @Title: userList
     * @Description: TODO通过组织机构名字搜索，以便给组织机构授权
     * @param userName
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "officeList" })
    public String officeList(String officeName, Model model) {
        List<Office> list = this.authOfficeService.findOfficeByName(officeName);
        model.addAttribute("officeName", officeName);
        model.addAttribute("offices", list);
        return "modules/unifiedauth/officeSearchList";
    }
}
