package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private ArticleStatus status;

    @ManyToOne
    private Category category;

    @Column
    private String title;

    @Column
    private String abstractText;

    @Lob
    @Column
    private String contentText;

    @Column
    private Date createTime;

    @Column
    private Date releaseTime;

    @Column
    private Date lastModifiedTime;

}
