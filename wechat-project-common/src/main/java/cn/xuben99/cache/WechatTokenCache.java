package cn.xuben99.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class WechatTokenCache {

    private String wechatToken;

    public WechatTokenCache(String wechatToken){
        this.wechatToken = wechatToken;
    }
}
