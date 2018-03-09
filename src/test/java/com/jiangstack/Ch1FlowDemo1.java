package com.jiangstack;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 部署流程和走流程
 * deploy() 部署
 * start()  开始
 * findTaskState() 查看任务
 * completeTask() 完成
 */
public class Ch1FlowDemo1 {

    //默认引擎自动读取activiti.cfg.xml
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void  test1()  throws Exception{


    }

    /**
     * 部署流程定义
     * 有变化的表
     *
     * act_re_deployment 流程部署表
     * act_re_procdef    流程定义表
     * act_ge_bytearray 资源文件表
     *
     * 重复执行只要流程id一致 会在流程定义里新增加一个版本
     *
     */
    @Test
    public void deploy(){

        Deployment deploy = processEngine.getRepositoryService()   //获取部署相关service
                .createDeployment()            //创建部署
                .addClasspathResource("bpmn/demo1.bpmn") //加载资源
                .name("HelloDemo1")
                .deploy();//部署
        System.out.println("部署id"+deploy.getId());
        System.out.println("部署name"+deploy.getName());

    }

    /**
     * 使用zip部署流程定义
     */
    @Test
    public void deployWithZip(){
        InputStream resourceAsStream =
                this.getClass().getClassLoader().getResourceAsStream("bpmn/demozip.zip");

        Deployment helloZip = processEngine.getRepositoryService()
                .createDeployment()
                .addZipInputStream(new ZipInputStream(resourceAsStream))
                .name("helloZip")
                .deploy();

        System.out.println("部署id"+helloZip.getId());
        System.out.println("部署name"+helloZip.getName());


    }

    /**
     * 查询流程定义 by key
     * 对应表 act_re_procdef
     */
    @Test
    public void findProcessDefList(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> myFirstProcess = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myFirstProcess")
                .list();
        for (ProcessDefinition process : myFirstProcess) {
            System.out.println("info:"+process.toString());
        }
    }

    /**
     * 查询最新的流程定义 by key
     * 对应表 act_re_procdef
     */
    @Test
    public void findProcessDefListNewest(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> myFirstProcess = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myFirstProcess")
                .orderByProcessDefinitionVersion()
                .asc()    //升序
                .list();
        Map<String,ProcessDefinition> map = new HashMap<>();
        for (ProcessDefinition process : myFirstProcess) {
            map.put(process.getKey(),process);
        }
        //利用map覆盖原理
        List<ProcessDefinition> newestList = new ArrayList(map.values());
        for (ProcessDefinition processDefinition : newestList) {
            System.out.println("ddd:"+processDefinition);
        }
    }


    /**
     * 删除流程定义
     *
     * 流程定义的修改通过部署新版本来实现
     */
    @Test
    public void delProcessDef(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myFirstProcess")
                .list();
        for (ProcessDefinition processDefinition : list) {
            //级联删除
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
        }
    }

    /**
     * 查询单个流程
     */
    @Test
    public void findProcessById(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId("myFirstProcess:1:4")
                .singleResult();
        if(processDefinition!=null)
        System.out.println(processDefinition.toString());
    }


    /**
     * 使用流程定义的实例
     * act_ru_execution 运行时执行对象表
     * act_ru_identitylink  身份联系表
     * act_ru_task       任务表
     * act_hi_actinst    活动节点历史表
     * act_hi_identitylink 身份联系历史表
     * act_hi_procinst     实例历史表
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        RuntimeService runtimeService = processEngine.getRuntimeService();   //运行时service
        ProcessInstance myFirstProcess = runtimeService.startProcessInstanceByKey("myFirstProcess");  //通过key得到流程实例
        System.out.println("id:"+myFirstProcess.getId());
        System.out.println("process id:"+myFirstProcess.getProcessDefinitionId());

    }

    /**
     * 查看任务
     * 查询  act_ru_task 表
     *
     * @throws Exception
     */
    @Test
    public void findTaskState() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("jiang")
                .list();
        for (Task task : taskList) {
            System.out.println("-------");
            System.out.println("id:"+task.getId());
            System.out.println("name:"+task.getName());
            System.out.println("ass:"+task.getAssignee());
            System.out.println("did:"+task.getProcessDefinitionId());
            System.out.println("iid:"+task.getProcessInstanceId());
            System.out.println("iid:"+task.getCreateTime());
            System.out.println("-------");
        }
    }

    /**
     * 完成任务
     * 运行时 act_ru_*中相关的都没了
     * 历史表 act_hi_
     * @throws Exception
     */
    @Test
    public void completeTask() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("7504");
    }
}
