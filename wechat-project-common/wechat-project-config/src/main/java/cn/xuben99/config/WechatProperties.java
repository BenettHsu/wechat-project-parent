package cn.xuben99.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wechat.signature")
public class WechatProperties {

    /**
     * 微信公众平台 appId
     */
    private String appId;
    /**
     * 微信公众平台 appSecret
     */
    private String appSecret;
}
