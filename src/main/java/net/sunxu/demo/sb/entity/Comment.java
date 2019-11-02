package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Comment implements Serializable {
    @Id
    @Column
    private Long id;

    @Column
    private Long articleId;

    private Long userId;

    private String text;

    private Date time;

    private Long replyToId;
}
