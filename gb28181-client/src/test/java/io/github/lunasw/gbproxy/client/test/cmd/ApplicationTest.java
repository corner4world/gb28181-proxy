package io.github.lunasw.gbproxy.client.test.cmd;

import javax.sip.message.Request;

import io.github.lunasaw.gbproxy.client.transmit.response.impl.DefaultRegisterResponseProcessor;
import io.github.lunasaw.gbproxy.client.transmit.response.processor.RegisterResponseProcessor;
import io.github.lunasaw.sipproxy.common.transmit.event.Event;
import io.github.lunasaw.sipproxy.common.transmit.event.EventResult;
import io.github.lunasaw.sipproxy.common.transmit.impl.SipProcessorObserverImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.luna.common.os.SystemInfoUtil;
import com.luna.common.text.RandomStrUtil;

import io.github.lunasaw.gbproxy.client.transmit.cmd.SipRequestHeaderProvider;
import io.github.lunasaw.sipproxy.common.Gb28181Common;
import io.github.lunasaw.sipproxy.common.entity.FromDevice;
import io.github.lunasaw.sipproxy.common.entity.ToDevice;
import io.github.lunasaw.sipproxy.common.layer.SipLayer;
import io.github.lunasaw.sipproxy.common.transmit.SipSender;
import lombok.SneakyThrows;

/**
 * @author luna
 * @date 2023/10/13
 */
@SpringBootTest(classes = Gb28181Common.class)
public class ApplicationTest {

    FromDevice fromDevice;

    ToDevice   toDevice;

    @BeforeEach
    public void before() {
        SipLayer.addListeningPoint(SystemInfoUtil.getIP(), 8117);
        fromDevice = FromDevice.getInstance("33010602011187000001", SystemInfoUtil.getIP(), 8117);
        toDevice = ToDevice.getInstance("41010500002000000010", "192.168.2.102", 8116);
        toDevice.setPassword("weidian");
        toDevice.setRealm("4101050000");
    }

    @SneakyThrows
    @Test
    public void atest() {
        String callId = RandomStrUtil.getUUID();
        Request messageRequest = SipRequestHeaderProvider.createMessageRequest(fromDevice, toDevice, "123123", callId);
        SipSender.transmitRequest(fromDevice.getIp(), messageRequest);
    }

    @SneakyThrows
    @Test
    public void register() {
        String callId = RandomStrUtil.getUUID();
        Request registerRequest = SipRequestHeaderProvider.createRegisterRequest(fromDevice, toDevice, callId, 300);
        SipSender.transmitRequestSuccess(fromDevice.getIp(), registerRequest, new Event() {
            @Override
            public void response(EventResult eventResult) {
                System.out.println(eventResult);
            }
        });
    }

    @SneakyThrows
    @Test
    public void registerResponse() {

        String callId = RandomStrUtil.getUUID();
        Request registerRequest = SipRequestHeaderProvider.createRegisterRequest(fromDevice, toDevice, callId, 300);
        DefaultRegisterResponseProcessor responseProcessor = new DefaultRegisterResponseProcessor(fromDevice, toDevice, 300);
        SipProcessorObserverImpl.addResponseProcessor(RegisterResponseProcessor.METHOD, responseProcessor);


        SipSender.transmitRequestSuccess(fromDevice.getIp(), registerRequest, new Event() {
            @Override
            public void response(EventResult eventResult) {
                System.out.println(eventResult);
            }
        });
        Thread.sleep(30000);
    }
}
