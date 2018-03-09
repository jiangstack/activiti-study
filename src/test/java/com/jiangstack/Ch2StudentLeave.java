package com.jiangstack;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class Ch2StudentLeave {

    //默认引擎自动读取activiti.cfg.xml
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署
     */
    @Test
    public void deploy(){

        Deployment deploy = processEngine.getRepositoryService()   //获取部署相关service
                .createDeployment()            //创建部署
                .addClasspathResource("bpmn/studentLeave.bpmn") //加载资源
                .name("学生请假流程")
                .deploy();//部署
        System.out.println("部署id"+deploy.getId());
        System.out.println("部署name"+deploy.getName());

    }

    @Test
    public void start() throws Exception{
        RuntimeService runtimeService = processEngine.getRuntimeService();   //运行时service
        ProcessInstance myFirstProcess =
                runtimeService.startProcessInstanceByKey("studentLeave");  //通过key得到流程实例
        System.out.println("id:"+myFirstProcess.getId());
        System.out.println("process id:"+myFirstProcess.getProcessDefinitionId());

    }

    @Test
    public void findStudentTaskState() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("s_jiang")
                .list();
        for (Task task : taskList) {
            System.out.println("---------");
            System.out.println("id:"+task.getId());
            System.out.println("name:"+task.getName());
            System.out.println("ass:"+task.getAssignee());
            System.out.println("did:"+task.getProcessDefinitionId());
            System.out.println("iid:"+task.getProcessInstanceId());
            System.out.println("iid:"+task.getCreateTime());
            System.out.println("----------");
        }
    }


    @Test
    public void studentCompleteTask() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("37504");
//        taskService.complete();
    }

    @Test
    public void findStudentMajorTaskState() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("sm_dan")
                .list();
        for (Task task : taskList) {
            System.out.println("---------");
            System.out.println("id:"+task.getId());
            System.out.println("name:"+task.getName());
            System.out.println("ass:"+task.getAssignee());
            System.out.println("did:"+task.getProcessDefinitionId());
            System.out.println("iid:"+task.getProcessInstanceId());
            System.out.println("iid:"+task.getCreateTime());
            System.out.println("----------");
        }
    }


    @Test
    public void studentMajorCompleteTask() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("25002");
    }

    @Test
    public void teacherTaskState() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("t_guangming")
                .list();
        for (Task task : taskList) {
            System.out.println("----------");
            System.out.println("id:"+task.getId());
            System.out.println("name:"+task.getName());
            System.out.println("ass:"+task.getAssignee());
            System.out.println("did:"+task.getProcessDefinitionId());
            System.out.println("iid:"+task.getProcessInstanceId());
            System.out.println("iid:"+task.getCreateTime());
            System.out.println("-----------");
        }
    }

    @Test
    public void teacherCompleteTask() throws Exception{
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("27502");
    }

    /**
     * 历史流程查询
     */
    @Test
    public void history(){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("32501")
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("name:"+historicTaskInstance.getName());
        }
    }

    /**
     * 设置流程变量
     * 学生task
     */
    @Test
    public void variableTest(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "37504";
        taskService.setVariable(taskId,"reson","发烧");
        taskService.setVariable(taskId,"days","1");
//        taskService.setVariables();
    }


    /**
     * 读取流程变量
     */
    @Test
    public void getVariableTest(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "45002";
        String reson = (String)taskService.getVariable(taskId, "reson");
        String days = (String)taskService.getVariable(taskId,"days");
        System.out.println("reson:"+reson+"//days:"+days);

    }





}
