package com.direct.WXLogin;

import com.direct.common.constant.ResponseMsg;
import com.direct.model.User;
import com.direct.model.WeChatAppLoginReq;
//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.direct.service.user.UserService;
import com.direct.utils.HmacUtil;
import org.omg.CORBA.SystemException;

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
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx")
public class WXLoginController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(WXLoginController.class);

    public static boolean initialized = false;

    private static final String APPID = "wx577429cf3ab57317";
    private static final String SECRET = "331c1c7752e3f0c1914e1cffcac1a8d4";

    @ResponseBody
    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
    public Map<String,Object> login(WeChatAppLoginReq WxData){

        logger.info("code "+ WxData.getCode() +
                "getState "+ WxData.getState() +
                "getRawData "+ WxData.getRawData() +
                "getEncryptedData "+ WxData.getEncryptedData() +
                "getIv "+ WxData.getIv()+
                "getSignature "+ WxData.getSignature());
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+WxData.getCode()+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            logger.info("sessionData = "+ sessionData);
            JSONObject jsonObj = JSON.parseObject(sessionData);
            String openId = jsonObj.getString("openid");
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
                logger.info("====已经存在");
                //已经存在
                userService.updateByOpenidSelective(user);
            }
        }
        //打印
        logger.info(WxData.getCode()+"--:"+responseEntity.getStatusCode()+"--:"+HttpStatus.OK);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("==="+df.format(new Date()));// new Date()为获取当前系统时间

        Map<String,Object> data = new HashMap<String, Object>();
        data.put( "1",123);
        data.put( "2",456);
        logger.info(" data="+data);

        return data;
    }

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
