package io.github.lunasaw.gbproxy.server.transimit.request.bye;

import gov.nist.javax.sip.message.SIPRequest;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.transmit.event.request.SipRequestProcessorAbstract;
import io.github.lunasaw.sip.common.utils.SipUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sip.RequestEvent;

/**
 * SIP命令类型： 收到Bye请求
 * 客户端发起Bye请求，结束通话
 * @author luna
 */
@Component
@Getter
@Setter
public class ByeRequestProcessorServer extends SipRequestProcessorAbstract {

    public static final String METHOD = "BYE";

    private String method = METHOD;

    @Resource
    private ByeProcessorServer byeProcessorServer;

    /**
     * 收到Bye请求 处理
     *
     * @param evt
     */
    @Override
    public void process(RequestEvent evt) {
        SIPRequest request = (SIPRequest) evt.getRequest();

        // 在服务端看来 收到请求的时候fromHeader还是客户端的 toHeader才是自己的，这里是要查询自己的信息
        String sip = SipUtils.getUserIdFromToHeader(request);

        // 获取设备
        FromDevice fromDevice = (FromDevice) byeProcessorServer.getFromDevice();
        if (!sip.equals(fromDevice.getUserId())) {
            return;
        }

        String userId = SipUtils.getUserIdFromFromHeader(request);
        byeProcessorServer.receiveBye(userId);
    }

}
