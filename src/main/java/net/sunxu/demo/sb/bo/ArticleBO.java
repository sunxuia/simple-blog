package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sunxu.demo.sb.entity.Category;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class ArticleBO implements Serializable {
    private Long id;

    private String title;

    private String abstractText;

    private String contentText;

    private Date createTime;

    private Date releaseTime;

    private Date lastModifiedTime;

    private Category category;

    private UserBO author;


    private int likeCount;

    private boolean liked;

    private int collectCount;

    private int viewCount;

    private boolean visible;

    private boolean locked;

    private boolean hasDraft;

    private boolean released;
}
