package cn.xuben99.config;

import cn.xuben99.cache.WechatTokenCache;
import cn.xuben99.config.constant.WechatResponseConstant;
import cn.xuben99.service.WechatTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.IOException;

@Slf4j
@Configuration
@EnableConfigurationProperties({WechatProperties.class, ScheduleProperties.class})
public class WechatAutoConfiguration {

    private final WechatProperties wechatProperties;

    private final ScheduleProperties scheduleProperties;

    public WechatAutoConfiguration(WechatProperties wechatProperties, ScheduleProperties scheduleProperties){
        Assert.notNull(wechatProperties,"properties -> wechat.signature ->  must not null");
        Assert.notNull(wechatProperties.getAppId(),"properties ->wechat.signature.appId -> must not null");
        Assert.notNull(wechatProperties.getAppSecret(),"properties -> wechat.signature.appSecret -> must not null");
        this.wechatProperties = wechatProperties;
        this.scheduleProperties = scheduleProperties;
    }


    @Bean(name = "wechatTokenCache")
    public WechatTokenCache initWechatToken(WechatTokenService wechatTokenService) throws IOException {
        //TODO 获取token
        String toWechatGetToken = wechatTokenService.getToken(
                WechatResponseConstant.wechatAccessTokenUrl,wechatProperties.getAppId(),wechatProperties.getAppSecret()
        );
        log.info("初始化token：{}",toWechatGetToken);
        return new WechatTokenCache(toWechatGetToken);
    }

}


