package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    /**
     * 根据页面别名查询页面信息
     *
     * @param name
     * @return
     */
    public abstract CmsPage findByPageName(String name);

    /**
     * 根据唯一索引查询页面信息
     * @param siteId
     * @param pageName
     * @param pageWebPath
     * @return
     */
    public abstract CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String pageName, String pageWebPath);
}
