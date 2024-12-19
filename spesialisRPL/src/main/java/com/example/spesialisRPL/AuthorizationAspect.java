package com.example.spesialisRPL;

import java.util.Arrays;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;

    @Before("@annotation(requiredRole)")
    public void checkAuthorization(RequiredRole requiredRole) throws Exception {
        if(requiredRole == null){
            throw new SecurityException("RequiredRole annotation is missing");
        }
        
        String[] roles = requiredRole.value();
        String role = (String) session.getAttribute("role");

        if (Arrays.asList(roles).contains("*")) {
            return;
        }

        if (session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return;
        }
        
        if (!Arrays.asList(roles).contains(role)) {
            if(role.equals("pasien")){
                response.sendRedirect("/user/");
                return;
            }
            if(role.equals("dokter") || role.equals("perawat")){
                response.sendRedirect("/listPasien");
                return;
            }
            if(role.equals("admin")){
                System.out.println("Lu admin lu punya kuasa");
                response.sendRedirect("/admin/");
                return;
            }
            throw new SecurityException("Authorized only for : " + Arrays.toString(roles));
        }
    }
}
