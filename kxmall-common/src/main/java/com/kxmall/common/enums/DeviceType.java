package com.kxmall.common.enums;

import com.kxmall.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对一套 用户体系
 *
 * @author kxmall
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc"),

    /**
     * app端
     */
    APP("app");

    public static DeviceType getDeviceType(String str) {
        for (DeviceType value : values()) {
            if (StringUtils.contains(str, value.getDevice())) {
                return value;
            }
        }
        throw new RuntimeException("'DeviceType' not found By " + str);
    }

    private final String device;
}
