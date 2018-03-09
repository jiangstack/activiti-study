package com.jiangstack;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;

public class Ch4GroupTest {

    //默认引擎自动读取activiti.cfg.xml
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void testSaveUser(){
        IdentityService identityService = processEngine.getIdentityService();
        User user  =  new UserEntity();
        user.setId("jiang");
        user.setPassword("jiang");
        user.setLastName("jiang");
        user.setEmail("jiangstack@163.com");
        identityService.saveUser(user);

    }

    @Test
    public void testGroup(){
        IdentityService identityService = processEngine.getIdentityService();
        Group group = new GroupEntity();
        group.setId("dev001");
        group.setName("dev");
        identityService.saveGroup(group);

        identityService.createMembership("jiang","dev001");
    }
}
