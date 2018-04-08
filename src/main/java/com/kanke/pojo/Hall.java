package com.kanke.pojo;

import java.util.Date;

public class Hall {
    private Integer id;

    private Integer movieId;

    private String name;

    private Date time;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Hall(Integer id, Integer movieId, String name, Date time, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.movieId = movieId;
        this.name = name;
        this.time = time;
        this.status = status;
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

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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