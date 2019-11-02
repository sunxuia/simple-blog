package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ArticleEditBO implements Serializable {
    private Long id;

    private Long categoryId;

    private String title;

    private String abstractText;

    private String contentText;

}
