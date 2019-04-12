package com.mht.modules.account.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mht.common.json.ZTreeDto;
import com.mht.common.utils.FileUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: CommonController
 * @Description: 账号通用控制器
 * @author com.mhout.sx
 * @date 2017年4月18日 下午5:24:54
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/account/common")
public class CommonController extends BaseController {

    @Autowired
    private CommonService commonService;

    // 数据来源本地
    public static final String USER_DATA_ORIGIN_LOCAL = "1";

    // 数据来源excel
    public static final String USER_DATA_ORIGIN_EXCEL = "2";

    // 数据来源字典类别
    public static final String USER_DATA_ORIGIN = "user_data_origin";

    // 批量头像上传保存路径
    public static final String UPLOAD_USER_PHOTO = "upload/user/photo/";

    // 批量头像上传zip解压缓存目录
    public static final String UPLOAD_TEMP_USER_PHOTO = "upload/temp/user/photo/";

    /**
     * @Title: roleTree
     * @Description: TODO 选择角色页面
     * @param id
     * @param roleType
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = "roleTree")
    public String roleTree(String id, String roleType, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("roleType", roleType);
        return "modules/account/roleTree";
    }

    /**
     * @Title: roleTreeData
     * @Description: TODO 获取角色树结构，并选中用户已拥有的
     * @param id
     * @param roleType
     * @return
     * @author com.mhout.sx
     */
    @ResponseBody
    @RequestMapping(value = "roleTreeData")
    public List<Map<String, Object>> roleTreeData(@RequestParam(required = false) String id, String roleType) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Role> list = commonService.findRoleList(id);
        for (int i = 0; i < list.size(); i++) {
            Role e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("pIds", e.getParentIds());
            map.put("name", e.getName());
            if (roleType.equals(e.getEnname())) {
                map.put("checked", true);
                map.put("chkDisabled", true);
            } else if (!StringUtils.isBlank(e.getOtherId())) {
                map.put("checked", true);
            } else if ("0".equals(e.getParentId())) {
                map.put("checked", false);
                map.put("chkDisabled", true);
            } else {
                map.put("checked", false);
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * @Title: groups
     * @Description: TODO 用户组选择页面
     * @param id
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "groups" })
    public String groups(String id, Model model) {
        // 获取post列表
        List<IdentityGroup> list = commonService.findGroupList(id);
        model.addAttribute("groups", list);
        return "modules/account/userGroups";
    }

