package com.ets.security;

import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.util.Map;
import javax.validation.constraints.AssertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Yusuf
 */
public class LoginManagerTest {

    public LoginManagerTest() {
    }

    @Test
    public void addLoginTest() {
        
        LoginManager.resetCache();
       User user = new User();       
       user.setLoginID("ViHyrZi9xvhh3NNaPNf1rw==partition[61, -86, -95, -97, 48, -10, 20, -71, -100, 108, 101, -44, 55, 59, -49, -124]");
        user.setPassword("p2b34iqYsJXLZmAQx3bGmg==partition[103, 28, -59, -67, -31, 73, 29, -86, -77, 12, -92, 4, -95, -35, 109, 49]");
       user.setUserType(Enums.UserType.GS);
       LoginManager.addLogin(user);
       
       Map<String, Login> loginList = LoginManager.getLoginList();
       assertNotNull(loginList);
       assertEquals(1, loginList.keySet().size());
       
       user = new User();       
       user.setLoginID("ViHyrZi9xvhh3NNaPNf1rw==partition[61, -86, -95, -97, 48, -10, 20, -71, -100, 108, 101, -44, 55, 59, -49, -124]");
        user.setPassword("p2b34iqYsJXLZmAQx3bGmg==partition[103, 28, -59, -67, -31, 73, 29, -86, -77, 12, -92, 4, -95, -35, 109, 49]");
       user.setUserType(Enums.UserType.GS);
       LoginManager.addLogin(user);
       
       assertNotNull(loginList);
       assertEquals(1, loginList.keySet().size());
       
       user = new User();       
       user.setLoginID("IrZr0/Cm5smlhB3Bu80Avw==partition[48, 89, -86, 57, 54, -70, -36, 57, -91, 41, -62, -28, 92, 68, -35, 57]");
        user.setPassword("NXA5GYX+Vac/bJVtFEwJjw==partition[119, -19, -44, -54, -125, 4, 32, 105, 118, -102, -31, -79, 103, -128, -122, 94]");
       user.setUserType(Enums.UserType.GS);
       LoginManager.addLogin(user);
       
       assertNotNull(loginList);
       assertEquals(2, loginList.keySet().size());
    }

    @Test
    public void validateLoginTest() {
        LoginManager.resetCache();
        User user = new User();

        user.setLoginID("ViHyrZi9xvhh3NNaPNf1rw==partition[61, -86, -95, -97, 48, -10, 20, -71, -100, 108, 101, -44, 55, 59, -49, -124]");
        user.setPassword("p2b34iqYsJXLZmAQx3bGmg==partition[103, 28, -59, -67, -31, 73, 29, -86, -77, 12, -92, 4, -95, -35, 109, 49]");

        user.setUserType(Enums.UserType.GS);
        LoginManager.addLogin(user);

        Map<String, Login> loginList = LoginManager.getLoginList();
        assertNotNull(loginList);
        assertEquals(1, loginList.keySet().size());

        user = new User();
        //maks80 maks215
        user.setLoginID("maks80");
        user.setPassword("NXA5GYX+Vac/bJVtFEwJjw==partition[119, -19, -44, -54, -125, 4, 32, 105, 118, -102, -31, -79, 103, -128, -122, 94]");
        user.setUserType(Enums.UserType.GS);
        LoginManager.addLogin(user);

        assertNotNull(loginList);
        assertEquals(2, loginList.keySet().size());

        User isvalid = LoginManager.validateLogin("maks80", "maks215");
        assertNotNull(isvalid);
    }

    @Test
    public void valideRoleTest() {
        LoginManager.resetCache();
        User user = new User();
        user.setLoginID("ViHyrZi9xvhh3NNaPNf1rw==partition[61, -86, -95, -97, 48, -10, 20, -71, -100, 108, 101, -44, 55, 59, -49, -124]");
        user.setPassword("p2b34iqYsJXLZmAQx3bGmg==partition[103, 28, -59, -67, -31, 73, 29, -86, -77, 12, -92, 4, -95, -35, 109, 49]");

        user.setUserType(Enums.UserType.SM);
        LoginManager.addLogin(user);

        User user1 = new User();
        
        //user1.setLoginID("maks80");
        //user1.setPassword("maks215");
        user1.setLoginID("IrZr0/Cm5smlhB3Bu80Avw==partition[48, 89, -86, 57, 54, -70, -36, 57, -91, 41, -62, -28, 92, 68, -35, 57]");
        user1.setPassword("NXA5GYX+Vac/bJVtFEwJjw==partition[119, -19, -44, -54, -125, 4, 32, 105, 118, -102, -31, -79, 103, -128, -122, 94]");
        user1.setUserType(Enums.UserType.GS);
        LoginManager.addLogin(user1);

        boolean result = LoginManager.valideRole(user1.getUserType().toString(), "GS");
        assertEquals(true, result);

        result = LoginManager.valideRole(user1.getUserType().toString(), "AD");
        assertEquals(false, result);

        result = LoginManager.valideRole(user1.getUserType().toString(), "SU");
        assertEquals(false, result);

    }
}
