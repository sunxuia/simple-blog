package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserSettingSocialBindVO implements Serializable {
    private String github;

    private String gitlab;

    private String weibo;

    private String facebook;
}
