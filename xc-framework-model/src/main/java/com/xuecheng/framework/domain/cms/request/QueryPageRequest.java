package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

@Data
public class QueryPageRequest {
    //接受页面查询条件
    //站点Id
    private String siteId;
    //页面Id
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模板ID
    private String templateId;
}
