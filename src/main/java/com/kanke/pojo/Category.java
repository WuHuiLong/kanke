package com.kanke.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {
    private Integer id;

    private String name;

    private Integer parentId;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}