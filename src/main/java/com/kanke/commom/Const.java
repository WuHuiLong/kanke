package com.kanke.commom;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER="current_user";//当前用户

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    public interface MovieListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price_desc","price_asc");
    }
    public interface Role{
        int ROLE_CUSTOMER=0; //普通用户
        int ROLE_ADMIN=1;//管理员
    }

    public enum MovieStatusEnum{
        ON_SALE(1,"在线");

        private String value;
        private int code;

        MovieStatusEnum(int code,String value){
            this.code=code;
            this.value=value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum HallStatusEnum{
        IDLE(1,"空闲");

        private String value;
        private int code;

        HallStatusEnum(int code,String value){
            this.code=code;
            this.value=value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
