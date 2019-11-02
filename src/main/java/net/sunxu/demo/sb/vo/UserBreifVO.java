package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserBreifVO implements Serializable {
    private Long id;

    private String name;

    private String url;

    private String avatarUrl;
}
