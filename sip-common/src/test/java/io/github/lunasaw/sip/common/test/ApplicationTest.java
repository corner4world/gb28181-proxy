package io.github.lunasaw.sip.common.test;

import io.github.lunasaw.sip.common.SipCommonApplication;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
import io.github.lunasaw.sip.common.layer.SipLayer;
import io.github.lunasaw.sip.common.utils.SipRequestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.luna.common.os.SystemInfoUtil;

import lombok.SneakyThrows;

/**
 * @author luna
 * @date 2023/10/13
 */
@SpringBootTest(classes = SipCommonApplication.class)
public class ApplicationTest {

    @Autowired
    private SipLayer sipLayer;

    @BeforeEach
    public void before() {
        sipLayer.addListeningPoint(SystemInfoUtil.getIP(), 8117);
    }

    @SneakyThrows
    @Test
    public void atest() {

        FromDevice fromDevice = FromDevice.getInstance("33010602011187000001", SystemInfoUtil.getIP(), 8117);

        ToDevice toDevice = ToDevice.getInstance("41010500002000000001", "192.168.2.102", 8116);

        String callId = SipRequestUtils.getNewCallId();

    }
}
