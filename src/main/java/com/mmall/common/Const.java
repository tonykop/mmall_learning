package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface Cart{
        int CHECKED =1;//XUANZHONGZHUANGTAI
        int UN_CHECKED =0;//weixuanzhongzhuangtai

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }
    public enum ProductStatusEnum{
        ON_SALE(1,"ONLINE");

        private String value;
        private  int code;
        ProductStatusEnum(int code, String value){
          this.code = code;
          this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}