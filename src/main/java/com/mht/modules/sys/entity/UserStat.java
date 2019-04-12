/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.sys.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: UserStat
 * @Description: 账号统计
 * @author com.mhout.sx
 * @date 2017年3月23日 上午11:47:26
 * @version 1.0.0
 */
public class UserStat extends DataEntity<UserStat> {

    private static final long serialVersionUID = 1L;
    private String operationIp; // 操作IP
    private Dict operationType;// 操作类型1新增、2修改信息、3修改密码、4删除、5登录、6登出等
    private DataEntity userOperate;// 被操作的账号
    private OperateUser user;

    private Date beginDate; // 开始日期
    private Date endDate; // 结束日期

    public UserStat() {
        super();
    }

    public UserStat(String id) {
        super(id);
    }

    public String getOperationIp() {
        return operationIp;
    }

    public void setOperationIp(String operationIp) {
        this.operationIp = operationIp;
    }

    public Dict getOperationType() {
        return operationType;
    }

    public void setOperationType(Dict operationType) {
        this.operationType = operationType;
    }

    public DataEntity getUserOperate() {
		return userOperate;
	}

	public void setUserOperate(DataEntity userOperate) {
		this.userOperate = userOperate;
	}
	
	public OperateUser getUser() {
		return user;
	}

	public void setUser(OperateUser user) {
		this.user = user;
	}

	public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}