package com.mht.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;

public class OperateUser extends DataEntity<OperateUser> {

	private static final long serialVersionUID = 1L;
	private String account;// 用户名、昵称
	private String idNo;// 证件号
	private String gender;// 性别
	private String nation;// 民族

	protected Office company; // 归属公司/学校
	protected Office office; // 归属部门
	protected String loginName;// 登录名
	protected String password;// 密码
	protected String no; // 工号/学号
	protected String name; // 姓名
	protected String email; // 邮箱
	protected String phone; // 电话
	protected String mobile; // 手机
	protected String postIds;// 岗位ids
	protected String postNames;// 岗位names

	protected String userType;// 用户类型
	protected String loginIp; // 最后登陆IP
	protected Date loginDate; // 最后登陆日期
	protected String loginFlag; // 是否允许登陆
	protected String photo; // 头像
	protected String qrCode; // 二维码
	protected String oldLoginName;// 原登录名
	protected String newPassword; // 新密码
	protected String sign;// 签名

	protected String oldLoginIp; // 上次登陆IP
	protected Date oldLoginDate; // 上次登陆日期

	protected Role role; // 根据角色查询用户条件

	protected List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	private String groupNames;
	private String officeNames;
	private String token;// 手机登录的时候产生的token

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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


	@JsonIgnore
	@NotNull(message = "归属公司不能为空")
	@ExcelField(title = "归属公司", align = 2, sort = 20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@JsonIgnore
	@NotNull(message = "归属部门不能为空")
	@ExcelField(title = "归属部门", align = 2, sort = 25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min = 1, max = 100, message = "登录名长度必须介于 1 和 100 之间")
	@ExcelField(title = "登录名", align = 2, sort = 30)
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
	@ExcelField(title = "工号", align = 2, sort = 45)
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
	@ExcelField(title = "电话", align = 2, sort = 60)
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

	@Length(min = 0, max = 100, message = "用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title = "用户类型", align = 2, sort = 80, dictType = "sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getOfficeNames() {
		return officeNames;
	}

	public void setOfficeNames(String officeNames) {
		this.officeNames = officeNames;
	}

}