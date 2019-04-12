
package com.mht.modules.account.dao;

import org.apache.ibatis.annotations.Update;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.account.entity.Student;

/**
 * @ClassName: StudentDao
 * @Description: 学生账号dao
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:29:33
 * @version 1.0.0
 */
@MyBatisDao
public interface StudentDao extends CrudDao<Student> {
    /**
     * @Title: updatePasswordById
     * @Description: TODO 修改账号密码
     * @param id
     * @param password
     * @author com.mhout.sx
     */
    public void updatePasswordById(String id, String password);

    /**
     * @Title: updatePhotoByNo 通过学号、工号修改头像
     * @Description: TODO
     * @param id
     * @param url
     * @author com.mhout.sx
     */
    @Update("update sys_user set photo=#{1} where no=#{0}")
    public void updatePhotoByNo(String id, String url);

    /**
     * @Title: updatePhotoByIdNo
     * @Description: TODO 通过证件号修改头像
     * @param id
     * @param url
     * @author com.mhout.sx
     */
    @Update("update sys_user set photo=#{1} where id_no=#{0}")
    public void updatePhotoByIdNo(String id, String url);
}
