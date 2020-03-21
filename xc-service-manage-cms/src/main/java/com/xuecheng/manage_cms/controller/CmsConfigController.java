package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerAPI;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerAPI {
    @Autowired
    private PageService pageService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @GetMapping("/getModel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return pageService.getConfigById(id);
    }
}
