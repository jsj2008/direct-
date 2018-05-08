package com.direct.WX;

import com.direct.common.constant.WxPayConfig;
import com.direct.model.User;
import com.direct.model.WXPayOrder;
import com.direct.model.WeChatAppLoginReq;
//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.direct.service.WXPayOrder.OrderService;
import com.direct.service.user.UserService;
import com.direct.utils.HmacUtil;
import com.direct.utils.IpUtils;
import com.direct.utils.PayUtil;
import com.direct.utils.StringUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.base64url.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx")
public class WXController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(WXController.class);

    public static boolean initialized = false;

    @ResponseBody
    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
    public Map<String,Object> login(WeChatAppLoginReq WxData){

//        logger.info("code "+ WxData.getCode() +
//                "getState "+ WxData.getState() +
//                "getRawData "+ WxData.getRawData() +
//                "getEncryptedData "+ WxData.getEncryptedData() +
//                "getIv "+ WxData.getIv()+
//                "getSignature "+ WxData.getSignature());
        String openId="";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+WxPayConfig.APPID+"&secret="+WxPayConfig.SECRET+"&js_code="+WxData.getCode()+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            logger.info("sessionData = "+ sessionData);
            JSONObject jsonObj = JSON.parseObject(sessionData);
            openId = jsonObj.getString("openid");
            String sessionKey = jsonObj.getString("session_key");

            User user = new User();
            //用户拒绝授权个人信息
            if (WxData.getState()==1){
                //插入个人信息 已经授权
                String signature = HmacUtil.SHA1(WxData.getRawData()+sessionKey);
                //logger.info("signature = "+ signature +"- =-- "+ WxData.getRawData()+"=="+sessionKey);
                if(!signature.equals(WxData.getSignature()))
                {
                    logger.info(" req signature="+WxData.getSignature());
                    //throw new SystemException(ResponseMsg.WECHAT_LOGIN_SIGNATURE_ERROR);
                }
                byte[] resultByte = null;
                try {
                    resultByte = decrypt(Base64.decode(WxData.getEncryptedData()), Base64.decode(sessionKey), Base64.decode(WxData.getIv()));
                } catch (Exception e) {
                    //throw new SystemException(ResponseMsg.WECHAT_LOGIN_USER_ERROR);
                }

                if(null != resultByte && resultByte.length > 0)
                {
                    String userInfoStr = "";
                    try {
                        userInfoStr = new String(resultByte, "UTF-8");
                    } catch (UnsupportedEncodingException e)
                    {
                        logger.error(e.getMessage());
                    }
                    logger.info("userInfo = "+ userInfoStr);
                    JSONObject userInfoObj = JSON.parseObject(userInfoStr);
                    //插入个人信息
                    user.setUserName(userInfoObj.getString("nickName"));
                    user.setCreatedtime(new Date());
                    user.setGender(userInfoObj.getString("gender"));
                    user.setAvatarurl(userInfoObj.getString("avatarUrl"));
                    user.setUnionid(userInfoObj.getString("unionId"));
                    user.setUsertype("WeiXin");
                    user.setUsertype(userInfoObj.getString("unionId"));
                    user.setOpenid(userInfoObj.getString("openId"));
                    user.setUpdatatime(new Date());
    //                Map<String,Object> data = userInfoBiz.insertOrUpdate(userPo);
    //                Map<String,Object> data = new HashMap<String, Object>();
    //                return data;
                }else
                {
                    //throw new SystemException(ResponseMsg.WECHAT_LOGIN_USER_ERROR);
                }

            }else{
                //插入个人信息 拒绝授权
                user.setUserName(null);
                user.setCreatedtime(new Date());
                user.setGender(null);
                user.setAvatarurl(null);
                user.setUsertype("WeiXin");
                user.setUsertype(null);
                user.setOpenid(openId);
                user.setUpdatatime(new Date());
            }
            if (userService.selectByOpenid(openId).isEmpty()){
                userService.addUser(user);
            }else{
                //已经存在该用户
                logger.info("====已经存在");
                userService.updateByOpenidSelective(user);
            }
        }
        Map<String,Object> data = new HashMap<String, Object>();
        data.put( "openid",openId);
        logger.info(" data="+data);

        return data;
    }

    /**
     * @Description: 发起微信支付
     * @param
     */
    @ResponseBody
    @RequestMapping(value = "/wxPay", produces = {"application/json;charset=UTF-8"})
    public Map<String,Object> wxPay(String openid,String name,String total_fee,HttpServletRequest request){
        try{
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            //商品名称
            String body = name;
            //获取客户端的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);
            //生成订单号
            String orderId=new SimpleDateFormat("yyyyMMddHHddss").format(new Date())+ StringUtils.getRandomStringByLength(10);
            logger.info(" nonce_str:"+nonce_str+"+spbill_create_ip:"+spbill_create_ip);
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.APPID);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderId);//商户订单号
            packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);//支付成功后的回调地址
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);//支付方式
            packageParams.put("openid", openid);

            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.APPID + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                    + "<openid>" + openid + "</openid>"
                    + "<out_trade_no>" + orderId + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + total_fee + "</total_fee>"
                    + "<trade_type>" + WxPayConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            logger.info("调试模式_统一下单接口 请求XML数据：" + xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);
            logger.info("调试模式_统一下单接口 返回XML数据：" + result);

            // 将解析结果存储在HashMap中
            Map map = PayUtil.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码

            Map<String, Object> response = new HashMap<String, Object>();//返回给小程序端需要的参数
            if(return_code=="SUCCESS"||return_code.equals(return_code)){
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp.toString());//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + WxPayConfig.APPID + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();

                response.put("paySign", paySign);
            }

            response.put("appid", WxPayConfig.APPID);

            return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description:微信支付
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/wxNotify", produces = {"application/json;charset=UTF-8"})
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        /*
        测试
        */
        WXPayOrder order = new WXPayOrder();
        order.setOpenid("123121131");
        List<WXPayOrder> xx=orderService.selectByOpenid("123456");
        logger.info("xx: "+xx +"=="+xx.isEmpty());
        if (xx.isEmpty()){
            orderService.insertSelective(order);
        }

        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        logger.info("接收到的报文：" + notityXml);

        Map map = PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            if(PayUtil.verify(PayUtil.createLinkString(map), (String)map.get("sign"), WxPayConfig.key, "utf-8")){

                logger.info("=======================支付成功======================");

                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        logger.info("resXml:"+resXml);
        logger.info("微信支付回调数据结束");


        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
        /*
    *
    * */
    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
    //生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
