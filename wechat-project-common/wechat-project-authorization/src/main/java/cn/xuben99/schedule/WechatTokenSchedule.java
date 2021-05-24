package cn.xuben99.schedule;

import cn.xuben99.cache.WechatTokenCache;
import cn.xuben99.config.ScheduleProperties;
import cn.xuben99.config.WechatProperties;
import cn.xuben99.config.constant.WechatResponseConstant;
import cn.xuben99.service.WechatTokenService;
import cn.xuben99.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WechatTokenSchedule extends AbstractTaskSchedule {

    private final String WECHAT_TOKEN_SCHEDULE = "刷新微信Token";

    private AtomicInteger scheduleCount = new AtomicInteger(1);
    public WechatTokenService wechatTokenService;
    private WechatTokenCache wechatTokenCache;

    public WechatTokenSchedule(WechatTokenService wechatTokenService, WechatProperties wechatProperties,
                               ScheduleProperties scheduleProperties, WechatTokenCache wechatTokenCache) {
        super(wechatProperties, scheduleProperties);
        this.wechatTokenService = wechatTokenService;
        this.wechatTokenCache = wechatTokenCache;
    }

    @Scheduled(cron = "0 0/30 * * * ?" )
    public void refreshWechatTokenSchedule(){
        if (this.scheduleCount.getAndIncrement() % 3 == 0) {
            this.execute(WECHAT_TOKEN_SCHEDULE);
        }
    }

    @Override
    public void handle() throws IOException {
        //TODO 重置token的操作
        Timer timer = new Timer(WECHAT_TOKEN_SCHEDULE);
        timer.start();
        String toWechatGetNewToken = wechatTokenService.getToken(
                WechatResponseConstant.wechatAccessTokenUrl,wechatProperties.getAppId(),wechatProperties.getAppSecret()
        );
        wechatTokenCache.setWechatToken(toWechatGetNewToken);
        log.info("获取到新的token：{}",wechatTokenCache.getWechatToken());
        timer.end();
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
