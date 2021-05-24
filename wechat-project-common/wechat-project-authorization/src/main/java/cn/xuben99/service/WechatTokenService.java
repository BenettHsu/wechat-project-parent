package cn.xuben99.service;

import cn.xuben99.config.constant.WechatResponseConstant;
import cn.xuben99.utils.HttpUtil;
import cn.xuben99.utils.JsonUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component("wechatTokenService")
public class WechatTokenService {

    public String getToken(String wechatGrantUrl,String appId,String appsecret) throws IOException {
        // 发起GET请求获取凭证
        String requestUrl = String.format(wechatGrantUrl,appId,appsecret);
        String response = HttpUtil.doGet(requestUrl);
        String access_token = JsonUtil.getJsonStrValueByKey(response, WechatResponseConstant.ACCESS_TOKEN);
        return access_token;
    }
}
