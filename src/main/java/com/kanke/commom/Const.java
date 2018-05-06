package com.kanke.commom;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER="current_user";//当前用户

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    public static final Long   HALFTIME=30*60*1000L;//休息时间30分钟；

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
    public enum SeatStatusEnum{
        SELECTABLE(0,"可选"),
        REVERSIBILITY(1,"已选，可取消"),
        UN_SELECTABLE(2,"不可选");


        private String value;
        private int code;

        SeatStatusEnum(int code,String value){
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

    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        ORDER_SUCCESS(40,"订单完成"),
        ORDER_CLOSE(50,"订单关闭");

        private String value;
        private int code;

        OrderStatusEnum(int code,String value){
            this.code=code;
            this.value=value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum OrderStatusEnum1 : values()){
                if(OrderStatusEnum1.getCode() == code){
                    return OrderStatusEnum1;
                }
            }
            throw new RuntimeException("没有这个枚举");
        }
    }
    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";

        String RESPONSE_SECCESS="success";
        String RESPONSE_FAILED="failed";
    }

    public  enum payPlatformEnum{
        ALIPAY(1,"支付宝交易");

        private String value;
        private int code;

        payPlatformEnum(int code,String value){
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

    public  enum paymentTypeEnum{
        PAY_ONLINE(1,"在线支付");

        private String value;
        private int code;

        paymentTypeEnum(int code,String value){
            this.code=code;
            this.value=value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static paymentTypeEnum codeOf(int code){
            for(paymentTypeEnum paymentTypeEnum1 : values()){
                if(paymentTypeEnum1.getCode() == code){
                    return paymentTypeEnum1;
                }
            }
            throw new RuntimeException("没有这个枚举");
        }
    }
}
