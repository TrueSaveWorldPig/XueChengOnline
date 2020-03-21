package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author 11388
 */
@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String test2(Map<String, Object> map) {
        //使用restTemplate来请求轮播图的数据模型
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "index_banner";
    }

    //测试1
    @RequestMapping("/test1")
    public String test1(Map<String, Object> map) {
        //map就是freemarker模板使用的数据
        map.put("name", "传智播客");
        //学生1信息
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        //学生2信息
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        //给学生2添加好朋友列表
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        //给学生2添加最好的朋友
        stu2.setBestFriend(stu1);
        //两个学生集合
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        //向数据模型放数据
        map.put("stu1", stu1);
        //向数据模型放数据
        map.put("stuMap", stuMap);
        //返回模板文件名称
        //返回freemarker模板位置
        return "test1";
    }
}
