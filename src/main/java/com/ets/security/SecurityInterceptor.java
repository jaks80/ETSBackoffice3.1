package com.ets.security;

import com.ets.settings.domain.User;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

/**
 * This interceptor verify the access permissions for a user based on username
 * and passowrd provided in request
 *
 */
@Provider
@ServerInterceptor
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    //private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());//Login based
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());//Role based
    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());

    @Override
    public void filter(ContainerRequestContext requestContext) {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        //Access allowed for all 
        if (!method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all 
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            final String[] tokenizer = authorization.get(0).split("crsplitter");

            final String username = tokenizer[0];
            final String enc_password = tokenizer[1];

            final String password = Cryptography.decryptString(enc_password);

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(ACCESS_FORBIDDEN);
                    //return;
                }
                if (LoginManager.loginExpired(username, password)) {
                    requestContext.abortWith(ACCESS_DENIED);
                }
            }
        }
    }

    public static User getUser(String tokenizer) {
        final String[] vals = tokenizer.split("crsplitter");

        final String loginId = vals[0];
        final String enc_password = vals[1];
        final String password = Cryptography.decryptString(enc_password);
        return LoginManager.getUser(loginId, password);
    }

    private boolean isUserAllowed(final String loginId, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;
        User valid_user = LoginManager.validateLogin(loginId, password);
        String userRole = valid_user.getUserType().toString();
        String allowedRole = rolesSet.iterator().next();

        /**
         * If user is allowed for his role and all the roles bellow him. For
         * example SM has id 1 and GS has id 0. So SM is allowed for SM and GS
         * roles.
         */
        if (LoginManager.valideRole(userRole, allowedRole)) {
            isAllowed = true;
        }
        return isAllowed;
    }

}
