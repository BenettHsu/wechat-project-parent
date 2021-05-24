package cn.xuben99.config.constant;

public interface WechatResponseConstant {
    /**
     * 微信获取普通access_token，获取token的key
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * 微信发送通知消息，获取状态码的key
     */
    String ERROR_CODE = "errcode";
    /**
     * 微信发送通知消息，状态码 0 表示成功
     */
    int SUCCESS = 0;

    /**
     * 获取普通access_token url
     */
    String wechatAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 通过模版id 发微信通知消息 url
     */
    String wechatNotifyByTemplateIdUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
}
