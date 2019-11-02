package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class CommentBO implements Serializable {
    private Long id;

    private Long articleId;

    private UserBO user;

    private String text;

    private Date time;

    private Long replyTo;

    private Integer likeCount;

    private Boolean commentLiked;
}
