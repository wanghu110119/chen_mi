
package com.mht.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.common.statistics.StatLineDataBean;
import com.mht.common.statistics.StatSeriesDataBean;
import com.mht.common.statistics.StatTimeDataBean;
import com.mht.common.utils.DateUtils;
import com.mht.modules.account.entity.Parents;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.sys.dao.UserStatDao;
import com.mht.modules.sys.entity.Dict;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.entity.UserStat;
import com.mht.modules.sys.utils.DictUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: UserStatService
 * @Description: 账户统计
 * @author com.mhout.sx
 * @date 2017年3月23日 下午1:49:59
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class UserStatService extends CrudService<UserStatDao, UserStat> {

    // 账号操作字典中type
    public static final String USER_OPERATION_TYPE = "user_operation_type";
    // 操作类型
    public static final String INSERT = "1";
    public static final String UPDATE = "2";
    public static final String UPDATE_PWD = "3";
    public static final String DELETE = "4";
    public static final String LOGIN = "5";
    public static final String LOGINOUT = "6";

    @Autowired
    private UserStatDao userStatDao;

    /**
     * @Title: save
     * @Description: 保存账号操作记录
     * @param user
     * @param typeValue
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void save(User user, String typeValue) {
        UserStat userStat = new UserStat();
        userStat.setUserOperate(user);
        userStat.setOperationType(DictUtils.getDict(typeValue, USER_OPERATION_TYPE));
        userStat.setOperationIp(UserUtils.getSession().getHost());
        // 装载创建人创建时间等信息
        userStat.preInsert();
        userStat.setRemarks(
                "账号：" + user.getLoginName() + ",名称：" + user.getName() + ",操作人：" + userStat.getCreateBy().getName());
        this.userStatDao.insert(userStat);
    }

    /**
     * @Title: statLine
     * @Description: 按时间统计各类操作，供折线图使用
     * @param statTimeDataBean
     * @return
     * @author com.mhout.sx
     */
    public StatLineDataBean statLine(StatTimeDataBean statTimeDataBean) {
        StatLineDataBean statLineDataBean = new StatLineDataBean(statTimeDataBean);
        List<Dict> dicts = DictUtils.getDictList(UserStatService.USER_OPERATION_TYPE);
        List<String> legend = new ArrayList<>();
        List<StatSeriesDataBean> series = new ArrayList<StatSeriesDataBean>();
        for (Dict dict : dicts) {
            legend.add(dict.getLabel());
            // 统计该类别数据
            List<String[]> times = statLineDataBean.getTimes();
            StatSeriesDataBean statSeriesDataBean = new StatSeriesDataBean();
            statSeriesDataBean.setName(dict.getLabel());
            List<Object> statSeriesData = new ArrayList<Object>();
            for (String[] time : times) {
                long count = this.userStatDao.getCountByTimeAndType(time[0], time[1], dict.getId());
                statSeriesData.add(count);
            }
            statSeriesDataBean.setData(statSeriesData);
            series.add(statSeriesDataBean);
        }
        statLineDataBean.setLegend(legend);
        statLineDataBean.setSeries(series);
        return statLineDataBean;
    }

    /**
     * @Title: findUserStat
     * @Description: TODO 分页查询
     * @param page
     * @param userStat
     * @return
     * @author com.mhout.sx
     */
    public Page<UserStat> findUserStat(Page<UserStat> page, UserStat userStat) {
        if (userStat.getBeginDate() == null) {
            userStat.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
        }
        if (userStat.getEndDate() == null) {
            userStat.setEndDate(DateUtils.addMonths(userStat.getBeginDate(), 1));
        }
        return super.findPage(page, userStat);
    }

    /**
     * @Title: save
     * @Description: TODO 记录学生账号操作记录
     * @param student
     * @param typeValue
     * @author com.mhout.sx
     */
    public void save(Student student, String typeValue) {
        // TODO Auto-generated method stub
        UserStat userStat = new UserStat();
        userStat.setUserOperate(student);
        userStat.setOperationType(DictUtils.getDict(typeValue, USER_OPERATION_TYPE));
        userStat.setOperationIp(UserUtils.getSession().getHost());
        // 装载创建人创建时间等信息
        userStat.preInsert();
        userStat.setRemarks("账号：" + student.getLoginName() + ",名称：" + student.getName() + ",操作人："
                + userStat.getCreateBy().getName());
        this.userStatDao.insert(userStat);
    }

    /**
     * @Title: save
     * @Description: TODO 记录教工账号操作记录
     * @param teacher
     * @param typeValue
     * @author com.mhout.sx
     */
    public void save(Teacher teacher, String typeValue) {
        // TODO Auto-generated method stub
        UserStat userStat = new UserStat();
        userStat.setUserOperate(teacher);
        userStat.setOperationType(DictUtils.getDict(typeValue, USER_OPERATION_TYPE));
        userStat.setOperationIp(UserUtils.getSession().getHost());
        // 装载创建人创建时间等信息
        userStat.preInsert();
        userStat.setRemarks("账号：" + teacher.getLoginName() + ",名称：" + teacher.getName() + ",操作人："
                + userStat.getCreateBy().getName());
        this.userStatDao.insert(userStat);
    }

    /**
     * @Title: save
     * @Description: TODO 记录家长账号操作记录
     * @param parents
     * @param typeValue
     * @author com.mhout.sx
     */
    public void save(Parents parents, String typeValue) {
        // TODO Auto-generated method stub
        UserStat userStat = new UserStat();
        userStat.setUserOperate(parents);
        userStat.setOperationType(DictUtils.getDict(typeValue, USER_OPERATION_TYPE));
        userStat.setOperationIp(UserUtils.getSession().getHost());
        // 装载创建人创建时间等信息
        userStat.preInsert();
        userStat.setRemarks("账号：" + parents.getLoginName() + ",名称：" + parents.getName() + ",操作人："
                + userStat.getCreateBy().getName());
        this.userStatDao.insert(userStat);
    }
}
