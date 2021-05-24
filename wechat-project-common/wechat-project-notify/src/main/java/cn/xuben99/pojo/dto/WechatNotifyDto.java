package cn.xuben99.pojo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class WechatNotifyDto {

    private String touser;

    private String template_id;

    Map<String, WechatNotifyDataDto> data;
}