package io.github.lunasaw.gbproxy.server.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.github.lunasaw.sip.common.entity.xml.XmlBean;
import lombok.*;

/**
 * @author luna
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Notify")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceBroadcast extends XmlBean {
    @XmlElement(name = "CmdType")
    public String cmdType;

    @XmlElement(name = "SN")
    public String sn;

    @XmlElement(name = "SourceID")
    public String sourceId;

    @XmlElement(name = "TargetID")
    public String targetId;

    @SneakyThrows
    @Override
    public String toString() {
        return super.toString();
    }
}
