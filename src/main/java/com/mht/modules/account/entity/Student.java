
package com.mht.modules.account.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.mht.common.config.Global;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;

/**
 * @ClassName: Student
 * @Description: 账号管理实体，目前先不分表，学生、老师、家长使用同一张表，对应不同实体
 * @author com.mhout.sx
 * @date 2017年4月13日 下午3:33:37
 * @version 1.0.0
 */
public class Student extends DataEntity<Student> {

    private static final long serialVersionUID = 1L;
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_STUDENT_NAME = "学生";

    private String otherId;

    private String account;// 用户名、昵称
    private String idNo;// 证件号
    private String gender;// 性别
    private String nation;// 民族
    private String roleType;// 默认角色：student学生、teacher教工、parents家长
    private String roleTypeName;
    private String origin;// 数据来源
    private String originName;

    private List<Office> officeList = Lists.newArrayList();;// 部门
    private String officeIds;
    private String officeNames;

    private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
    private String roleIds;
    private String roleNames;

    private List<IdentityGroup> groupList = Lists.newArrayList(); // 拥有用户组列表
    private String groupIds;
    private String groupNames;

    private String officeId;// 通过部门查询
    private String officeParentIds;// 查询部门下子部门

    private Office company; // 归属公司/学校
    private Office office; // 归属部门
    private String loginName;// 登录名
    private String password;// 密码
    private String no; // 工号/学号
    private String name; // 姓名
    private String email; // 邮箱
    private String phone; // 电话
    private String mobile; // 手机
    private String postIds;// 岗位ids
    private String postNames;// 岗位names

    private String userType;// 用户类型
    private String loginIp; // 最后登陆IP
    private Date loginDate; // 最后登陆日期
    private String loginFlag; // 是否允许登陆
    private String photo; // 头像
    private String qrCode; // 二维码
    private String oldLoginName;// 原登录名
    private String newPassword; // 新密码
    private String sign;// 签名

    protected String oldLoginIp; // 上次登陆IP
    protected Date oldLoginDate; // 上次登陆日期

    public Student() {
        super();
        this.loginFlag = Global.YES;
    }

    public Student(String id) {
        super(id);
    }

    public Student(String id, String loginName) {
        super(id);
        this.loginName = loginName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @ExcelField(title = "证件号", align = 2, sort = 46)
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeParentIds() {
        return officeParentIds;
    }

    public void setOfficeParentIds(String officeParentIds) {
        this.officeParentIds = officeParentIds;
    }

    @ExcelField(title = "性别", align = 2, sort = 47, dictType = "sex")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @ExcelField(title = "民族", align = 2, sort = 48, dictType = "nation")
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }

    public String getOfficeNames() {
        return officeNames;
    }

    public void setOfficeNames(String officeNames) {
        this.officeNames = officeNames;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public List<IdentityGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<IdentityGroup> groupList) {
        this.groupList = groupList;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @ExcelField(title = "ID", type = 1, align = 2, sort = 1)
    public String getId() {
        return id;
    }

    @JsonIgnore
    @NotNull(message = "归属学校不能为空")
    @ExcelField(title = "归属学校", align = 2, sort = 20)
    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    @JsonIgnore
    @NotNull(message = "归属组织机构不能为空")
    @ExcelField(title = "组织机构", align = 2, sort = 25)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Length(min = 1, max = 100, message = "登录名长度必须介于 1 和 100 之间")
    @ExcelField(title = "登录账号", align = 2, sort = 30)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JsonIgnore
    @Length(min = 1, max = 100, message = "密码长度必须介于 1 和 100 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min = 1, max = 100, message = "姓名长度必须介于 1 和 100 之间")
    @ExcelField(title = "姓名", align = 2, sort = 40)
    public String getName() {
        return name;
    }

    @Length(min = 1, max = 100, message = "工号长度必须介于 1 和 100 之间")
    @ExcelField(title = "学号", align = 2, sort = 45)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email(message = "邮箱格式不正确")
    @Length(min = 0, max = 200, message = "邮箱长度必须介于 1 和 200 之间")
    @ExcelField(title = "邮箱", align = 1, sort = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 0, max = 200, message = "电话长度必须介于 1 和 200 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min = 0, max = 200, message = "手机长度必须介于 1 和 200 之间")
    @ExcelField(title = "手机", align = 2, sort = 70)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPostIds() {
        return postIds;
    }

    public void setPostIds(String postIds) {
        this.postIds = postIds;
    }

    public String getPostNames() {
        return postNames;
    }

    public void setPostNames(String postNames) {
        this.postNames = postNames;
    }

    @ExcelField(title = "备注", align = 1, sort = 900)
    public String getRemarks() {
        return remarks;
    }

    @Length(min = 0, max = 100, message = "用户类型长度必须介于 1 和 100 之间")
    // @ExcelField(title = "用户类型", align = 2, sort = 80, dictType =
    // "sys_user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @ExcelField(title = "创建时间", type = 0, align = 1, sort = 90)
    public Date getCreateDate() {
        return createDate;
    }

    @ExcelField(title = "最后登录IP", type = 1, align = 1, sort = 100)
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "最后登录日期", type = 1, align = 1, sort = 110)
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null) {
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null) {
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    @JsonIgnore
    // @ExcelField(title = "其他角色", align = 1, sort = 800, fieldType =
    // RoleListType.class)
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    public List<String> getRoleIdList() {
        List<String> roleIdList = Lists.newArrayList();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        roleList = Lists.newArrayList();
        for (String roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }

    @Override
    public String toString() {
        return id;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    /**
     * @param sign
     *            the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }
}