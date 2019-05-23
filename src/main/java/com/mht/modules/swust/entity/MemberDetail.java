package com.mht.modules.swust.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;
import com.mht.modules.sys.entity.User;

public class MemberDetail extends DataEntity<MemberDetail> {
	//1:充值费用	0：消费费用
    private String type;

    private String costMoney;

    private String addMoney;
    
    private int costTime;

    private int addTime;
    
    private Integer giftMoney;
    
    private Integer totalMoney;
    
    private String wechat;
    
    private String costTotalMoney;
    
    private String addTotalMoney;

    private String cardId;
    
    private SysCar car;
    
    private User user;
    
    private Date beginTime;
    
    private Date endTime;
    
    private boolean year;
    
    private String createByName;

    public MemberDetail(){
    	
    }
    public int getCostTime() {
		return costTime;
	}
	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}
	public int getAddTime() {
		return addTime;
	}
	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	@ExcelField(title = "赠送金额", align = 2, sort = 18)
    public Integer getGiftMoney() {
		return giftMoney;
	}
	public void setGiftMoney(Integer giftMoney) {
		this.giftMoney = giftMoney;
	}
	public Integer getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	@ExcelField(title = "消费总计", align = 2, sort = 110)
    public String getCostTotalMoney() {
		return costTotalMoney;
	}

	public void setCostTotalMoney(String costTotalMoney) {
		this.costTotalMoney = costTotalMoney;
	}
	@ExcelField(title = "充值总计", align = 2, sort = 112)
	public String getAddTotalMoney() {
		return addTotalMoney;
	}
	
	public void setAddTotalMoney(String addTotalMoney) {
		this.addTotalMoney = addTotalMoney;
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
		if("1".equals(type)){
			return "充值";
		}else if("0".equals(type)){
			return "消费";
		}else{
			return null;
		}
        
    }
	
	@ExcelField(title = "金额", align = 2, sort = 4)
	public String getCostOrAddMoney() {
        return (costMoney!=null&&!"".equals(costMoney))?costMoney:addMoney;
    }
	@ExcelField(title = "次数", align = 2, sort = 5)
	public int getCostOrAddTime() {
        return (costTime!=0)?costTime:addTime;
    }
	@ExcelField(title = "时间", align = 2, sort = 15)
	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
    	if(createDate!=null){
    		String dateString = formatter.format(createDate);
    		return dateString;
    	}
        return null;
    }
	
	@ExcelField(title = "操作", align = 2, sort = 16)
	public String getCreateByName () {
		if(createByName!=null &&!"".equals(createByName)){
			return createByName;
		}else if(createBy!=null &&!"".equals(createBy.getLoginName())){
			return createBy.getLoginName();
		}
		
		return "";
        
    }
	
	public void setCreateByName ( ) {
		if(createBy!=null &&createBy.getLoginName()!=null){
			createByName = createBy.getLoginName();
		}
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