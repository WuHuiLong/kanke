package com.kanke.pojo;

import java.util.Date;

public class Ticket {
    private Integer id;

    private Integer userId;

    private Integer orderno;

    private Integer password;

    private Date createTime;

    private Date updateTime;

    public Ticket(Integer id, Integer userId, Integer orderno, Integer password, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.orderno = orderno;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Ticket() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderno() {
        return orderno;
    }

    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}