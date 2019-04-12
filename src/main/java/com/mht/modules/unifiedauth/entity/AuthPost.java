/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.entity.Post;

/**
 * 
 * @ClassName: AuthPost
 * @Description: 岗位授权实体
 * @author com.mhout.zjh
 * @date 2017年4月6日 上午9:03:33 
 * @version 1.0.0
 */
public class AuthPost extends DataEntity<AuthPost> {

    private static final long serialVersionUID = 1L;
    private Post post;
    private Application apply;
    private String accessAuth;

    private String postId;
    private String applyId;
    private String isEdit; //是否编辑1.是 2.不是
    private String closeType; //是否屏蔽 1.是 2.不是
    

    public String getAccessAuth() {
        return accessAuth;
    }

    public void setAccessAuth(String accessAuth) {
        this.accessAuth = accessAuth;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Application getApply() {
        return apply;
    }

    public void setApply(Application apply) {
        this.apply = apply;
    }

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getCloseType() {
		return closeType;
	}

	public void setCloseType(String closeType) {
		this.closeType = closeType;
	}
	
}