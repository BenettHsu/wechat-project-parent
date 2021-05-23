package cn.xuben99.service;

import cn.xuben99.utils.HttpUtil;
import org.springframework.stereotype.Component;


@Component("wechatTokenService")
public class WechatTokenService {

    public String getToken(String wechatGrantUrl,String appId,String appsecret) {
        String requestUrl = wechatGrantUrl.replace("APPID", appId).replace("APPSECRET", appsecret);
        // 发起GET请求获取凭证
        String res = HttpUtil.doGet(requestUrl);
        return res;
    }
}
