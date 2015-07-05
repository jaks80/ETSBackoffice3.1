package com.ets.settings.dao;

import com.ets.GenericDAOImpl;
import com.ets.settings.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("userDAO")
@Transactional
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO{
    
}
