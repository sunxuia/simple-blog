package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.NotifyType;

import java.util.Date;

@Data
@ToString
public class NotifyVO {
    private Long id;

    private NotifyType notifyType;

    private UserBreifVO user;

    private Date time;

    private String action;

    private String text;

    private String url;

    private Boolean hasRef;

    private String refText;

    private String refUrl;
}
