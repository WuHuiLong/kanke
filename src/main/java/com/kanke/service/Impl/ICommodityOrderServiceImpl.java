package com.kanke.service.Impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kanke.commom.Const;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.*;
import com.kanke.pojo.*;
import com.kanke.service.ICommodityOrderService;
import com.kanke.util.BigDecimalUtil;
import com.kanke.util.DateTimeUtil;
import com.kanke.util.PropertiesUtil;
import com.kanke.vo.CommodityOrderItemVo;
import com.kanke.vo.CommodityOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service("iCommodityOrderService")
@Slf4j
public class ICommodityOrderServiceImpl implements ICommodityOrderService{
    @Autowired
    private Commodity_orderMapper commodity_orderMapper;

    @Autowired
    private Commodity_order_itemMapper commodity_order_itemMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    private static AlipayTradeService tradeService;
    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    public ServerResponse creat(Integer userId){
        List<Cart> cartList =cartMapper.selectCart(userId);

        ServerResponse serverResponse =this.getOrderItem(cartList,userId);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<Commodity_order_item> commodityOrderItems =(List<Commodity_order_item>)serverResponse.getData();
        BigDecimal payment =this.getTolPrice(commodityOrderItems);

        //生成订单
        Commodity_order commodityOrder =this.assembleOrder(payment,userId);
        if(commodityOrder==null){
            return ServerResponse.createByErrorMsg("生成订单错误");
        }
        if(CollectionUtils.isEmpty(commodityOrderItems)){
            return ServerResponse.createByErrorMsg("购物车为空");
        }

        for(Commodity_order_item commodityOrderItem : commodityOrderItems){
            commodityOrderItem.setOrderNo(commodityOrder.getOrderNo());
        }
        //批量插入
        commodity_order_itemMapper.batchInsert(commodityOrderItems);

        //减少库存
        this.reduceCommodityStock(commodityOrderItems);

        //清空购物车
        for(Cart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }

        CommodityOrderVo commodityOrderVo =this.assembleOrderVo(commodityOrder,commodityOrderItems);
        return ServerResponse.createBySuccess(commodityOrderVo);

    }




    private CommodityOrderVo assembleOrderVo(Commodity_order order, List<Commodity_order_item> commodityOrderItemList){
        CommodityOrderVo orderVo = new CommodityOrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.paymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());


        orderVo.setPaymentTime(DateTimeUtil.DateTostr(order.getPaymentTime()));
        orderVo.setEndTime(DateTimeUtil.DateTostr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.DateTostr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.DateTostr(order.getCloseTime()));


        orderVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix"));


        List<CommodityOrderItemVo> orderItemVoList = Lists.newArrayList();

        for(Commodity_order_item orderItem : commodityOrderItemList){
            CommodityOrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setCommodityOrderItemList(orderItemVoList);
        return orderVo;
    }

    private CommodityOrderItemVo assembleOrderItemVo(Commodity_order_item commodity_order_item){
        CommodityOrderItemVo commodityOrderItemVo =new CommodityOrderItemVo();
        commodityOrderItemVo.setCommodityId(commodity_order_item.getCommodityId());
        commodityOrderItemVo.setCommodityImage(commodity_order_item.getCommodityImage());
        commodityOrderItemVo.setCommodityName(commodity_order_item.getCommodityName());
        commodityOrderItemVo.setCurrentUnitPrice(commodity_order_item.getCurrentUnitPrice());
        commodityOrderItemVo.setOrderNo(commodity_order_item.getOrderNo());
        commodityOrderItemVo.setQuantity(commodity_order_item.getQuantity());
        commodityOrderItemVo.setTotalPrice(commodity_order_item.getTotalPrice());
        commodityOrderItemVo.setCreateTime(DateTimeUtil.DateTostr(commodity_order_item.getCreateTime()));
        commodityOrderItemVo.setUpdateTime(DateTimeUtil.DateTostr(commodity_order_item.getUpdateTime()));

        return commodityOrderItemVo;
    }
    private void reduceCommodityStock(List<Commodity_order_item> commodityOrderItems){
        for(Commodity_order_item commodityOrderItem : commodityOrderItems){
            Commodity commodity = commodityMapper.selectByPrimaryKey(commodityOrderItem.getCommodityId());
            commodity.setStock(commodity.getStock()-commodityOrderItem.getQuantity());
            commodityMapper.updateByPrimaryKeySelective(commodity);
        }
    }

