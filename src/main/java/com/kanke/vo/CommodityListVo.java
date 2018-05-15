package com.kanke.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommodityListVo {
    private Integer id;

    private String name;

    private String mainImage;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    //后面添加的
    private String imageHost;
}
