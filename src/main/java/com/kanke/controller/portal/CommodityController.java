package com.kanke.controller.portal;


import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Commodity;
import com.kanke.service.ICommodityService;
import com.kanke.vo.CommodityDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/commodity/")
public class CommodityController {

    @Autowired
    private ICommodityService iCommodityService;

    /**
     * 前台商品详情
     * @param commodityId
     * @return
     */
    @RequestMapping(value="detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CommodityDetailVo> detail(Integer commodityId){
        return iCommodityService.detail(commodityId);
    }

    /**
     * 前台模糊搜索
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iCommodityService.searchList(keyword,pageNum,pageSize);
    }

    /**
     * 前端全查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> selectAll(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        return iCommodityService.AllSelect(pageNum, pageSize);
    }
}
