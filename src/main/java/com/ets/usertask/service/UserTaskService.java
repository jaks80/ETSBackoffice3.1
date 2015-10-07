package com.ets.usertask.service;

import com.ets.usertask.dao.UserTaskDAO;
import com.ets.usertask.domain.UserTask;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf Akhond
 */
@Service("userTaskService")
public class UserTaskService {
    
    @Resource(name = "userTaskDAO")
    private UserTaskDAO dao;
    
    public UserTask createNewTask(UserTask userTask){
    
     dao.save(userTask);
     return userTask;
    }
    
    
}
