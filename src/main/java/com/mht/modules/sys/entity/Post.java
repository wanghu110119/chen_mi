
package com.mht.modules.sys.entity;

import com.mht.common.persistence.TreeEntity;

/**
 * @ClassName: Post
 * @Description: 岗位实体
 * @author com.mhout.sx
 * @date 2017年4月5日 下午12:45:20
 * @version 1.0.0
 */
public class Post extends TreeEntity<Post> {

    private static final long serialVersionUID = 1L;
    private String code; // 岗位编码

    private String otherId;// 表示其他相关连id

    public Post() {

    }

    public Post(String id) {
        super(id);
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

}