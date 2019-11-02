package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class ArticleVO {
    private Long id;

    private String title;

    private UserBreifVO author;

    private String category;

    private Date createTime;

    private Date lastModifiedTime;

    private int readCount;

    private int likeCount;

    private int collectCount;

    private int commentCount;

    private boolean liked;

    private String abstractText;

    private String contentText;

    private List<AttachmentVO> attachments;


}
