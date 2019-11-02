package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class CommentVO {

    private Long id;

    private UserBreifVO user;

    private String text;

    private Integer likeCount;

    private Date time;

    private Long replyTo;

    private UserBreifVO replyToUser;

}
