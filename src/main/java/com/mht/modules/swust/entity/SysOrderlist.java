package com.mht.modules.swust.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.mht.common.persistence.DataEntity;
import com.mht.common.persistence.TreeEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.common.utils.excel.fieldtype.CarType;
import com.mht.common.utils.excel.fieldtype.OfficeType;
import com.mht.common.utils.excel.fieldtype.StatusType;
import com.mht.modules.sys.entity.Office;

/**
 * 
 * @ClassName: SysOrderlist
 * @Description: 预约列表
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午3:12:51 
 * @version 1.0.0
 */

public class SysOrderlist extends TreeEntity<SysOrderlist> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderId;

    private Office office;//院系id
    
    private Office officeExcel;//excel导入院系

    private String userId;//用户关联id
    
    private String company; //来访人单位

    private String orderName;//预约人

    private String orderPhone;//预约人手机号

    private String carNumber;//车辆号码
    
    private String qrCodeAddress;

    private String carType;//车辆类型

    private Date beginTime;//起始时间
    
    private String beginTimeExample;//起始时间导出

	private Date endTime;//结束时间
    
    private String endTimeExample;//结束时间
    
    private String sumPayMoney;

    private String accreditTime;//授权时限

    private String orderReason;//预约事由
    
    private String qrCodeImage;//预约事由qrCodeImg

    private String pass;//是否通过审核 (1.审核通过，2.审核未通过,3.车辆已入场)
    
    private String payFor;//是否收费(0.收费  1.免费 )
    
    private String payMoney;//收费金额
    
    private String state;//状态 (0.未审核  1.已审核 2.未收费 3.已收费)
    
    private List<String> carTypeList;
    
    private List<String> statusList;
    
	private String color;

	private String disable;
	
	
	@Override
	public String toString() {
		return "SysOrderlist [orderId=" + orderId + ", office=" + office + ", userId=" + userId + ", company=" + company
				+ ", orderName=" + orderName + ", orderPhone=" + orderPhone + ", carNumber=" + carNumber + ", carType="
				+ carType + ", beginTime=" + beginTime + ", endTime=" + endTime + ", accreditTime=" + accreditTime
				+ ", orderReason=" + orderReason + ", pass=" + pass + ", payFor=" + payFor + ", payMoney=" + payMoney
				+ ", state=" + state + "]";
	}

	@ExcelField(title = "总计收款", align = 2, sort = 93)
	public String getSumPayMoney() {
		if(sumPayMoney==null||"".equals(sumPayMoney)){
			return "";
		}
		return sumPayMoney;
	}


	public void setSumPayMoney(String sumPayMoney) {
		this.sumPayMoney = sumPayMoney;
	}


	@ExcelField(title = "预约剧本", align = 2, sort = 40,fieldType = OfficeType.class )
	public Office getOfficeExcel() {
		officeExcel = office;
		return officeExcel;
	}



	public void setOfficeExcel(Office officeExcel) {
		office = officeExcel;
	}



	public String getQrCodeImage() {
		return qrCodeImage;
	}



	public void setQrCodeImage(String qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}



	public String getQrCodeAddress() {
		return qrCodeAddress;
	}


	public void setQrCodeAddress(String qrCodeAddress) {
		this.qrCodeAddress = qrCodeAddress;
	}


	public String getDisable() {
		return disable;
	}


	public void setDisable(String disable) {
		this.disable = disable;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	@ExcelField(title = "玩家人数", align = 2, sort = 10)
	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}

	@ExcelField(title = "收款金额", align = 2, sort = 92)
	public String getPayMoney() {
		return payMoney;
	}



	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}



	public String getPayFor() {
		return payFor;
	}



	public void setPayFor(String payFor) {
		this.payFor = payFor;
	}



	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@ExcelField(title = "编号", align = 2, sort = 1)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
    
    @NotNull(message = "校内对接单位不能为空")
    public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    @ExcelField(title = "玩家姓名", align = 2, sort = 2)
    @NotBlank(message = "来访人不能为空")
    @Length(min = 2, max = 10, message = "来访人长度必须介于 2和 10之间汉字或字母")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName == null ? null : orderName.trim();
    }

    @ExcelField(title = "联系方式", align = 2, sort = 60)
    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone == null ? null : orderPhone.trim();
    }

    @ExcelField(title = "玩家来源", align = 2, sort = 70)
    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    @NotNull(message = "车辆类型不能为空")
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType == null ? null : carType.trim();
    }

    @JsonIgnore
    @ExcelField(title = "预约房间", align = 2, sort = 80 , fieldType = CarType.class)
    public List<String> getCarTypeList() {
    	List<String> carTypeList = Lists.newArrayList();
    	carTypeList.add(carType);
		return carTypeList;
	}


	public void setCarTypeList(List<String> carTypeList) {
		carType = carTypeList.get(0);
	}


    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    @ExcelField(title = "起始时间", align = 2, sort = 90)
	@NotNull(message = "起始时间不能为空")
    public String getBeginTimeExample() {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if(beginTime!=null){
    		String dateString = formatter.format(beginTime);
    		return dateString;
    	}
    	return " ";
	}


	public void setBeginTimeExample(String beginTimeExample) {
		this.beginTimeExample = beginTimeExample;
	}

	@ExcelField(title = "截止时间", align = 2, sort = 91)
    @NotNull(message = "截止时间不能为空")
	public String getEndTimeExample() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(endTime!=null){
			String dateString = formatter.format(endTime);
			return dateString;
		}
		return" ";
	}


	public void setEndTimeExample(String endTimeExample) {
		this.endTimeExample = endTimeExample;
	}

    public String getAccreditTime() {
        return accreditTime;
    }

    public void setAccreditTime(String accreditTime) {
        this.accreditTime = accreditTime;
    }

    @NotBlank(message = "预约事由不能为空")
    @Length(min = 4, max = 140, message = "预约事由必须介于 4和 20之间汉字、字母、数字")
    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason == null ? null : orderReason.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    @JsonIgnore
	public List<String> getStatusList() {
    	List<String> statusList = Lists.newArrayList();
    	statusList.add(state);
    	statusList.add(pass);
		return statusList;
	}


	public void synchronizationTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			beginTime = formatter.parse(beginTimeExample);
			endTime = formatter.parse(endTimeExample);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
	
	

	@Override
	public SysOrderlist getParent() {
		// TODO Auto-generated method stub
		return parent;
	}


	@Override
	public void setParent(SysOrderlist parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}
        
    
}