package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(ArticleHistory.ArticleHistoryKey.class)
@Data
@ToString
@NoArgsConstructor
public class ArticleHistory implements Serializable {
    public ArticleHistory(Long articleId, ArticleHistoryType type) {
        this.type = type;
        this.articleId = articleId;
        this.timeKey = System.currentTimeMillis();
    }

    @Id
    @Column
    private Long articleId;

    @Id
    @Column
    private Long timeKey;

    @Column
    private ArticleHistoryType type;

    @Column(columnDefinition = "text")
    private String preText;

    @Column(columnDefinition = "text")
    private String afterText;

    @Column(columnDefinition = "text")
    private String content;

    @Data
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ArticleHistoryKey implements Serializable {

        private Long articleId;

        private Long timeKey;
    }
}