    /**
     * @Title: officePostTree
     * @Description: TODO 教工选择职务与部门页面
     * @param id
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "officePostTree" })
    public String officePostTree(String id, Model model) {
        model.addAttribute("id", id);
        return "modules/account/officePostTree";
    }

    /**
     * @Title: officePostTreeData
     * @Description: TODO 教工选择职务与部门树结构选中用户拥有的
     * @param id
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "officePostTreeData" })
    @ResponseBody
    public Object officePostTreeData(String id, String userId) {
        List<ZTreeDto> list = commonService.officePostTreeData(id, userId);
        return list;
    }

    /**
     * @Title: officeStudentTree
     * @Description: TODO 家长选择学生页面
     * @param id
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "officeStudentTree" })
    public String officeStudentTree(String id, Model model) {
        model.addAttribute("id", id);
        return "modules/account/officeStudentTree";
    }

    /**
     * @Title: officeStudentTreeData
     * @Description: TODO 家长选择学生树结构
     * @param id
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "officeStudentTreeData" })
    @ResponseBody
    public Object officeStudentTreeData(String id, String userId) {
        List<ZTreeDto> list = commonService.officeStudentTreeData(id, userId);
        return list;
    }

    /**
     * @Title: checkLoginName 检查loginName是否存在
     * @Description: TODO
     * @param oldLoginName
     * @param loginName
     * @return
     * @author com.mhout.sx
     */
    public static String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && UserUtils.getByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * @Title: updatePassword
     * @Description: TODO 修改用户密码页面
     * @param id
     * @param url
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "updatePassword" })
    public String updatePassword(@RequestParam(required = true) String id, @RequestParam(required = true) String url,
            Model model) {
        model.addAttribute("id", id);
        model.addAttribute("url", url);
        commonService.setPswRule(model);
        return "modules/account/updatePassword";
    }

    /**
     * @Title: savePassword
     * @Description: TODO 提交密码修改
     * @param id
     * @param url
     * @param newPassword
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "savePassword" })
    public String savePassword(@RequestParam(required = true) String id, @RequestParam(required = true) String url,
            @RequestParam(required = true) String newPassword, RedirectAttributes redirectAttributes) {
        commonService.savePassword(id, newPassword);
        addMessage(redirectAttributes, "修改密码成功");
        return "redirect:" + adminPath + url + "?repage";
    }
    
    /**
     * @Title: importPhoto
     * @Description: TODO 批量导入头像
     * @param file
     * @param photoNameType
     * @param url
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = "importPhoto", method = RequestMethod.POST)
    public String importPhoto(MultipartFile file, @RequestParam(required = true) String photoNameType,
            @RequestParam(required = true) String url, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            addMessage(redirectAttributes, "文件为空！");
            return "redirect:" + adminPath + url;
        }
        // String contentType = file.getContentType();
        // 保存文件到缓存目录
        String basePath = request.getSession().getServletContext().getRealPath("/");
        String tempPath = UPLOAD_TEMP_USER_PHOTO;
        String fileName = file.getOriginalFilename();
        String realPath = basePath + tempPath;
        String dirPath = basePath + tempPath + "unzip";
        String photoPath = basePath + UPLOAD_USER_PHOTO;
        File localFile = new File(realPath);
        if (!localFile.exists() && !localFile.isDirectory()) {
            localFile.mkdirs();
        }
        // long time = System.currentTimeMillis();
        // String zippath = realPath + time + ".zip";
        String zippath = realPath + fileName;
        try {
            this.SaveFileFromInputStream(file.getInputStream(), zippath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            addMessage(redirectAttributes, e.getMessage());
        }
        // 将文件解压
        unZipFiles(zippath, dirPath);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("successNum", 0);
        map.put("failureNum", 0);
        StringBuilder failureMsg = new StringBuilder();
        map.put("failureMsg", failureMsg);
        // 将文件拷贝到正式目录下，同时修改数据库
        if(!copyDirectoryCover(dirPath, photoPath, true, photoNameType, request, map)){
        	addMessage(redirectAttributes, "上传头像失败！");
        } else {
        	String msg = "，失败 " + map.get("failureNum") + " 条记录，导入信息如下：";
        	addMessage(redirectAttributes, "已成功导入 " + map.get("successNum") + " 条记录" +
        			msg + map.get("failureMsg"));
        }
        // 删除缓存文件
        FileUtils.deleteFile(zippath);
        FileUtils.deleteDirectory(dirPath);
        return "redirect:" + adminPath + url;
    }
    
    /**
     * @Title: copyDirectoryCover
     * @Description: TODO 将解压到临时目录的文件考到真实目录中，移动成功一个则修改对应人员头像
     * @param srcDirName
     * @param descDirName
     * @param coverlay
     * @param photoNameType
     * @param request
     * @return
     * @author com.mhout.sx
     */
    private boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay, String photoNameType,
            HttpServletRequest request, Map<String, Object> map) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            // log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            // log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            // if (coverlay) {
            // // 允许覆盖目标目录
            // log.debug("目标目录已存在，准备删除!");
            // if (!FileUtils.delFile(descDirNames)) {
            // log.debug("删除目录 " + descDirNames + " 失败!");
            // return false;
            // }
            // } else {
            // log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
            // return false;
            // }
        } else {
            // 创建目标目录
            // log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                // log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = FileUtils.copyFileCover(files[i].getAbsolutePath(), descDirName + files[i].getName(), coverlay);
                // 如果拷贝文件失败，则退出循环
                if (flag && isImage(files[i])) {
                    // 保存到数据库
                    String name = files[i].getName();
                    String url = request.getContextPath() + "/" + UPLOAD_USER_PHOTO + name;
                    String id = name.substring(0, name.lastIndexOf("."));
                    if(commonService.updatePhoto(id, photoNameType, url)){
                    	int num = (int) map.get("successNum");
                        map.put("successNum", num+1);
                        continue;
                    }
                } 
            	StringBuilder value = (StringBuilder) map.get("failureMsg");
            	int num = (int) map.get("failureNum");
            	value.append("<br/>文件名 " + files[i].getName() + " 导入失败：文件类型不符合要求！");
            	map.put("failureNum", num+1);
            	map.put("failureMsg", value);
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                // flag = copyDirectoryCover(files[i].getAbsolutePath(),
                // descDirName + files[i].getName(), coverlay);
                flag = copyDirectoryCover(files[i].getAbsolutePath(), descDirName, coverlay, photoNameType, request, map);
                // 如果拷贝目录失败，则退出循环
                // if (!flag) {
                // break;
                // }
            }
        }

        if (!flag) {
            // log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        // log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * @Title: unZipFiles
     * @Description: TODO 解压文件到临时目录
     * @param zipFileName
     * @param descFileName
     * @return
     * @author com.mhout.sx
     */
    private boolean unZipFiles(String zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            // 根据ZIP文件创建ZipFile对象
            ZipFile zipFile = new ZipFile(zipFileName, "GBK");
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.getEntries();
            // 遍历所有entry
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                // 获得entry的名字
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                // 打开文件输出流
                OutputStream os = new FileOutputStream(file);
                // 从ZipFile对象中打开entry的输入流
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte = is.read(buf)) != -1) {
                    os.write(buf, 0, readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            // log.debug("文件解压成功!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Title: SaveFileFromInputStream
     * @Description: TODO 保存上传文件
     * @param stream
     * @param pathfilename
     * @throws IOException
     * @author com.mhout.sx
     */
    private void SaveFileFromInputStream(InputStream stream, String pathfilename) throws IOException {
        FileOutputStream fs = new FileOutputStream(pathfilename);
        byte[] buffer = new byte[1024 * 1024];
        // int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            // bytesum+=byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }
    
    /**
     * @Title: isImage 
     * @Description: 判断文件是否为图片
     * @param file
     * @return
     * @author com.mhout.xyb
     */
    public static final boolean isImage(File file){  
        boolean flag = false;  
        try  
        {  
            BufferedImage bufreader = ImageIO.read(file);  
            int width = bufreader.getWidth();  
            int height = bufreader.getHeight();  
            if(width==0 || height==0){  
                flag = false;  
            }else {  
                flag = true;  
            }  
        }  
        catch (IOException e)  
        {  
            flag = false;  
        }catch (Exception e) {  
            flag = false;  
        }  
        return flag;  
    }  

}
