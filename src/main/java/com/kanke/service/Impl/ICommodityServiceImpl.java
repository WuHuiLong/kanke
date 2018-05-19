package com.kanke.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.CommodityMapper;
import com.kanke.pojo.Commodity;
import com.kanke.service.ICommodityService;
import com.kanke.util.DateTimeUtil;
import com.kanke.util.PropertiesUtil;
import com.kanke.vo.CommodityDetailVo;
import com.kanke.vo.CommodityListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iCommodityService")
public class ICommodityServiceImpl implements ICommodityService{
    @Autowired
    private CommodityMapper commodityMapper;

    public ServerResponse saveOrUpdateMovie(Commodity commodity){
        if(commodity!=null){
            if(StringUtils.isNotBlank(commodity.getSubImages())){
                String[] subImages=commodity.getSubImages().split(",");
                if(subImages.length>0){
                    commodity.setMainImage(subImages[0]);
                }
            }
            if(commodity.getId()!=null){
                int rowCount= commodityMapper.updateByPrimaryKey(commodity);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMsg("更新商品成功");
                }
                return ServerResponse.createByErrorMsg("更新商品失败");
            }else{
                int rowCount =commodityMapper.insert(commodity);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMsg("添加电影成功");
                }
                return ServerResponse.createByErrorMsg("添加电影失败");
            }
        }
        return ServerResponse.createByErrorMsg("参数错误，请稍后再试");
    }

    public ServerResponse<String> setSaleStatus(Integer commodityId,Integer status){
        if(commodityId==null||status==null){
            return ServerResponse.createByErrorMsg("参数错误，请稍后再试");
        }
        Commodity commodity=new Commodity();
        commodity.setId(commodityId);
        commodity.setStatus(status);
        int rowCount=commodityMapper.updateByPrimaryKeySelective(commodity);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("修改电影状态成功");
        }
        return  ServerResponse.createByErrorMsg("修改电影状态失败");
    }

    public ServerResponse<CommodityDetailVo> manageCommodityDetail(Integer commodityId){
        if(commodityId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Commodity commodity=commodityMapper.selectByPrimaryKey(commodityId);
        if(commodity==null){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        //VO对象->value Object
        //pojo->bo(business Object)->vo(view object)
        CommodityDetailVo commodityDetailVo=assembleCommodityDetailVo(commodity);
        return ServerResponse.createBySuccess(commodityDetailVo);
    }
    private CommodityDetailVo assembleCommodityDetailVo(Commodity commodity){
        CommodityDetailVo commodityDetailVo =new CommodityDetailVo();
        commodityDetailVo.setId(commodity.getId());
        commodityDetailVo.setDetail(commodity.getDetail());
        commodityDetailVo.setMainImage(commodity.getMainImage());
        commodityDetailVo.setSubImages(commodity.getSubImages());
        commodityDetailVo.setName(commodity.getName());
        commodityDetailVo.setPrice(commodity.getPrice());
        commodityDetailVo.setStatus(commodity.getStatus());
        commodityDetailVo.setStock(commodity.getStock());
        commodityDetailVo.setCreateTime(DateTimeUtil.DateTostr(commodity.getCreateTime()));
        commodityDetailVo.setUpdateTime(DateTimeUtil.DateTostr(commodity.getUpdateTime()));
        commodityDetailVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://image.imooc.com/"));
        return commodityDetailVo;
    }

    public ServerResponse<PageInfo> getCommodityList(int pageNum, int pageSize){
        //startpage-start
        //填充自己的sql逻辑
        //pageHelper收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Commodity> CommodityList= commodityMapper.selectListManage();
        List<CommodityListVo> commodityListVoList= Lists.newArrayList();
        for(Commodity movieItem : CommodityList){
            CommodityListVo movieListVo=assembleCommodityListVo(movieItem);
            commodityListVoList.add(movieListVo);
        }
        PageInfo pageInfo=new PageInfo(CommodityList);
        pageInfo.setList(commodityListVoList);
        return  ServerResponse.createBySuccess(pageInfo);
    }

    private CommodityListVo assembleCommodityListVo(Commodity commodity){
        CommodityListVo commodityListVo =new CommodityListVo();
        commodityListVo.setId(commodity.getId());
        commodityListVo.setDetail(commodity.getDetail());
        commodityListVo.setMainImage(commodity.getSubImages());
        commodityListVo.setName(commodity.getName());
        commodityListVo.setPrice(commodity.getPrice());
        commodityListVo.setStatus(commodity.getStatus());
        commodityListVo.setStock(commodity.getStock());
        commodityListVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://image.imooc.com/"));
        return commodityListVo;
    }

    public ServerResponse<PageInfo> searchCommodity(String commodityName,Integer commodityId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(commodityName)){
            commodityName=new StringBuilder().append("%").append(commodityName).append("%").toString();
        }
        List<Commodity> commodity =commodityMapper.searchCommodityByNameAndId(commodityName,commodityId);
        List<CommodityListVo> commodityListVoList =Lists.newArrayList();
        for(Commodity commodityItem : commodity){
            CommodityListVo commodityListVo=assembleCommodityListVo(commodityItem);
            commodityListVoList.add(commodityListVo);
        }
        PageInfo pageInfo =new PageInfo(commodity);
        pageInfo.setList(commodityListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //前台
    public ServerResponse<CommodityDetailVo> detail(Integer commodityId){
        if(commodityId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Commodity commodity=commodityMapper.selectByPrimaryKey(commodityId);
        if(commodity==null){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        if(commodity.getStatus()!= Const.CommodityStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        CommodityDetailVo commodityDetailVo=assembleCommodityDetailVo(commodity);
        return ServerResponse.createBySuccess(commodityDetailVo);
    }

    public ServerResponse<PageInfo> searchList(String keyword,int pageNum,int pageSize){
        if(StringUtils.isBlank(keyword)){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Commodity> commodities=commodityMapper.searchCommodityByKeyword(keyword);
        List<CommodityListVo> commodityListVoList =Lists.newArrayList();
        for (Commodity commodity : commodities){
            CommodityListVo commodityListVo=assembleCommodityListVo(commodity);
            commodityListVoList.add(commodityListVo);
        }
        PageInfo pageInfo =new PageInfo(commodities);
        pageInfo.setList(commodityListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse<PageInfo> AllSelect(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Commodity> commodities =commodityMapper.selectAll();
        List<CommodityDetailVo> commodityDetailVoList =Lists.newArrayList();
        for (Commodity commodity : commodities){
            CommodityDetailVo commodityDeatilVo=assembleCommodityDetailVo(commodity);
            commodityDetailVoList.add(commodityDeatilVo);
        }
        PageInfo pageInfo = new PageInfo(commodities);
        pageInfo.setList(commodityDetailVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
