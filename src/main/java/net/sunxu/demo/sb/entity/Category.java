package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Category implements Serializable {

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
    @Formula("select count(*) from article a where a.category_id = id")
    private Integer articleCount;

}
