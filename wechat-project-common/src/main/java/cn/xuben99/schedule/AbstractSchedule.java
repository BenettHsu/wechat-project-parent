package cn.xuben99.schedule;

import cn.xuben99.config.ScheduleProperties;
import cn.xuben99.config.WechatProperties;
import cn.xuben99.service.WechatTokenService;
import cn.xuben99.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@EnableScheduling
@EnableConfigurationProperties({WechatProperties.class,ScheduleProperties.class})

public abstract class AbstractSchedule implements InterfaceSchedule{
    public WechatTokenService wechatTokenService;
    public WechatProperties wechatProperties;
    public ScheduleProperties scheduleProperties;
    public AbstractSchedule(WechatTokenService wechatTokenService
            ,WechatProperties wechatProperties,ScheduleProperties scheduleProperties){
        this.wechatTokenService = wechatTokenService;
        this.wechatProperties = wechatProperties;
        this.scheduleProperties = scheduleProperties;

    }



    @Resource(name = "threadPool")
    ExecutorService threadPool;

    @Override
    public void execute(String taskName) {
        threadPool.execute(()->{
            Timer timer = new Timer(taskName);
            timer.start();
            handle(taskName);
            timer.end();
        });
    }

    public void retryHandleWhenException(AtomicInteger currentRetryCount,String taskName){
        //下次重试时间依次累加 10秒 20秒 30秒
        long nextRetryTime = currentRetryCount.incrementAndGet() * scheduleProperties.getExceptionRetryTimeGap();
        log.error("{}任务调度【发生异常】,将在{}秒后第{}次重试，共{}次",taskName
                ,nextRetryTime,currentRetryCount.get(),scheduleProperties.getExceptionRetryCount());
        try {
            Thread.sleep(nextRetryTime * SECOND);
        } catch (InterruptedException e) {
            log.warn("thread sleep throw exception");
        }
    }

    @Override
    public void notImplementHandleMethod() {
        log.error("not implement handle method!");
    }

    @Data
    public class Timer {
        private String taskName;
        private Long startTime;
        private Long endTime;

        Timer(String taskName) {
            this.taskName = taskName;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            log.info(">>>>>>>>>> {}任务调度开始:{} ", taskName, DateUtil.getCurrentStr());
        }

        public void end() {
            endTime = System.currentTimeMillis();
            log.info("<<<<<<<<<< {}任务调度结束 用时:{}毫秒", taskName, endTime - startTime);
        }
    }
}
