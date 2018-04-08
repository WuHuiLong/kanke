package com.kanke.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Movie {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String detail;

    private Integer status;

    private String mainImage;

    private String subImages;

    private BigDecimal price;

    private String movieAddress;

    private String language;

    private String director;

    private Integer length;

    private String starring;

    private Date createTime;

    private Date updateTime;

    public Movie(Integer id, Integer categoryId, String name, String detail, Integer status, String mainImage, String subImages, BigDecimal price, String movieAddress, String language, String director, Integer length, String starring, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.detail = detail;
        this.status = status;
        this.mainImage = mainImage;
        this.subImages = subImages;
        this.price = price;
        this.movieAddress = movieAddress;
        this.language = language;
        this.director = director;
        this.length = length;
        this.starring = starring;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Movie() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages == null ? null : subImages.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMovieAddress() {
        return movieAddress;
    }

    public void setMovieAddress(String movieAddress) {
        this.movieAddress = movieAddress == null ? null : movieAddress.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring == null ? null : starring.trim();
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