    private Commodity_order assembleOrder(BigDecimal payment,Integer userId){
        Commodity_order commodityOrder =new Commodity_order();
        commodityOrder.setUserId(userId);
        Long orderNo =this.generateOrderNo();
        commodityOrder.setOrderNo(orderNo);
        commodityOrder.setPayment(payment);
        commodityOrder.setPaymentType(Const.paymentTypeEnum.PAY_ONLINE.getCode());
        commodityOrder.setStatus(Const.CommodityOrderStatusEnum.NO_PAY.getCode());
        int rowCount =commodity_orderMapper.insert(commodityOrder);
        if(rowCount>0){
            return commodityOrder;
        }
        return null;
    }

    //随机生成订单号
    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }
    //把多种商品价格加起来算出总价格
    private BigDecimal getTolPrice(List<Commodity_order_item> orderItems){
        BigDecimal payment = new BigDecimal("0");
        for(Commodity_order_item commodityOrderItem : orderItems){
            payment =BigDecimalUtil.add(payment.doubleValue(),commodityOrderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    //通过购物车信息获取除订单号外其他所有信息
    private ServerResponse getOrderItem(List<Cart> cartList,Integer userId){
        List<Commodity_order_item> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMsg("购物车为空");
        }
        for(Cart cart : cartList){
            Commodity_order_item commodity_order_item =new Commodity_order_item();
            Commodity commodity =commodityMapper.selectByPrimaryKey(cart.getCommodityId());
            if(commodity.getStatus()!=Const.CommodityStatusEnum.ON_SALE.getCode()){
                return ServerResponse.createByErrorMsg("产品"+commodity.getName()+"不是在线售卖状态");
            }
            if(commodity.getStock()<cart.getQuantity()){
                return ServerResponse.createByErrorMsg("产品"+commodity.getName()+"库存不足");
            }
            commodity_order_item.setCommodityId(commodity.getId());
            commodity_order_item.setUserId(userId);
            commodity_order_item.setQuantity(cart.getQuantity());
            commodity_order_item.setCommodityName(commodity.getName());
            commodity_order_item.setCommodityImage(commodity.getMainImage());
            commodity_order_item.setCurrentUnitPrice(commodity.getPrice());
            commodity_order_item.setTotalPrice(BigDecimalUtil.mul(cart.getQuantity(),commodity.getPrice().doubleValue()));
            orderItemList.add(commodity_order_item);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }



    public ServerResponse<String> cancle(Integer userId,Long orderNo){
        Commodity_order commodityOrder =commodity_orderMapper.selectByUserIdAndOrderNo(userId,orderNo);

        if(commodityOrder==null){
            return ServerResponse.createByErrorMsg("用户无该订单");
        }
        if(commodityOrder.getStatus() !=Const.CommodityOrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMsg("已付款，无法取消订单");
        }
        Commodity_order commodityOrderItem =new Commodity_order();
        commodityOrderItem.setId(commodityOrder.getId());
        commodityOrderItem.setStatus(Const.CommodityOrderStatusEnum.CANCELED.getCode());
        int row = commodity_orderMapper.updateByPrimaryKeySelective(commodityOrderItem);
        if(row>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    public ServerResponse<CommodityOrderVo> getCommodityOrderDetail(Integer userId,Long orderNo){
        Commodity_order commodityOrder =commodity_orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(commodityOrder !=null){
            List<Commodity_order_item> commodityOrderItems =commodity_order_itemMapper.getByOrderNoUserId(userId,orderNo);
            CommodityOrderVo commodityOrderVo =this.assembleOrderVo(commodityOrder,commodityOrderItems);
            return ServerResponse.createBySuccess(commodityOrderVo);
        }
        return ServerResponse.createByError();
    }

    public ServerResponse<PageInfo> getCommodityOrderList(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Commodity_order> commodityOrderList = commodity_orderMapper.selectByUserId(userId);
        List<CommodityOrderVo> commodityOrderVoList=getList(userId,commodityOrderList);
        PageInfo pageInfo =new PageInfo(commodityOrderList);
        pageInfo.setList(commodityOrderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private List<CommodityOrderVo> getList(Integer userId,List<Commodity_order>commodityOrderList){
        List<CommodityOrderVo> commodityOrderVoList =Lists.newArrayList();
        for(Commodity_order commodityOrder : commodityOrderList){
            List<Commodity_order_item> commodityOrderItems =Lists.newArrayList();
            if(userId == null){
                commodityOrderItems =commodity_order_itemMapper.getByOrderNo(commodityOrder.getOrderNo());
            }
            else{
                commodityOrderItems =commodity_order_itemMapper.getByOrderNoUserId(userId,commodityOrder.getOrderNo());
            }
            CommodityOrderVo commodityOrderVo =assembleOrderVo(commodityOrder,commodityOrderItems);
            commodityOrderVoList.add(commodityOrderVo);
        }
        return  commodityOrderVoList;
    }


    //后台
    public ServerResponse<PageInfo> getCommodityOrderListManage( int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Commodity_order> commodityOrderList = commodity_orderMapper.selectList();
        List<CommodityOrderVo> commodityOrderVoList=getList(null,commodityOrderList);
        PageInfo pageInfo =new PageInfo(commodityOrderList);
        pageInfo.setList(commodityOrderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<CommodityOrderVo> getCommodityOrderDetailManage(Long orderNo){
        Commodity_order commodityOrder =commodity_orderMapper.selectByOrderNo(orderNo);
        if(commodityOrder !=null){
            List<Commodity_order_item> commodityOrderItems =commodity_order_itemMapper.getByOrderNo(orderNo);
            CommodityOrderVo commodityOrderVo =this.assembleOrderVo(commodityOrder,commodityOrderItems);
            return ServerResponse.createBySuccess(commodityOrderVo);
        }
        return ServerResponse.createByError();
    }


    public ServerResponse<PageInfo> searchOrder(Long orderNo,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        Commodity_order order =commodity_orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("订单不存在");
        }
        List<Commodity_order_item> orderItemList = commodity_order_itemMapper.getByOrderNo(orderNo);
        CommodityOrderVo orderVo = assembleOrderVo(order,orderItemList);

        PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
        pageInfo.setList(Lists.newArrayList(orderVo));
        return ServerResponse.createBySuccess(pageInfo);
    }















    public ServerResponse pay(Long orderNo, Integer userId , String path){

        Map<String,String> resultMap = Maps.newHashMap();
        Commodity_order order=commodity_orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));





        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "看客影城当面付扫码消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        List<Commodity_order_item> orderItemList = commodity_order_itemMapper.getByOrderNoUserId(userId,orderNo);
        for(Commodity_order_item orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getCommodityId().toString(), orderItem.getCommodityName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
//                .setNotifyUrl("http://www.test-notify-url.com")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String filePath = String.format(path+"/qr-%s.png", response.getOutTradeNo());

                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                File file=new File(filePath);
                String url=file.getName();
                log.info("filePath:" + filePath);
                resultMap.put("url",url);
                return ServerResponse.createBySuccess(resultMap);


            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMsg("系统异常，预下单状态未知!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMsg("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMsg("不支持的交易状态，交易返回异常!!!");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    public  ServerResponse alipayCallback(Map<String,String> params){
        //获取订单号
        Long orderNo= Long.parseLong(params.get("out_trade_no"));
        //获取交易号
        String tradeNo=params.get("trade_no");
        //获取交易状态
        String tradeStatus=params.get("trade_Status");

        Commodity_order order=commodity_orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("订单错误，请检查后再试");
        }
        if(order.getStatus()>= Const.CommodityOrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if(Const.AlipayCallback.RESPONSE_SECCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDate( params.get("gmt_payment")));
            order.setStatus(Const.CommodityOrderStatusEnum.PAID.getCode());
            commodity_orderMapper.updateByPrimaryKey(order);
        }
        PayInfo payInfo=new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setCommodityorderNo(orderNo);
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setPayPlatform(Const.payPlatformEnum.ALIPAY.getCode());

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    public  ServerResponse queryOrderPayStatus(Long orderNo,Integer userId){
        Commodity_order order=commodity_orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("用户没有该订单");
        }
        if(order.getStatus()>Const.CommodityOrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
