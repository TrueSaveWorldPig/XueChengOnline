package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkerTest {

    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        String classPath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates/"));
        Template template = configuration.getTemplate("test1.ftl");
        //定义数据模型
        Map map = getMap();
        //静态化
        String string = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        System.out.println(string);
        InputStream inputStream = IOUtils.toInputStream(string);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:/songs/test1.html"));
        //输出文件
        IOUtils.copy(inputStream, fileOutputStream);
        //关流
        inputStream.close();
        fileOutputStream.close();

    }

    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //模板内容
        //模板内容，这里测试时使用简单的字符串作为模板
        String templateString = "" + "<html>\n" + " <head></head>\n" + " <body>\n" + " 名称：${name}\n" + " </body>\n" + "</html>";

        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("template", templateString);
        configuration.setTemplateLoader(templateLoader);
        //得到模板
        Template template = configuration.getTemplate("template", "UTF-8");
        //定义数据模型
        Map map = getMap();
        //静态化
        String string = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        System.out.println(string);
        InputStream inputStream = IOUtils.toInputStream(string);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:/songs/test2.html"));
        //输出文件
        IOUtils.copy(inputStream, fileOutputStream);
        //关流
        inputStream.close();
        fileOutputStream.close();

    }

    private Map getMap() {
        Map map = new HashMap();
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
        return map;
    }
}
