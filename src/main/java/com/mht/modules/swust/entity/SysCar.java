package com.mht.modules.swust.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.common.utils.excel.fieldtype.CarType;
import com.mht.common.utils.excel.fieldtype.MemberType;
import com.mht.modules.sys.entity.User;

public class SysCar extends  DataEntity<SysCar>{
    private String id;

    private String carId;

    private String userName;

    private String phone;
    
    private int giftMoney = 0;
    
    private int totalMoney;
    
    private String wechat;

    private String carType;
    
    private List<String> carTypeList;
    
    private int costMoney;
    
    private Integer effectiveTime;
    
    private String effectiveTimeList;

    private Integer money = 0;
    
    private String moneyList;

    private Date beginTime;

    private Date endTime;
    
    private String beginTimeExample;
    
    private String endTimeExample;

    private String disable;//1为可用，2为不可用

    private MemberDetail memberDetail;
    

    @Override
	public String toString() {
		return "SysCar [id=" + id + ", carId=" + carId + ", userName=" + userName + ", phone=" + phone + ", carType="
				+ carType + ", effectiveTime=" + effectiveTime + ", money=" + money + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", updateBy=" + updateBy + ", createBy=" + createBy  + ", disable=" + disable + "]";
	}


	public int getTotalMoney() {
		return totalMoney;
	}


	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}


	public int getGiftMoney() {
		return giftMoney;
	}


	public void setGiftMoney(int giftMoney) {
		this.giftMoney = giftMoney;
	}


	public String getWechat() {
		return wechat;
	}


	public void setWechat(String wechat) {
		this.wechat = wechat;
	}


	public MemberDetail getMemberDetail() {
		return memberDetail;
	}

	public void setMemberDetail(MemberDetail memberDetail) {
		this.memberDetail = memberDetail;
	}

	public int getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(int costMoney) {
		this.costMoney = costMoney;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    @ExcelField(title = "会员卡号", align = 2, sort = 20 )
    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId == null ? null : carId.trim();
    }
    @ExcelField(title = "会员姓名", align = 2, sort = 30)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
    @ExcelField(title = "联系方式", align = 2, sort = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
    
    public String getCarType() {
        return carType;
    }
    
    @JsonIgnore
    @ExcelField(title = "会员等级", align = 2, sort = 40 , fieldType = MemberType.class)
    public List<String> getCarTypeList() {
    	List<String> carTypeList = Lists.newArrayList();
    	carTypeList.add(carType);
		return carTypeList;
	}
    public void setCarTypeList(List<String> carTypeList) {
    	carType = carTypeList.get(0);
	}


    public void setCarType(String carType) {
        this.carType = carType == null ? null : carType.trim();
    }
    @ExcelField(title = "剩余次数", align = 2, sort = 60 )
    public String getEffectiveTimeList() {
    	if(String.valueOf(effectiveTime)==null||"".equals(String.valueOf(effectiveTime))){
    		effectiveTimeList = "0";
    	}else{
    	effectiveTimeList=String.valueOf(effectiveTime);
    	}
		return effectiveTimeList;
	}
    
    
    public void setEffectiveTimeList(String effectiveTimeList) {
    	effectiveTimeList = effectiveTimeList.replaceAll(" ", "");
		if(effectiveTimeList!=null&&!"".equals(effectiveTimeList)){
			this.effectiveTimeList = effectiveTimeList;
			effectiveTime = Integer.valueOf(effectiveTimeList);
		}else{
			effectiveTime=0;
		}
	}

	public Integer getEffectiveTime() {
		if(effectiveTime==null){
			return 0;
		}
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
    @ExcelField(title = "剩余金额（元）", align = 2, sort = 70 )
    public String getMoneyList() {
    	moneyList = String.valueOf(money);
		return moneyList;
	}
    
    public void setMoneyList(String moneyList) {
    	moneyList = moneyList.replaceAll(" ", "");
		if(moneyList!=null&&!"".equals(moneyList)){
			this.moneyList = moneyList;
			money = Integer.valueOf(moneyList);
		}else{
			money=0;
		}
	}

	public Integer getMoney() {
        return money; 
    }

    public void setMoney(Integer money) {
    	if(money==null){
    		money=0;
    	}
        this.money = money;
    }
//    @ExcelField(title = "起始时间", align = 2, sort = 80)
    public Date getBeginTime() {
    	if(beginTime==null){
    		return new Date();
    	}else if(beginTime.getTime()<new Date().getTime()){
    		beginTime = new Date();
    	}
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
//    @ExcelField(title = "截止时间", align = 2, sort = 90)
    public Date getEndTime() {
    	if(getBeginTime()==null){
    		setBeginTime(new Date());
    	}
    	if(effectiveTime==null){
    		effectiveTime=0;
    	}
        return new Date(getBeginTime().getTime()+(long)getEffectiveTime()*24*60*60*1000);
    }

    public void setEndTime(Date endTime) {
    	if(endTime==null){
    		 this.endTime = new Date(getBeginTime().getTime()+((long)getEffectiveTime())*24*60*60*1000);
    		 return;
    	}
        this.endTime = endTime;
    }

    @ExcelField(title = "起始时间", align = 2, sort = 80)
 	@NotNull(message = "起始时间不能为空")
     public String getBeginTimeExample() {
     	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     	if(beginTime!=null){
     		String dateString = formatter.format(beginTime);
     		return dateString;
     	}
     	return "2018-01-01 00:00:00";
 	}


 	public void setBeginTimeExample(String beginTimeExample) {
 		this.beginTimeExample = beginTimeExample;
 	}

 	@ExcelField(title = "截止时间", align = 2, sort = 90)
     @NotNull(message = "截止时间不能为空")
 	public String getEndTimeExample() {
 		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		if(endTime!=null){
 			String dateString = formatter.format(endTime);
 			return dateString;
 		}
 		return"2018-01-01 00:00:00";
 	}


 	public void setEndTimeExample(String endTimeExample) {
 		this.endTimeExample = endTimeExample;
 	}

	public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable == null ? null : disable.trim();
    }
    
    @Length(min=0, max=255)
    @ExcelField(title = "编号", align = 2, sort = 5)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

}