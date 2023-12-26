package io.github.lunasaw.gbproxy.test.user.server;

import io.github.lunasaw.sip.common.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.github.lunasaw.gb28181.common.entity.notify.DeviceAlarmNotify;
import io.github.lunasaw.gb28181.common.entity.notify.DeviceKeepLiveNotify;
import io.github.lunasaw.gb28181.common.entity.notify.MediaStatusNotify;
import io.github.lunasaw.gb28181.common.entity.notify.MobilePositionNotify;
import io.github.lunasaw.gb28181.common.entity.response.DeviceRecord;
import io.github.lunasaw.gb28181.common.entity.response.DeviceResponse;
import io.github.lunasaw.gbproxy.server.transimit.request.message.MessageProcessorServer;
import io.github.lunasaw.gbproxy.test.config.DeviceConfig;
import io.github.lunasaw.sip.common.entity.RemoteAddressInfo;
import io.github.lunasaw.sip.common.entity.ToDevice;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2023/10/17
 */
@Slf4j
@Component
public class DefaultMessageProcessorServer implements MessageProcessorServer {

    @Autowired
    @Qualifier("serverFrom")
    private Device fromDevice;

    @Override
    public Device getToDevice(String userId) {
        return DeviceConfig.DEVICE_SERVER_VIEW_MAP.get(userId);
    }

    @Override
    public Device getFromDevice() {
        return fromDevice;
    }

    @Override
    public void keepLiveDevice(DeviceKeepLiveNotify deviceKeepLiveNotify) {
        log.info("接收到设备的心跳 keepLiveDevice::deviceKeepLiveNotify = {}", JSON.toJSONString(deviceKeepLiveNotify));
    }

    @Override
    public void updateRemoteAddress(String userId, RemoteAddressInfo remoteAddressInfo) {
        log.info("接收到设备的地址信息 updateRemoteAddress::remoteAddressInfo = {}", remoteAddressInfo);
        ToDevice device = (ToDevice) DeviceConfig.DEVICE_SERVER_VIEW_MAP.get(userId);
        if (device == null) {
            return;
        }
        device.setIp(remoteAddressInfo.getIp());
        device.setPort(remoteAddressInfo.getPort());
        DeviceConfig.DEVICE_SERVER_VIEW_MAP.put(userId, device);
    }

    @Override
    public void updateDeviceAlarm(DeviceAlarmNotify deviceAlarmNotify) {
        log.info("接收到设备的告警信息 updateDeviceAlarm::deviceAlarmNotify = {}", deviceAlarmNotify);
    }

    @Override
    public void updateMobilePosition(MobilePositionNotify mobilePositionNotify) {
        log.info("接收到设备的位置信息 updateMobilePosition::mobilePositionNotify = {}", mobilePositionNotify);
    }

    @Override
    public void updateMediaStatus(MediaStatusNotify mediaStatusNotify) {
        log.info("接收到设备的媒体状态信息 updateMediaStatus::mediaStatusNotify = {}", mediaStatusNotify);
    }

    @Override
    public void updateDeviceRecord(String userId, DeviceRecord deviceRecord) {
        log.info("接收到设备的录像信息 updateDeviceRecord::userId = {}, deviceRecord = {}", userId, JSON.toJSONString(deviceRecord));
    }

    @Override
    public void updateDeviceResponse(String userId, DeviceResponse deviceResponse) {
        log.info("接收到设备通道信息 updateDeviceResponse::userId = {}, deviceResponse = {}", userId, JSON.toJSONString(deviceResponse));
    }
}
