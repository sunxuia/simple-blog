package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ArticleBriefVO implements Serializable {
    private Long id;

    private String title;

    private String url;

    private String brief;

    private Integer likeCount;

    private Date createTime;

    private Date lastModifiedTime;

    private UserBreifVO author;
}
