package io.github.lunasaw.gbproxy.client.transmit.cmd;

import java.util.List;

import com.luna.common.check.Assert;
import com.luna.common.text.RandomStrUtil;

import io.github.lunasaw.sip.common.entity.DeviceAlarm;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
import io.github.lunasaw.sip.common.entity.notify.*;
import io.github.lunasaw.sip.common.entity.response.*;
import io.github.lunasaw.sip.common.enums.CmdTypeEnum;
import io.github.lunasaw.sip.common.subscribe.SubscribeInfo;
import io.github.lunasaw.sip.common.transmit.SipSender;

/**
 * @author luna
 * @date 2023/10/15
 */
public class ClientSendCmd {

    /**
     * 告警上报
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @return callId
     */
    public static String deviceAlarmNotify(FromDevice fromDevice, ToDevice toDevice, DeviceAlarm deviceAlarm) {
        DeviceAlarmNotify deviceAlarmNotify =
                new DeviceAlarmNotify(CmdTypeEnum.DEVICE_INFO.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceAlarmNotify.setAlarm(deviceAlarm);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceAlarmNotify);
    }

    /**
     * 上报设备状态
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param status
     * @return
     */
    public static String deviceKeepLiveNotify(FromDevice fromDevice, ToDevice toDevice, String status) {
        DeviceKeepLiveNotify deviceKeepLiveNotify =
            new DeviceKeepLiveNotify(CmdTypeEnum.DEVICE_INFO.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceKeepLiveNotify.setStatus(status);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceKeepLiveNotify);
    }

    public static String deviceChannelCatalogResponse(FromDevice fromDevice, ToDevice toDevice, List<DeviceItem> deviceItems, String sn) {
        DeviceResponse deviceResponse =
                new DeviceResponse(CmdTypeEnum.CATALOG.getType(), sn, toDevice.getUserId());

        deviceResponse.setSumNum(deviceItems.size());
        deviceResponse.setDeviceItemList(deviceItems);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceResponse);
    }

    /**
     * 上报设备信息
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceItems 通道状态
     * @return
     */
    public static String deviceChannelCatalogResponse(FromDevice fromDevice, ToDevice toDevice, List<DeviceItem> deviceItems) {
        return deviceChannelCatalogResponse(fromDevice, toDevice, deviceItems, RandomStrUtil.getValidationCode());
    }

    /**
     * 向上级回复DeviceInfo查询信息
     *
     * @param fromDevice
     * @param toDevice
     * @param deviceInfo
     * @return
     */
    public static String deviceInfoResponse(FromDevice fromDevice, ToDevice toDevice, DeviceInfo deviceInfo) {
        Assert.notNull(deviceInfo, "deviceInfo is null");
        deviceInfo.setCmdType(CmdTypeEnum.DEVICE_INFO.getType());
        deviceInfo.setSn(RandomStrUtil.getValidationCode());
        deviceInfo.setDeviceId(toDevice.getUserId());
        return SipSender.doMessageRequest(fromDevice, toDevice, deviceInfo);
    }

    /**
     * 推送设备状态信息
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param online "ONLINE":"OFFLINE"
     * @return
     */
    public static String deviceStatusResponse(FromDevice fromDevice, ToDevice toDevice, String online) {

        DeviceStatus deviceStatus =
                new DeviceStatus(CmdTypeEnum.DEVICE_STATUS.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceStatus.setStatus("ok");
        deviceStatus.setResult("ok");
        deviceStatus.setOnline(online);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceStatus);
    }

    /**
     * 设备位置推送
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param mobilePositionNotify
     * @return
     */
    public static String MobilePositionNotify(FromDevice fromDevice, ToDevice toDevice, MobilePositionNotify mobilePositionNotify,
                                              SubscribeInfo subscribeInfo) {
        mobilePositionNotify.setCmdType(CmdTypeEnum.DEVICE_INFO.getType());
        mobilePositionNotify.setSn(RandomStrUtil.getValidationCode());
        mobilePositionNotify.setDeviceId(toDevice.getUserId());
        return SipSender.doNotifyRequest(fromDevice, toDevice, mobilePositionNotify, subscribeInfo);
    }

    /**
     * 设备通道更新通知
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceItems 通道列表
     * @return
     */
    public static String deviceChannelUpdateCatlog(FromDevice fromDevice, ToDevice toDevice, List<DeviceUpdateItem> deviceItems,
                                                   SubscribeInfo subscribeInfo) {
        DeviceUpdateNotify deviceUpdateNotify =
            new DeviceUpdateNotify(CmdTypeEnum.CATALOG.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceUpdateNotify.setSumNum(deviceItems.size());
        deviceUpdateNotify.setDeviceItemList(deviceItems);

        return SipSender.doNotifyRequest(fromDevice, toDevice, deviceUpdateNotify, subscribeInfo);
    }

    /**
     * 事件更新推送
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceItems 推送事件
     * @return
     */
    public static String deviceOtherUpdateCatlog(FromDevice fromDevice, ToDevice toDevice, List<DeviceOtherUpdateNotify.OtherItem> deviceItems,
                                                 SubscribeInfo subscribeInfo) {
        DeviceOtherUpdateNotify deviceUpdateNotify =
                new DeviceOtherUpdateNotify(CmdTypeEnum.CATALOG.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceUpdateNotify.setSumNum(deviceItems.size());
        deviceUpdateNotify.setDeviceItemList(deviceItems);

        return SipSender.doNotifyRequest(fromDevice, toDevice, deviceUpdateNotify, subscribeInfo);
    }

    /**
     * 设备录像上报
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceRecordItems 录像文件
     * @return
     */
    public static String deviceCatalogResponse(FromDevice fromDevice, ToDevice toDevice, List<DeviceRecord.RecordItem> deviceRecordItems) {
        DeviceRecord deviceRecord =
                new DeviceRecord(CmdTypeEnum.RECORD_INFO.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        deviceRecord.setSumNum(deviceRecordItems.size());
        deviceRecord.setRecordList(deviceRecordItems);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceRecord);
    }

    /**
     * 流媒体状态推送
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param notifyType 121
     * @return
     */
    public static String deviceMediaStatusNotify(FromDevice fromDevice, ToDevice toDevice, String notifyType) {
        MediaStatusNotify mediaStatusNotify =
                new MediaStatusNotify(CmdTypeEnum.MEDIA_STATUS.getType(), RandomStrUtil.getValidationCode(), toDevice.getUserId());

        mediaStatusNotify.setNotifyType(notifyType);

        return SipSender.doMessageRequest(fromDevice, toDevice, mediaStatusNotify);
    }

    /**
     * 向上级发送BYE
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @return
     */
    public static String deviceBye(FromDevice fromDevice, ToDevice toDevice) {
        return SipSender.doByeRequest(fromDevice, toDevice);
    }

    /**
     * 回复ACK
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @return
     */
    public static String deviceAck(FromDevice fromDevice, ToDevice toDevice) {
        return SipSender.doAckRequest(fromDevice, toDevice);
    }

    public static String deviceAck(FromDevice fromDevice, ToDevice toDevice, String callId) {
        return SipSender.doAckRequest(fromDevice, toDevice, callId);
    }

    /**
     * 设备注册
     *
     * @param fromDevice 当前设备
     * @param toDevice   注册平台
     * @param expires    注册时间 0注销
     * @return
     */
    public String deviceRegister(FromDevice fromDevice, ToDevice toDevice, Integer expires) {
        return SipSender.doRegisterRequest(fromDevice, toDevice, expires);
    }
}
