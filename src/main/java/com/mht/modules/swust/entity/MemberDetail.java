package com.mht.modules.swust.entity;

import java.text.SimpleDateFormat;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.modules.sys.entity.User;

public class MemberDetail extends DataEntity<MemberDetail> {
	//1:充值费用	0：消费费用
    private String type;

    private String costMoney;

    private String addMoney;

    private String cardId;
    
    private SysCar car;
    
    private User user;
    
    private boolean year;

    public boolean isYear() {
		return year;
	}

	public void setYear(boolean year) {
		this.year = year;
	}

	public SysCar getCar() {
		return car;
	}

	public void setCar(SysCar car) {
		this.car = car;
	}
	@ExcelField(title = "会员卡号", align = 2, sort = 1)
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@ExcelField(title = "充值/消费", align = 2, sort = 3)
	public String getCostOrAdd () {
        return "1".equals(type)?"充值":"消费";
    }
	
	@ExcelField(title = "金额", align = 2, sort = 4)
	public String getCostOrAddMoney() {
        return (costMoney!=null&&!"".equals(costMoney))?costMoney:addMoney;
    }
	@ExcelField(title = "时间", align = 2, sort = 5)
	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
    	if(createDate!=null){
    		String dateString = formatter.format(createDate);
    		return dateString;
    	}
        return null;
    }
	
	public String getType() {
        return type;
    }
	
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney == null ? null : costMoney.trim();
    }

    public String getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(String addMoney) {
        this.addMoney = addMoney == null ? null : addMoney.trim();
    }

}