package org.example.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
public class TestJob {

    //@Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
        //执行代码
        System.out.println("定时任务执行");
    }
}
