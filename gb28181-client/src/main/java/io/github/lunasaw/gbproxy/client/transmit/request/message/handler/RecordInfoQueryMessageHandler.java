package io.github.lunasaw.gbproxy.client.transmit.request.message.handler;

import javax.sip.RequestEvent;

import io.github.lunasaw.gbproxy.client.transmit.cmd.ClientSendCmd;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
import io.github.lunasaw.sip.common.entity.base.DeviceSession;
import io.github.lunasaw.sip.common.entity.query.DeviceQuery;
import io.github.lunasaw.sip.common.entity.query.DeviceRecordQuery;
import io.github.lunasaw.sip.common.entity.response.DeviceRecord;
import org.springframework.stereotype.Component;

import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageHandlerAbstract;
import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageProcessorClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2023/10/19
 */
@Component
@Slf4j
@Getter
@Setter
public class RecordInfoQueryMessageHandler extends MessageHandlerAbstract {

    public static final String     CMD_TYPE = "RecordInfo";

    private String                 cmdType  = CMD_TYPE;

    private MessageProcessorClient messageProcessorClient;

    public RecordInfoQueryMessageHandler(MessageProcessorClient messageProcessorClient) {
        super(messageProcessorClient);
    }

    @Override
    public void handForEvt(RequestEvent event) {

        DeviceSession deviceSession = getDeviceSession(event);
        String userId = deviceSession.getUserId();
        String sipId = deviceSession.getSipId();

        // 设备查询
        FromDevice fromDevice = (FromDevice)messageProcessorClient.getFromDevice(userId);
        ToDevice toDevice = (ToDevice)messageProcessorClient.getToDevice(sipId);

        DeviceRecordQuery deviceRecordQuery = parseRequest(event, fromDevice.getCharset(), DeviceRecordQuery.class);

        String sn = deviceRecordQuery.getSn();
        DeviceRecord deviceRecord = messageProcessorClient.getDeviceRecord(deviceRecordQuery);
        deviceRecord.setSn(sn);

        ClientSendCmd.deviceRecordResponse(fromDevice, toDevice, deviceRecord);
    }

    @Override
    public String getCmdType() {
        return cmdType;
    }
}
