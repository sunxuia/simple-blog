package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class UserEventVO implements Serializable {
    private Long userId;

    private String userName;

    private String eventName;

    private Date eventTime;

    private ArticleBriefVO article;
}
