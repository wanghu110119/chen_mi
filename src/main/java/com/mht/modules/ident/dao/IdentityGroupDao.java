package com.mht.modules.ident.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.IdentityGroupRecord;
import com.mht.modules.ident.entity.IdentityGroupUser;
import com.mht.modules.ident.entity.StatictisData;
import com.mht.modules.sys.entity.User;

/**
 * 
 * @ClassName: IdentityGroupDao
 * @Description:
 * @author com.mhout.zjh
 * @date 2017年3月23日 下午1:55:43
 * @version 1.0.0
 */
@MyBatisDao
public interface IdentityGroupDao extends CrudDao<IdentityGroup> {
    /**
     * 
     * @Title: findByName
     * @Description: 根据组名查询
     * @param groupName
     * @return
     * @author com.mhout.zjh
     */
    IdentityGroup findByName(String groupName);

    /**
     * 
     * @Title: findUserByIdentityGroup
     * @Description:
     * @param id
     * @return
     * @author com.mhout.zjh
     */
    List<User> findUserByIdentityGroup(String id);

    /**
     * 
     * @Title: insertIdentityGroupUser
     * @Description: TODO
     * @param userId
     * @param identityGroupId
     * @return
     * @author com.mhout.zjh
     */
    int insertIdentityGroupUser(@Param("id") String id, @Param("identityGroupId") String identityGroupId,
            @Param("userId") String userId);

    /**
     * 
     * @Title: outIndentityGroup
     * @Description: TODO
     * @param identityGroupId
     * @param userId
     * @return
     * @author com.mhout.zjh
     */
    int deleteIdentityGroupUser(@Param("identityGroupId") String identityGroupId, @Param("userId") String userId);

    /**
     * 
     * @Title: insertIdentityGroupRecord
     * @Description: TODO
     * @param identityGroupRecord
     * @return
     * @author com.mhout.zjh
     */
    int insertIdentityGroupRecord(IdentityGroupRecord identityGroupRecord);

    /**
     * 
     * @Title: findWeekList
     * @Description: TODO
     * @param map
     * @return
     * @author com.mhout.zjh
     */
    List<StatictisData> findWeekList(@Param("currentDate") Date currentDate, @Param("action") String action,
            @Param("group") String group);

    /**
     * 
     * @Title: findMonthList
     * @Description: TODO
     * @param map
     * @return
     * @author com.mhout.zjh
     */
    List<StatictisData> findMonthList(@Param("currentDate") Date currentDate, @Param("action") String action,
            @Param("group") String group);

    /**
     * 
     * @Title: findYearList
     * @Description: TODO
     * @param map
     * @return
     * @author com.mhout.zjh
     */
    List<StatictisData> findYearList(@Param("currentDate") Date currentDate, @Param("action") String action,
            @Param("group") String group);

    /**
     * 
     * @Title: findByIdAndUserId
     * @Description: TODO
     * @param identityGroup
     * @param user
     * @return
     * @author com.mhout.zjh
     */
    List<IdentityGroupUser> findByIdentityGroupIdAndUserId(@Param("identityGroup") IdentityGroup identityGroup,
            @Param("user") User user);

    /**
     * @Title: getAllGroupForUser
     * @Description: TODO 查询所有用户组
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,group_name groupName from t_identity_group where del_flag='0'")
    List<IdentityGroup> getAllGroupForUser();

    /**
     * @Title: getAllGroupForUser2
     * @Description: TODO 查询所有用户组,并选中用户已拥有的
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @Select("select g.id,g.group_name groupName,gu.user_id otherId  from "
            + "t_identity_group g left join t_identity_group_user gu on (gu.user_id=#{0} and g.id=gu.identity_group_id) where g.del_flag='0'")
    List<IdentityGroup> getAllGroupForUser2(String id);

    /**
     * @Title: findIdentityGroupByUser
     * @Description: TODO 获取用户已拥有的用户组
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select g.id,g.group_name groupName from t_identity_group g,t_identity_group_user gu where gu.user_id=#{0} and g.id=gu.identity_group_id and g.del_flag='0'")
    List<IdentityGroup> findIdentityGroupByUser(String userId);

}