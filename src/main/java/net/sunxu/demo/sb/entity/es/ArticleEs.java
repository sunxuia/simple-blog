package net.sunxu.demo.sb.entity.es;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Document(indexName = "article", type = "article")
@Data
@ToString
public class ArticleEs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field
    private Long articleId;

    @Field
    private String title;

    @Field
    private String abstractText;

    @Field
    private String contentText;

}
