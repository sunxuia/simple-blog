package net.sunxu.demo.sb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(ArticleCollect.ArticleCollectKey.class)
@Data
@ToString
@NoArgsConstructor
public class ArticleCollect implements Serializable {

    @Id
    private Long articleId;

    @Id
    private Long collectionId;

    @Column
    private Date time;


    @Data
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ArticleCollectKey implements Serializable {

        private Long articleId;

        private Long collectionId;
    }
}
