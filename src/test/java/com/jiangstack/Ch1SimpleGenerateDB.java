package com.jiangstack;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * ref:http://blog.java1234.com/blog/articles/77.html
 *
 */
public class Ch1SimpleGenerateDB {

    /**
     * 使用xml配置 简化
     */
    @Test
    public void testCreateTableWithXml(){
        // 引擎配置
        ProcessEngineConfiguration pec=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 获取流程引擎对象
        ProcessEngine processEngine=pec.buildProcessEngine();
    }
}
