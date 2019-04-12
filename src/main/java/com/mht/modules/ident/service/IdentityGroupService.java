package com.mht.modules.ident.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.common.utils.IdGen;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.IdentityGroupRecord;
import com.mht.modules.ident.entity.IdentityGroupUser;
import com.mht.modules.ident.entity.StatictisData;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;

/**
 * 
 * @ClassName: IdentityGroupService
 * @Description:
 * @author com.mhout.zjh
 * @date 2017年3月23日 下午1:52:06
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class IdentityGroupService extends CrudService<IdentityGroupDao, IdentityGroup> {

    @Autowired
    private IdentityGroupDao identityGroupDao;

    @Autowired
    private OfficeDao officeDao;

    public IdentityGroup get(String id) {
        IdentityGroup identityGroup = super.get(id);
        return identityGroup;
    }

    public List<IdentityGroup> findList(IdentityGroup identityGroup) {
        List<IdentityGroup> list = identityGroupDao.findList(identityGroup);
        return list;
    }

    /**
     * 
     * @Title: findByName
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    public IdentityGroup findByName(String groupName) {
        return this.identityGroupDao.findByName(groupName);
    }

    /**
     * 
     * @Title: findByName
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    @Transactional(readOnly = false)
    public int deleteById(String id) {
        IdentityGroup identityGroup = this.identityGroupDao.get(id);
        if (identityGroup != null) {
            return this.identityGroupDao.deleteByLogic(identityGroup);
        }
        return 0;
    }

    /**
     * 
     * @Title: findUserByIdentityGroup
     * @Description: TODO
     * @param identityGroup
     * @return
     * @author com.mhout.zjh
     */
    public List<User> findUserByIdentityGroup(IdentityGroup identityGroup) {
        List<User> list = this.identityGroupDao.findUserByIdentityGroup(identityGroup.getId());
        for (User u : list) {
            List<Office> offices = officeDao.findOfficeByUser(u.getId());
            List<String> officeNames = offices.stream().map(Office::getName).collect(Collectors.toList());
            Office office = new Office();
            office.setName(String.join(",", officeNames));
            u.setOffice(office);
        }
        return list;
    }

    /**
     * 
     * @Title: assignUserToIdentityGroup
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    @Transactional(readOnly = false)
    public int assignUserToIdentityGroup(IdentityGroup identityGroup, User user) {

        List<IdentityGroupUser> list = identityGroupDao.findByIdentityGroupIdAndUserId(identityGroup, user);
        if (list.size() > 0) {
            return 0;
        }
        this.identityGroupDao.insertIdentityGroupRecord(
                new IdentityGroupRecord(identityGroup, user, IdentityGroupRecord.ACTION_JOIN_GROUP));
        int rows = this.identityGroupDao.insertIdentityGroupUser(IdGen.uuid(), identityGroup.getId(), user.getId());
        return rows;
    }

    /**
     * 
     * @Title: outIdentityGroup
     * @Description: TODO
     * @param identityGroup
     * @param user
     * @return
     * @author com.mhout.zjh
     */
    @Transactional(readOnly = false)
    public int outIdentityGroup(IdentityGroup identityGroup, User user) {
        this.identityGroupDao.insertIdentityGroupRecord(
                new IdentityGroupRecord(identityGroup, user, IdentityGroupRecord.ACTION_OUT_GROUP));
        int rows = this.identityGroupDao.deleteIdentityGroupUser(identityGroup.getId(), user.getId());
        return rows;
    }

    /**
     * 
     * @Title: findRecordList
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    public List<StatictisData> findRecordList(String dateType, String action, String group) {
        List<StatictisData> list = null;
        switch (dateType) {
        // 本周
        case "week":
            list = this.completeData(
                    this.identityGroupDao.findWeekList(Calendar.getInstance().getTime(), action, group), dateType);
            break;
        // 本月
        case "month":
            list = this.completeData(
                    this.identityGroupDao.findMonthList(Calendar.getInstance().getTime(), action, group), dateType);
            break;
        // 本年
        case "year":
            list = this.completeData(
                    this.identityGroupDao.findYearList(Calendar.getInstance().getTime(), action, group), dateType);
            break;
        }
        return list;
    }

    /**
     * 
     * @Title: completeData
     * @Description: 补全数据
     * @param list
     * @param type
     * @return
     * @author com.mhout.zjh
     */
    private List<StatictisData> completeData(List<StatictisData> list, String dateType) {
        List<StatictisData> resultList = new ArrayList<>();
        switch (dateType) {
        // 本周
        case "week":
            resultList = completeDataByDate(list, 7);
            break;
        // 本月
        case "month":
            resultList = completeDataByDate(list, getCurrentMonthDay());
            break;
        // 本年
        case "year":
            resultList = completeDataByDate(list, 12);
            break;
        }
        return resultList;
    }

    /**
     * 
     * @Title: completeDataByDate
     * @Description: TODO
     * @param list
     * @param num
     * @return
     * @author com.mhout.zjh
     */
    private List<StatictisData> completeDataByDate(List<StatictisData> list, int num) {
        List<StatictisData> resultList = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            boolean flag = false;
            for (int j = 0; j < list.size(); j++) {
                StatictisData statictisData = list.get(j);
                if (Integer.valueOf(statictisData.getTime()) == i) {
                    resultList.add(statictisData);
                    flag = true;
                }
            }
            if (flag) {
                continue;
            }
            StatictisData sta = new StatictisData();
            sta.setTime(i + "");
            sta.setValue("0");
            resultList.add(sta);
        }
        return resultList;
    }

    /**
     * 获取当月的 天数
     */
    private static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public List<User> getTeacherByOffice(String officeid) {
        // TODO Auto-generated method stub
        List<User> list = this.officeDao.findUserByOfficeOnly(officeid, Teacher.ROLE_TEACHER);
        return list;
    }

    public List<User> getStudentByOffice(String officeid) {
        List<User> list = this.officeDao.findUserByOfficeOnly(officeid, Student.ROLE_STUDENT);
        return list;
    }
}