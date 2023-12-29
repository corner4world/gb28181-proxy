package io.github.lunasaw.gbproxy.server.config;

import java.util.Map;

import io.github.lunasaw.gbproxy.server.transimit.request.bye.ByeProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.bye.CustomByeProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.info.CustomInfoProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.info.InfoProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.message.CustomMessageProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.message.MessageProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.message.MessageServerHandlerAbstract;
import io.github.lunasaw.gbproxy.server.transimit.request.message.ServerMessageRequestProcessor;
import io.github.lunasaw.gbproxy.server.transimit.request.notify.CustomNotifyProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.notify.NotifyProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.register.CustomRegisterProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.request.register.RegisterProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.response.invite.CustomInviteResponseProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.response.invite.InviteResponseProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.response.subscribe.CustomSubscribeResponseProcessorServer;
import io.github.lunasaw.gbproxy.server.transimit.response.subscribe.SubscribeResponseProcessorServer;
import io.github.lunasaw.gbproxy.server.user.CustomSipUserGenerateServer;
import io.github.lunasaw.gbproxy.server.user.SipUserGenerateServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import io.github.lunasaw.sip.common.transmit.event.message.MessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2023/10/16
 */
@Slf4j
@Component
@ComponentScan(basePackages = "io.github.lunasaw.gbproxy.server")
public class SipProxyServerAutoConfig implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Map<String, MessageServerHandlerAbstract> messageHandlerMap = applicationContext.getBeansOfType(MessageServerHandlerAbstract.class);
        messageHandlerMap.forEach((k, v) -> ServerMessageRequestProcessor.addHandler(v));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageProcessorServer messageProcessorServer() {
        return new CustomMessageProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public SipUserGenerateServer sipUserGenerateServer() {
        return new CustomSipUserGenerateServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ByeProcessorServer byeProcessorServer() {
        return new CustomByeProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public InfoProcessorServer infoProcessorServer() {
        return new CustomInfoProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public NotifyProcessorServer notifyProcessorServer() {
        return new CustomNotifyProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisterProcessorServer registerProcessorServer() {
        return new CustomRegisterProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public InviteResponseProcessorServer inviteResponseProcessorServer() {
        return new CustomInviteResponseProcessorServer();
    }

    @Bean
    @ConditionalOnMissingBean
    public SubscribeResponseProcessorServer subscribeResponseProcessorServer() {
        return new CustomSubscribeResponseProcessorServer();
    }

}
