package com.ets.security;

import com.ets.settings.domain.User;
import java.util.Date;

/**
 *
 * @author Yusuf
 */
public class Login {
    
    private User user;
    private Date loginTime;
    private Date lastCommunication;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLastCommunication() {
        return lastCommunication;
    }

    public void setLastCommunication(Date lastCommunication) {
        this.lastCommunication = lastCommunication;
    }        
}
