package com.ets.settings.service;

import com.ets.security.Cryptography;
import com.ets.security.LoginManager;
import com.ets.settings.dao.UserDAO;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("userService")
public class UserService {

    @Resource(name = "userDAO")
    private UserDAO dao;

    public List<User> findAllOperational() {

        List<User> users = dao.findAll(User.class);
        List<User> operational_users = new ArrayList<>();

        for (User u : users) {
            if (!u.getUserType().equals(Enums.UserType.SU)) {
                operational_users.add(u);
            }
        }

        return operational_users;
    }

    public User login(String loginId, String enc_password, String enc_newPassword) {
        
        String password = Cryptography.decryptString(enc_password);
        String newPassword = null;

        if (enc_newPassword != null) {
            newPassword = Cryptography.decryptString(enc_newPassword);
        }

        List<User> dbUsers = dao.findAll(User.class);
        User authenticatedUser = null;

        for (User user : dbUsers) {
            String loginIdDB = user.getLoginID();
            String passwordDB = Cryptography.decryptString(user.getPassword());
            
            if (loginIdDB.equals(loginId) && passwordDB.equals(password) && user.isActive()) {
                authenticatedUser = user;
                break;
            }
        }

        LoginManager.addLogin(authenticatedUser);

        if (authenticatedUser != null) {
            authenticatedUser.setLoginID(loginId);
            authenticatedUser.setPassword(enc_password);
        }

        if (authenticatedUser != null && newPassword != null) {
            authenticatedUser.setPassword(enc_newPassword);
            dao.save(authenticatedUser);
        }

        return authenticatedUser;
    }

    public void logout(User user) {
        LoginManager.removeLogin(user.getLoginID());
    }

    public User saveorUpdate(User appSettings) {
        dao.save(appSettings);
        return appSettings;
    }

    public void delete(User user) {
        dao.delete(user);
    }
}
