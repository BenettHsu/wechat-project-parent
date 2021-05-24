package cn.xuben99.send;

import cn.xuben99.cache.WechatTokenCache;
import cn.xuben99.config.ScheduleProperties;
import cn.xuben99.config.WechatProperties;
import cn.xuben99.config.constant.WechatResponseConstant;
import cn.xuben99.pojo.dto.WechatNotifyDto;
import cn.xuben99.schedule.AbstractTaskSchedule;
import cn.xuben99.utils.HttpUtil;
import cn.xuben99.utils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class WechatNotifySender extends AbstractTaskSchedule{
    private final String WECHAT_SEND_NOTIFY = "发送微信通知";
    private WechatTokenCache wechatTokenCache;

    public WechatNotifySender(WechatProperties wechatProperties, ScheduleProperties scheduleProperties,WechatTokenCache wechatTokenCache) {
        super(wechatProperties, scheduleProperties);
        this.wechatTokenCache = wechatTokenCache ;
    }

    public void sendWechatNotify(List<WechatNotifyDto> list){
        threadPool.execute(()->{
            handleWithException(WECHAT_SEND_NOTIFY,list);
        });
    }

    private void sendWechatNotify(WechatNotifyDto wechatNotifyDto) throws Exception {
        String accessToken = wechatTokenCache.getWechatToken();
        String wechatAccessTokenUrl = String.format(WechatResponseConstant.wechatNotifyByTemplateIdUrl,accessToken);
        String jsonString = JSONObject.toJSONString(wechatNotifyDto);
        String response = HttpUtil.doPost(wechatAccessTokenUrl, jsonString);
        int errcode = JsonUtil.getJsonIntValueByKey(response, WechatResponseConstant.ERROR_CODE);
        if (WechatResponseConstant.SUCCESS != errcode){
            log.warn("wechat send notify failed -> request:{} -> response:{}",jsonString,response);
        }
        Assert.isTrue(WechatResponseConstant.SUCCESS == errcode,"wechat send notify failed");
    }

    public void handleWithException(String taskName,List<WechatNotifyDto> waitSendNotifyList) {
        AtomicInteger sendSuccessCount = new AtomicInteger(0);
        for (WechatNotifyDto dto : waitSendNotifyList){
            // 发送微信通知消息
            AtomicInteger currentRetryCount = new AtomicInteger(0);
            int totalRetryCount = scheduleProperties.getExceptionRetryCount();
            while (currentRetryCount.get() < totalRetryCount){
                try {
                    sendWechatNotify(dto);
                    sendSuccessCount.getAndIncrement();
                    break;
                }catch (Exception e){
                    log.error(e.getMessage());
                    retryHandleWhenException(currentRetryCount,taskName);
                }
            }
        }
        log.info("发送微信通知消息 -> 成功:{},总共:{}",sendSuccessCount.get(),waitSendNotifyList.size());
    }
}
