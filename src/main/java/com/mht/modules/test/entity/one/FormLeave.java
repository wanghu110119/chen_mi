/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.entity.one;

import javax.validation.constraints.NotNull;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.modules.sys.entity.Area;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;

/**
 * 请假表单Entity
 * @author lgf
 * @version 2016-10-06
 */
public class FormLeave extends DataEntity<FormLeave> {
	
	private static final long serialVersionUID = 1L;
	private User tuser;		// 员工
	private Office office;		// 归属部门
	private Area area;		// 归属区域
	private Date beginDate;		// 请假开始日期
	private Date endDate;		// 请假结束日期
	
	public FormLeave() {
		super();
	}

	public FormLeave(String id){
		super(id);
	}

	@NotNull(message="员工不能为空")
	@ExcelField(title="员工", fieldType=User.class, value="tuser.name", align=2, sort=1)
	public User getTuser() {
		return tuser;
	}

	public void setTuser(User tuser) {
		this.tuser = tuser;
	}
	
	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", fieldType=Office.class, value="office.name", align=2, sort=2)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="归属区域", fieldType=Area.class, value="area.name", align=2, sort=3)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="请假开始日期", align=2, sort=4)
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="请假结束日期", align=2, sort=5)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}