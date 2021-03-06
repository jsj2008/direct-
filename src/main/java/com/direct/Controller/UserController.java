package com.direct.Controller;

import com.direct.model.Tuser;
import com.direct.model.User;
import com.direct.model.paragraphWz;
import com.direct.service.Tuser.TuserService;
import com.direct.service.paragraph.ParagraphWzService;
import com.direct.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private TuserService tuserService;

    @Autowired
    private ParagraphWzService paragraphWzService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8"})
    public int addUser(Tuser tuser){
        System.out.println(tuser.getPassword()+"==>"+tuser.getPhone()+"==>"+tuser.getUserName()+"==>"+tuser.getUserId());
        System.out.println("==端口1111===");
        return tuserService.addUser(tuser);
    }
    @ResponseBody
    @RequestMapping(value = "/test_1")
    public String test1(){
//    public int test1(int parameter){
//        System.out.println(parameter);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }

    @RequestMapping(value="/test_2")
    @ResponseBody
    public void wxNotify( HttpServletResponse response) throws Exception{
        String resXml = "";

        //通知微信服务器已经支付成功
        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
    //价格计算测试
    @ResponseBody
    @RequestMapping(value = "/parPrice")
    public String parPrice(){
        paragraphWz resultPar = new paragraphWz();
        List<paragraphWz> resultList = paragraphWzService.selectAll();

        Iterator it = resultList.iterator();
        while(it.hasNext()) {
            Object object=it.next();
            logger.info("object = "+object);
        }
        return "parPrice";
    }

    @ResponseBody
    @RequestMapping(value = "/all/{pageNum}/{pageSize}", produces = {"application/json;charset=UTF-8"})
    public Object findAllUser(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){

        return tuserService.findAllUser(pageNum,pageSize);
    }
}
