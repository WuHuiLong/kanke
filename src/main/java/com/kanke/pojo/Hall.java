package com.kanke.pojo;

import java.util.Date;

public class Hall {
    private Integer id;

    private String name;

    private Integer status;

    private Integer number;

    private String stype;

    private Date createTime;

    private Date updateTime;

    public Hall(Integer id, String name, Integer status, Integer number, String stype, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.number = number;
        this.stype = stype;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Hall() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype == null ? null : stype.trim();
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