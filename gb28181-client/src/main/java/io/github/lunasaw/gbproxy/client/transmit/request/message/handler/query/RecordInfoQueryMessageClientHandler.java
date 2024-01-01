package io.github.lunasaw.gbproxy.client.transmit.request.message.handler.query;

import javax.sip.RequestEvent;

import org.springframework.stereotype.Component;

import io.github.lunasaw.gb28181.common.entity.query.DeviceRecordQuery;
import io.github.lunasaw.gb28181.common.entity.response.DeviceRecord;
import io.github.lunasaw.gbproxy.client.transmit.cmd.ClientSendCmd;
import io.github.lunasaw.gbproxy.client.transmit.request.message.ClientMessageRequestProcessor;
import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageClientHandlerAbstract;
import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageProcessorClient;
import io.github.lunasaw.gbproxy.client.user.SipUserGenerateClient;
import io.github.lunasaw.sip.common.entity.DeviceSession;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
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
public class RecordInfoQueryMessageClientHandler extends MessageClientHandlerAbstract {

    public static final String CMD_TYPE = "RecordInfo";

    private String             cmdType  = CMD_TYPE;

    public RecordInfoQueryMessageClientHandler(MessageProcessorClient messageProcessorClient, SipUserGenerateClient sipUserGenerateClient) {
        super(messageProcessorClient, sipUserGenerateClient);
    }

    @Override
    public String getRootType() {
        return ClientMessageRequestProcessor.METHOD + QUERY;
    }

    @Override
    public void handForEvt(RequestEvent event) {

        DeviceSession deviceSession = getDeviceSession(event);
        String sipId = deviceSession.getSipId();

        // 设备查询
        FromDevice fromDevice = (FromDevice)sipUserGenerate.getFromDevice();
        ToDevice toDevice = (ToDevice)sipUserGenerate.getToDevice(sipId);

        DeviceRecordQuery deviceRecordQuery = parseXml(DeviceRecordQuery.class);

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
