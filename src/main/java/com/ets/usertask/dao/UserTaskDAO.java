package com.ets.usertask.dao;

import com.ets.GenericDAO;
import com.ets.usertask.domain.UserTask;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Yusuf Akhond
 */
public interface UserTaskDAO extends GenericDAO<UserTask, Long>{
    
    public List<UserTask> findTaskByUser(Long userId, Date remindDate);        
}
