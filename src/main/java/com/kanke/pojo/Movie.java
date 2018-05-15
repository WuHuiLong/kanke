package com.kanke.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}