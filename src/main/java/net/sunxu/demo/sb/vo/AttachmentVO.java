package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AttachmentVO {
    private Long id;

    private String name;

    private String url;

    private Boolean visible;
}
