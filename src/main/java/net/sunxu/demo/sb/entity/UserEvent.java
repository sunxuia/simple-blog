package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@IdClass(UserHistoryKey.class)
@ToString
@NoArgsConstructor
public class UserEvent implements Serializable {

    @Id
    @ManyToOne
    private User user;

    @Id
    private Long timeKey;

    @Column
    private String eventName;

    @Column
    private Date eventTime;

    @ManyToOne
    private Article article;

    @Column
    private String eventContent;
}
