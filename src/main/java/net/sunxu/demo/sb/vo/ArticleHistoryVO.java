package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.ArticleHistoryType;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ArticleHistoryVO implements Serializable {

    private Date time;

    private ArticleHistoryType historyType;

    private String before;

    private String after;

    private String content;
}
