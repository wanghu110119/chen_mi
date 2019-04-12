
package com.mht.modules.account.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.account.entity.Parents;
import com.mht.modules.account.entity.Student;

/**
 * @ClassName: ParentsDao
 * @Description: 家长dao
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:05:01
 * @version 1.0.0
 */
@MyBatisDao
public interface ParentsDao extends CrudDao<Parents> {

    /**
     * @Title: deleteStudentParents
     * @Description: TODO 删除家长与学生对应关系
     * @param parentsId
     * @author com.mhout.sx
     */
    @Delete("delete from sys_parents_student where parents_id=#{0}")
    public void deleteStudentParents(String parentsId);

    /**
     * @Title: insertStudentParents
     * @Description: TODO 保存家长与学生对应关系
     * @param parentsId
     * @param studentId
     * @author com.mhout.sx
     */
    @Insert("insert into sys_parents_student(parents_id,student_id) VALUES(#{0},#{1})")
    void insertStudentParents(String parentsId, String studentId);

    /**
     * @Title: findStudentByParents
     * @Description: TODO 通过家长查询学生
     * @param parentsId
     * @return
     * @author com.mhout.sx
     */
    @Select("select u.id,u.name,u.id_no from sys_user u,sys_parents_student ps where ps.parents_id=#{0} and ps.student_id=u.id")
    public List<Student> findStudentByParents(String parentsId);

    /**
     * @Title: findStudentByName
     * @Description: TODO 通过证件号查询学生
     * @param idNo
     * @return
     * @author com.mhout.sx
     */
    @Select("select id from sys_user where id_no=#{0} and role_type='student' and del_flag = '0'")
    public Student findStudentByName(String idNo);

}
