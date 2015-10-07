package com.ets.usertask.dao;

import com.ets.GenericDAOImpl;
import com.ets.usertask.domain.UserTask;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf Akhond
 */
@Service("userTaskDAO")
@Transactional
public class UserTaskDAOImpl extends GenericDAOImpl<UserTask, Long> implements UserTaskDAO {

    @Override
    public List<UserTask> findTaskByUser(Long userId, Date remindDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
