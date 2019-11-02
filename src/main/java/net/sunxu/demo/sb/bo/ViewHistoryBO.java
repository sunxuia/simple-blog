package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.ArticleHistoryType;
import net.sunxu.demo.sb.vo.ArticleBriefVO;

import java.util.Date;

@ToString
@Data
public class ViewHistoryBO {
    private Date time;

    private String url;

    private String title;
}
