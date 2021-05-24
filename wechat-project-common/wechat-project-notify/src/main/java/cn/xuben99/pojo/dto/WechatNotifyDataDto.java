package cn.xuben99.pojo.dto;

import lombok.Data;

@Data
public class WechatNotifyDataDto {

    private String value;

    private String color;

    public WechatNotifyDataDto(String value, String color) {
        this.value = value;
        this.color = color;
    }
}