package com.kanke.pojo;

import java.util.Date;

public class Kind {
    private Integer id;

    private String stype;

    private Integer status;

    private Integer seatId;

    private Integer column;

    private Integer row;

    private Date createTime;

    private Date updateTime;

    public Kind(Integer id, String stype, Integer status, Integer seatId, Integer column, Integer row, Date createTime, Date updateTime) {
        this.id = id;
        this.stype = stype;
        this.status = status;
        this.seatId = seatId;
        this.column = column;
        this.row = row;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Kind() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype == null ? null : stype.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
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