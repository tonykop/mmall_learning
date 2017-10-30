package com.mmall.common;

/**
 * Created in 2017/10/25
 * @author tony
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;//common user
        int ROLE_ADMIN = 1;//admin
    }
}