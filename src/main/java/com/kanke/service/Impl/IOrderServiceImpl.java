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
import com.google.common.collect.Maps;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.*;
import com.kanke.pojo.*;
import com.kanke.service.IOrderService;
import com.kanke.util.BigDecimalUtil;
import com.kanke.util.DateTimeUtil;
import com.kanke.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service("iOrderService")
public class IOrderServiceImpl implements IOrderService {


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

    private static final Logger logger = LoggerFactory.getLogger(IOrderServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private SeatMapper seatMapper;

    public ServerResponse creat(Integer scheduleId,Integer seatId,Integer userId){
        if(scheduleId==null&&userId==null&&seatId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Schedule schedule=scheduleMapper.selectByPrimaryKey(scheduleId);
        if(schedule==null){
            return ServerResponse.createByErrorMsg("未排片，请换一个再试");
        }
        Hall hall=hallMapper.selectByPrimaryKey(schedule.getHallId());
        if(hall==null||hall.getStatus()!=Const.HallStatusEnum.IDLE.getCode()){
            return ServerResponse.createByErrorMsg("此放映厅没空，去他家把");
        }
        Seat seat=seatMapper.selectByPrimaryKey(seatId);
        if(seat==null){
            return ServerResponse.createByErrorMsg("出错了呢，换个座位试试把");
        }
        if(seat.getStatus()==Const.SeatStatusEnum.UN_SELECTABLE.getCode()){
            return ServerResponse.createByError();
        }
        if(seat.getStatus()==Const.SeatStatusEnum.SELECTABLE.getCode()){
            //todo 选择座位 ，撤销座位等对座位的操作
            return  null;
        }

        //计算总价格


        return null;
    }
    private ServerResponse<Integer> getTotalPrice(Integer userId){
        if(userId==null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(orderMapper.selectTotalPrice(userId));
    }

    private Order assembleOrder(Integer userId, Integer scheduleId, BigDecimal payment,Integer quantity){
        Order order=new Order();
        long orderNo=this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setScheduleId(scheduleId);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPayment(payment);
        order.setPaymentType(Const.paymentTypeEnum.PAY_ONLINE.getCode());
        order.setQuantity(quantity);
        int rowCount =orderMapper.insert(order);
        if(rowCount>0){
            return order;
        }
        return null;
    }
    //通过当前时间和一个100以内的随机数生成一个orderNO
    private long generateOrderNo(){
        long currentTime=System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }


    public ServerResponse pay(Long orderNo,Integer userId ,String path){
        Map<String,String> resultMap = Maps.newHashMap();
        Order order=orderMapper.selectByUserIdAndOrderId(userId,orderNo);
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
        Schedule schedule=scheduleMapper.selectByPrimaryKey(order.getScheduleId());
        if(schedule==null){
            return ServerResponse.createByErrorMsg("电影未排片");
        }
        Movie movie=movieMapper.selectByPrimaryKey(schedule.getMovieId());
        if(movie==null){
            return ServerResponse.createByErrorMsg("电影未上映");
        }
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo,userId);
        for(OrderItem orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(movie.getId().toString(), movie.getName(),
                    BigDecimalUtil.mul(movie.getPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    order.getQuantity());
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
                logger.info("支付宝预下单成功: )");

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
                logger.info("filePath:" + filePath);
                resultMap.put("url",url);
                return ServerResponse.createBySuccess(resultMap);


            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMsg("系统异常，预下单状态未知!!!");

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMsg("系统异常，预下单状态未知!!!");

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMsg("不支持的交易状态，交易返回异常!!!");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    public  ServerResponse alipayCallback(Map<String,String> params){
        //获取订单号
        Long orderNo= Long.parseLong(params.get("out_trade_no"));
        //获取交易号
        String tradeNo=params.get("trade_no");
        //获取交易状态
        String tradeStatus=params.get("trade_Status");

        Order order=orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("订单错误，请检查后再试");
        }
        if(order.getStatus()>= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if(Const.AlipayCallback.RESPONSE_SECCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDate( params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKey(order);
        }
        PayInfo payInfo=new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(orderNo);
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setPayPlatform(Const.payPlatformEnum.ALIPAY.getCode());

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    public  ServerResponse queryOrderPayStatus(Long orderNo,Integer userId){
        Order order=orderMapper.selectByUserIdAndOrderId(userId,orderNo);
        if(order==null){
            return ServerResponse.createByErrorMsg("用户没有该订单");
        }
        if(order.getStatus()>Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
