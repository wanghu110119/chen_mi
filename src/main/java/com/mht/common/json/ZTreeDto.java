package com.mht.common.json;

import java.util.List;

/**
 * ztree组件转换对象
 * 
 * @author 苏鑫
 * @param <T>
 *
 */
public class ZTreeDto {
    public static final String USER_ICON = "/static/jquery-ztree/3.5.12/css/zTreeStyle/img/man.png";
    private String id;

    private String name;

    private String fullName;

    private boolean checked = false;

    private boolean open = true;

    private boolean chkDisabled = true;

    private boolean nocheck = true;

    private boolean isParent = true;

    private String icon;

    private String iconSkin;

    private Object obj;

    private List<ZTreeDto> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List<ZTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ZTreeDto> children) {
        this.children = children;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
