package cn.xuben99.schedule;

import cn.xuben99.config.ScheduleProperties;
import cn.xuben99.config.WechatProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@EnableScheduling
@EnableConfigurationProperties({WechatProperties.class, ScheduleProperties.class})

public abstract class AbstractTaskSchedule implements InterfaceTaskSchedule {
    public WechatProperties wechatProperties;
    public ScheduleProperties scheduleProperties;

    public AbstractTaskSchedule(WechatProperties wechatProperties, ScheduleProperties scheduleProperties){
        this.wechatProperties = wechatProperties;
        this.scheduleProperties = scheduleProperties;
    }

    @Resource(name = "threadPool")
    public ExecutorService threadPool;

    @Override
    public void execute(String taskName) {
        threadPool.execute(()->{
            handleWithException(taskName);
        });
    }

    public void handleWithException(String taskName){
        AtomicInteger currentRetryCount = new AtomicInteger(0);
        int totalRetryCount = scheduleProperties.getExceptionRetryCount();
        while (currentRetryCount.get() < totalRetryCount){
            try {
                handle();
                return;
            }catch (Exception e){
                log.error(e.getMessage());
                retryHandleWhenException(currentRetryCount,taskName);
            }
        }
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


}
