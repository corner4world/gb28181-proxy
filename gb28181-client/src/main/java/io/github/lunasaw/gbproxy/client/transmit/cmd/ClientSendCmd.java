package io.github.lunasaw.gbproxy.client.transmit.cmd;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.luna.common.check.Assert;
import com.luna.common.text.RandomStrUtil;

import io.github.lunasaw.gb28181.common.entity.DeviceAlarm;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
import io.github.lunasaw.gb28181.common.entity.notify.*;
import io.github.lunasaw.gb28181.common.entity.response.*;
import io.github.lunasaw.gb28181.common.entity.enums.CmdTypeEnum;
import io.github.lunasaw.sip.common.subscribe.SubscribeInfo;
import io.github.lunasaw.sip.common.transmit.SipSender;
import io.github.lunasaw.sip.common.transmit.event.Event;

/**
 * @author luna
 * @date 2023/10/15
 */
public class ClientSendCmd {
    /**
     * 告警上报
     * 
     * @param fromDevice
     * @param toDevice
     * @param deviceAlarmNotify
     * @return
     */
    public static String deviceAlarmNotify(FromDevice fromDevice, ToDevice toDevice, DeviceAlarmNotify deviceAlarmNotify) {
        return SipSender.doMessageRequest(fromDevice, toDevice, deviceAlarmNotify.toString());
    }

    /**
     * 告警上报
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @return callId
     */
    public static String deviceAlarmNotify(FromDevice fromDevice, ToDevice toDevice, DeviceAlarm deviceAlarm) {
        DeviceAlarmNotify deviceAlarmNotify =
                new DeviceAlarmNotify(CmdTypeEnum.DEVICE_INFO.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceAlarmNotify.setAlarm(deviceAlarm);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceAlarmNotify.toString());
    }

    /**
     * 心跳设备状态
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param status
     * @return
     */
    public static String deviceKeepLiveNotify(FromDevice fromDevice, ToDevice toDevice, String status) {
        return deviceKeepLiveNotify(fromDevice, toDevice, status, null, null);
    }

    public static String deviceKeepLiveNotify(FromDevice fromDevice, ToDevice toDevice, String status, Event errorEvent) {
        return deviceKeepLiveNotify(fromDevice, toDevice, status, errorEvent, null);
    }

    public static String deviceKeepLiveNotify(FromDevice fromDevice, ToDevice toDevice, String status, Event errorEvent, Event okEvent) {
        DeviceKeepLiveNotify deviceKeepLiveNotify =
            new DeviceKeepLiveNotify(CmdTypeEnum.KEEPALIVE.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceKeepLiveNotify.setStatus(status);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceKeepLiveNotify.toString(), errorEvent, okEvent);
    }

    /**
     * 设备目录查询
     * 
     * @param fromDevice
     * @param toDevice
     * @param deviceResponse
     * @return
     */
    public static String deviceChannelCatalogResponse(FromDevice fromDevice, ToDevice toDevice, DeviceResponse deviceResponse) {
        return SipSender.doMessageRequest(fromDevice, toDevice, deviceResponse.toString());
    }

