package com.kanke.vo;

import com.kanke.pojo.Kind;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HallVo {
    private Integer id;

    private String name;

    private Integer status;

    private Integer number;

    private String stype;

    private String  createTime;

    private String updateTime;

//    private Integer row;
//
//    private Integer column;

    private List<Kind> kindList;
}
