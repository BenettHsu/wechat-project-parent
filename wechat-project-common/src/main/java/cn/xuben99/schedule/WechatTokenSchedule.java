package cn.xuben99.schedule;

import cn.xuben99.cache.WechatTokenCache;
import cn.xuben99.config.ScheduleProperties;
import cn.xuben99.config.WechatProperties;
import cn.xuben99.service.WechatTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WechatTokenSchedule extends AbstractSchedule{

    private final String WECHAT_TOKEN_SCHEDULE = "刷新微信Token";

    private AtomicInteger scheduleCount = new AtomicInteger(1);


    @Resource(name = "wechatTokenCache")
    private WechatTokenCache wechatTokenCache;

    public WechatTokenSchedule(WechatTokenService wechatTokenService, WechatProperties wechatProperties, ScheduleProperties scheduleProperties) {
        super(wechatTokenService, wechatProperties, scheduleProperties);
    }

    @Scheduled(cron = "${wechat.schedule.wechatTokenScheduleCron}" )
    public void refreshWechatTokenSchedule(){
        if (this.scheduleCount.getAndIncrement() % 3 == 0) {
            this.execute(WECHAT_TOKEN_SCHEDULE);
        }
    }

    @Override
    public void handle(String taskName) {
        AtomicInteger currentRetryCount = new AtomicInteger(0);
        int totalRetryCount = scheduleProperties.getExceptionRetryCount();
        while (currentRetryCount.get() < totalRetryCount){
            try {
                //TODO 重置token的操作
                String toWechatGetNewToken = wechatTokenService.getToken(
                        wechatProperties.getWechatGrantUrl(),wechatProperties.getAppId(),wechatProperties.getAppSecret()
                );
                wechatTokenCache.setWechatToken(toWechatGetNewToken);
                log.info("获取到新的token：{}",wechatTokenCache.getWechatToken());
                return;
            }catch (Exception e){
                log.error(e.getMessage());
                retryHandleWhenException(currentRetryCount,taskName);
            }
        }
    }


}
