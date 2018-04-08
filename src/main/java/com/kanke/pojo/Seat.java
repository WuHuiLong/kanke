package com.kanke.pojo;

import java.util.Date;

public class Seat {
    private Integer id;

    private Integer hallId;

    private Integer column;

    private Integer row;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Seat(Integer id, Integer hallId, Integer column, Integer row, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.hallId = hallId;
        this.column = column;
        this.row = row;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Seat() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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