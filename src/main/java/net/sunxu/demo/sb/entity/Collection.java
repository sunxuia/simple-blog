package net.sunxu.demo.sb.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Collection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String remark;

    @Column
    private Long creatorId;

    @Column
    private Date createTime;

    @Transient
    @Formula("select count(*) from article_count r where r.collection_id = id")
    private Integer articleCount;

}
