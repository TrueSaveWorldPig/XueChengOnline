package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsConfig 数据模型dao
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {
}
