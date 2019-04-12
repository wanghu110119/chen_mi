package com.mht.modules.ident.entity;

import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;

/**
 * 
 * @ClassName: IdentityGroup
 * @Description:
 * @author com.mhout.zjh
 * @date 2017年3月23日 下午1:56:03
 * @version 1.0.0
 */
public class IdentityGroup extends DataEntity<IdentityGroup> {

    private static final long serialVersionUID = 1L;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 描述
     */
    private String description;

    private String otherId;

    @Length(min = 0, max = 64, message = "群组名长度必须介于 0 和 64 之间")
    @ExcelField(title = "群组名", align = 2, sort = 1)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Length(min = 0, max = 256, message = "描述长度必须介于 0 和 256 之间")
    @ExcelField(title = "群头像", align = 2, sort = 2)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }
}