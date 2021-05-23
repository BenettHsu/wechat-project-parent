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
    /**
     * 微信公众平台 官方提供的api 鉴权地址
     */
    private String wechatGrantUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
}
