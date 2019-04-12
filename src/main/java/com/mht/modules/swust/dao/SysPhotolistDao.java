package com.mht.modules.swust.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.SmsSendMobile;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.entity.SysPhotolistExample;
import com.mht.modules.sys.entity.User;
@MyBatisDao
public interface SysPhotolistDao extends CrudDao<SysPhotolist>{
    long countByExample(SysPhotolistExample example);

    int deleteByExample(SysPhotolistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysPhotolist record);

    int insertSelective(SysPhotolist record);

    List<SysPhotolist> selectByExample(SysPhotolistExample example);

    SysPhotolist selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(SysPhotolist record);

    int updateByPrimaryKey(SysPhotolist record);

	List<SysPhotolist> selectAll(@Param("name")String name, @Param("id")String id);

	SysPhotolist selectByterm(SysPhotolist record);

	void changepicture(String id);

	void changepictureToNo(String id);

	SysPhotolist selectByCount(SysPhotolist photo);

	List<SmsSendMobile> selectAdminPhone(String carType);
}