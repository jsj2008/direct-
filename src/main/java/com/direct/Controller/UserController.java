package com.direct.Controller;

import com.direct.model.Tuser;
import com.direct.model.User;
import com.direct.service.Tuser.TuserService;
import com.direct.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private TuserService tuserService;

    @ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8"})
    public int addUser(Tuser tuser){
        System.out.println(tuser.getPassword()+"==>"+tuser.getPhone()+"==>"+tuser.getUserName()+"==>"+tuser.getUserId());
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

    @ResponseBody
    @RequestMapping(value = "/all/{pageNum}/{pageSize}", produces = {"application/json;charset=UTF-8"})
    public Object findAllUser(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){

        return tuserService.findAllUser(pageNum,pageSize);
    }
}
