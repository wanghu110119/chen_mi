package com.mht.modules.swust.entity;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;

public class SysCarMoney extends DataEntity<SysCarMoney>{
		
    private String carTypeId;

    private String timeTimes;

    private String addMoney;

    private String defaultTime;

    private String maxMoney;

    private String remarks;

    private String moneyMonth;

    

    @Override
	public String toString() {
		return "SysCarMoney [carTypeId=" + carTypeId + ", timeTimes=" + timeTimes + ", addMoney=" + addMoney
				+ ", defaultTime=" + defaultTime + ", maxMoney=" + maxMoney + ", remarks=" + remarks + ", moneyMonth="
				+ moneyMonth + "]";
	}

	public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId == null ? null : carTypeId.trim();
    }

    public String getTimeTimes() {
        return timeTimes;
    }

    public void setTimeTimes(String timeTimes) {
        this.timeTimes = timeTimes == null ? null : timeTimes.trim();
    }

    public String getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(String addMoney) {
        this.addMoney = addMoney == null ? null : addMoney.trim();
    }

    public String getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime == null ? null : defaultTime.trim();
    }

    public String getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney == null ? null : maxMoney.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getMoneyMonth() {
        return moneyMonth;
    }

    public void setMoneyMonth(String moneyMonth) {
        this.moneyMonth = moneyMonth == null ? null : moneyMonth.trim();
    }
}