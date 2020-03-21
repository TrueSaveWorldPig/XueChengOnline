package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    private PageService pageService;

    @Test
    public void testGetPageHtml() {
        String pageHtml = null;
        try {
            pageHtml = pageService.getPageHtml("5db6dd9ecf751e07e8968c66");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(pageHtml);
    }
}
