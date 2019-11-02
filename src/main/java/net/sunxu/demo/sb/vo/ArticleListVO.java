package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.ArticleStatus;

import java.util.Date;

@Data
@ToString
public class ArticleListVO {
    private Long id;

    private String title;

    private String url;

    private Date releaseTime;

    private ArticleStatus status;

    private boolean hasDraft;

    private boolean draft;
}
