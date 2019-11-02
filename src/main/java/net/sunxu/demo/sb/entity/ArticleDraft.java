package net.sunxu.demo.sb.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class ArticleDraft implements Serializable {
    @Id
    private Long articleId;

    @Column
    private String title;

    @Column
    private Long categoryId;

    @Column
    private String abstractText;

    @Column
    private String contentText;

    @Column
    private Date lastModifiedTime;
}
