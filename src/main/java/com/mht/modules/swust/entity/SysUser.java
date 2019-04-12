package com.mht.modules.swust.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;
/**
 * 
 * @ClassName: SysUser
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午3:12:37 
 * @version 1.0.0
 */
public class SysUser extends User{

	private static final long serialVersionUID = 1L;

	private String id;

    private String companyId;

    private String officeId;//学院ID

    private String loginName;//登录名

    private String password;//密码

    private String no;//编号

    private String name;//真实姓名

    private String email;//邮箱

    private String phone;//固定电话

    private String mobile;//移动电话

    private String userType;//用户类别

    private String photo;//上传图片

    private String loginIp;//登录IP地址

    private Date loginDate;//登录时间

    private String loginFlag;

    private Date createDate;//组册时间
    
    private Date updateDate;	// 更新日期
	
    private String remarks;//备注

    private String delFlag;

    private String qrcode;

    private String sign;

    private String account;//账号

    private String idNo;//身份证

    private String gender;//性别

    private String nation;

    private String roleType;//权限级别

    private String origin;

    private String education;

    private String duty;

    private String otherDuty;

    private String outside;

    private String token;
    
    private SysUser createBy;	// 创建者
    
    private SysUser updateBy;	// 更新者

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", companyId=" + companyId + ", officeId=" + officeId + ", loginName=" + loginName
				+ ", password=" + password + ", no=" + no + ", name=" + name + ", email=" + email + ", phone=" + phone
				+ ", mobile=" + mobile + ", userType=" + userType + ", photo=" + photo + ", loginIp=" + loginIp
				+ ", loginDate=" + loginDate + ", loginFlag=" + loginFlag + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", remarks=" + remarks + ", delFlag=" + delFlag + ", qrcode=" + qrcode
				+ ", sign=" + sign + ", account=" + account + ", idNo=" + idNo + ", gender=" + gender + ", nation="
				+ nation + ", roleType=" + roleType + ", origin=" + origin + ", education=" + education + ", duty="
				+ duty + ", otherDuty=" + otherDuty + ", outside=" + outside + ", token=" + token + "updateDate="+updateDate+ "]";
	}

	
	
	public SysUser getCreateBy() {
		return createBy;
	}



	public void setCreateBy(SysUser createBy) {
		this.createBy = createBy;
	}



	public SysUser getUpdateBy() {
		return updateBy;
	}



	public void setUpdateBy(SysUser updateBy) {
		this.updateBy = updateBy;
	}



	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }
    @NotNull(message = "部门不能为空")
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId == null ? null : officeId.trim();
    }
    @Length(min = 6, max = 100, message = "登录名长度必须介于 6 和 100 之间")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }
    @Length(min = 6, max = 30, message = "密码长度必须介于 6 和 30 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }
    @Length(min = 2, max = 100, message = "姓名长度必须介于 2 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    @Email(message = "邮箱格式不正确")
	@Length(min = 0, max = 200, message = "邮箱长度必须介于 1 和 200 之间")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
    @Length(min = 11, max = 12, message = "电话长度必须介于 11 和 11")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag == null ? null : loginFlag.trim();
    }

    

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode == null ? null : qrcode.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType == null ? null : roleType.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getOtherDuty() {
        return otherDuty;
    }

    public void setOtherDuty(String otherDuty) {
        this.otherDuty = otherDuty == null ? null : otherDuty.trim();
    }

    public String getOutside() {
        return outside;
    }

    public void setOutside(String outside) {
        this.outside = outside == null ? null : outside.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }
}