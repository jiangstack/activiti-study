activiti study note and code

### ch1 HelloWorld

- Ch1GenerateDB
- Ch1SimpleGenerateDB
- Ch1FlowDemo1

### ch2 常用接口

学生请假Demo

流程是:学生请假->班长审批->班主任审批

![](http://p0mxf0bq6.bkt.clouddn.com/18-3-9/24680236.jpg)

> Ch2StudentLeave

- 构建流程
- 执行任务
- 查看状态
- 判断流程实例状态
> 通过查询对应runtime是否已经结束
- 历史流程查询
- 历史活动查询

- 流程变量
> 通过taskService或runtimeService
> 启动时也可以通过startProcessInstanceByKey设置变量
> 完成任务时设置 taskService.complete(); * 经常使用 *

### ch3 流程控制

流程是:

- Ch2StudentLeave2

> 表达式 ${ exp } 在连线上设置

在完成任务时设置任务变量控制流程

网关

![](http://p0mxf0bq6.bkt.clouddn.com/18-3-9/61829024.jpg)
- 排它网关 ExclusiveGateway
- 并行网管

### ch4 任务分配

##### 个人任务
- 流程配置中写死 assignee
- 使用流程变量   Assignee  ${userId}
- 流程图配置一个 TaskListener
```java
public class MyTaskListener implements TaskListener{
 
    private static final long serialVersionUID = 1L;
 
    public void notify(DelegateTask delegateTask) {
        // TODO Auto-generated method stub
        delegateTask.setAssignee("李四"); // 指定办理人
    }
 
}
```

##### 多人任务
只要其中一个处理就OK

- 流程配置中写死 candidate users
- 使用流程变量   candidate users ${userIds}
- 流程图配置一个 TaskListener
```java
public class MyTaskListener implements TaskListener{
 
    private static final long serialVersionUID = 1L;
 
    public void notify(DelegateTask delegateTask) {
        // TODO Auto-generated method stub
        delegateTask.addCandidateUser("张三");
        delegateTask.addCandidateUser("李四");
        delegateTask.addCandidateUser("王五");
    }
 
}
```

##### 组任务分配(组<->角色)

> 内置用户组设计
> 相关表 act_id_*
> 用户 user  组group 关系membership
> 参考 Ch4GroupTest

两种方式
- 流程图写死
- 