    public static void deviceChannelCatalogResponse(FromDevice fromDevice, ToDevice toDevice, List<DeviceItem> deviceItems, String sn) {
        DeviceResponse deviceResponse =
                new DeviceResponse(CmdTypeEnum.CATALOG.getType(), sn, fromDevice.getUserId());

        List<List<DeviceItem>> partition = Lists.partition(deviceItems, 20);

        for (List<DeviceItem> items : partition) {
            deviceResponse.setSumNum(deviceItems.size());
            deviceResponse.setDeviceItemList(items);

            deviceChannelCatalogResponse(fromDevice, toDevice, deviceResponse);
        }
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

        DeviceResponse deviceResponse =
            new DeviceResponse(CmdTypeEnum.CATALOG.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceResponse.setSumNum(deviceItems.size());
        deviceResponse.setDeviceItemList(deviceItems);

        return deviceChannelCatalogResponse(fromDevice, toDevice, deviceResponse);
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
        if (deviceInfo.getSn() == null) {
            deviceInfo.setSn(RandomStrUtil.getValidationCode());
        }
        return SipSender.doMessageRequest(fromDevice, toDevice, deviceInfo.toString());
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
                new DeviceStatus(CmdTypeEnum.DEVICE_STATUS.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceStatus.setOnline(online);

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceStatus.toString());
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
        if (mobilePositionNotify.getSn() == null) {
            mobilePositionNotify.setSn(RandomStrUtil.getValidationCode());
        }
        mobilePositionNotify.setDeviceId(fromDevice.getUserId());
        return SipSender.doNotifyRequest(fromDevice, toDevice, mobilePositionNotify.toString(), subscribeInfo);
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
                new DeviceUpdateNotify(CmdTypeEnum.CATALOG.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceUpdateNotify.setSumNum(deviceItems.size());
        deviceUpdateNotify.setDeviceItemList(deviceItems);

        return SipSender.doNotifyRequest(fromDevice, toDevice, deviceUpdateNotify.toString(), subscribeInfo);
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
                new DeviceOtherUpdateNotify(CmdTypeEnum.CATALOG.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        deviceUpdateNotify.setSumNum(deviceItems.size());
        deviceUpdateNotify.setDeviceItemList(deviceItems);

        return SipSender.doNotifyRequest(fromDevice, toDevice, deviceUpdateNotify.toString(), subscribeInfo);
    }

    /**
     * 设备录像上报
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceRecord 录像响应
     * @return
     */
    public static String deviceRecordResponse(FromDevice fromDevice, ToDevice toDevice, DeviceRecord deviceRecord) {

        return SipSender.doMessageRequest(fromDevice, toDevice, deviceRecord.toString());
    }

    /**
     * 设备录像上报
     *
     * @param fromDevice 发送设备
     * @param toDevice 接收设备
     * @param deviceRecordItems 录像文件
     * @return
     */
    public static void deviceRecordResponse(FromDevice fromDevice, ToDevice toDevice, List<DeviceRecord.RecordItem> deviceRecordItems, String sn) {
        sn = Optional.ofNullable(sn).orElse(RandomStrUtil.getValidationCode());
        DeviceRecord deviceRecord =
            new DeviceRecord(CmdTypeEnum.RECORD_INFO.getType(), sn, fromDevice.getUserId());

        List<List<DeviceRecord.RecordItem>> partition = Lists.partition(deviceRecordItems, 20);
        for (List<DeviceRecord.RecordItem> recordItems : partition) {
            deviceRecord.setSumNum(deviceRecordItems.size());
            deviceRecord.setRecordList(recordItems);

            deviceRecordResponse(fromDevice, toDevice, deviceRecord);
        }
    }

    /**
     * 设备配置上报
     * 
     * @param fromDevice
     * @param toDevice
     * @param deviceConfigResponse
     * @return
     */
    public static String deviceConfigResponse(FromDevice fromDevice, ToDevice toDevice, DeviceConfigResponse deviceConfigResponse) {
        return SipSender.doMessageRequest(fromDevice, toDevice, deviceConfigResponse.toString());
    }

    public static String deviceConfigResponse(FromDevice fromDevice, ToDevice toDevice, DeviceConfigResponse.BasicParam basicParam) {
        DeviceConfigResponse configResponse =
                new DeviceConfigResponse(CmdTypeEnum.RECORD_INFO.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        configResponse.setBasicParam(basicParam);
        configResponse.setResult("ok");

        return deviceConfigResponse(fromDevice, toDevice, configResponse);
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
                new MediaStatusNotify(CmdTypeEnum.MEDIA_STATUS.getType(), RandomStrUtil.getValidationCode(), fromDevice.getUserId());

        mediaStatusNotify.setNotifyType(notifyType);

        return SipSender.doMessageRequest(fromDevice, toDevice, mediaStatusNotify.toString());
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

    public static String deviceAck(FromDevice fromDevice, ToDevice toDevice, String content, String callId) {
        return SipSender.doAckRequest(fromDevice, toDevice, content, callId);
    }

    /**
     * 设备注册
     *
     * @param fromDevice 当前设备
     * @param toDevice   注册平台
     * @param expires    注册时间 0注销
     * @return
     */
    public static String deviceRegister(FromDevice fromDevice, ToDevice toDevice, Integer expires) {
        return SipSender.doRegisterRequest(fromDevice, toDevice, expires);
    }

    public static String deviceRegister(FromDevice fromDevice, ToDevice toDevice, Integer expires, Event event) {
        return SipSender.doRegisterRequest(fromDevice, toDevice, expires, event);
    }

    /**
     * 设备注销
     *
     * @param fromDevice
     * @param toDevice
     * @return
     */
    public static String deviceUnRegister(FromDevice fromDevice, ToDevice toDevice) {
        return SipSender.doRegisterRequest(fromDevice, toDevice, 0);
    }
}
