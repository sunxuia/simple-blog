package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(UserHistoryKey.class)
@ToString
@Data
@NoArgsConstructor
public class UserHistory implements Serializable {
    public UserHistory(User user) {
        this.user = user;
        eventTime = new Date();
        timeKey = eventTime.getTime();
    }

    @Id
    @ManyToOne
    private User user;

    @Id
    @Column
    private Long timeKey;

    @Column
    private String eventName;

    @Column
    private Date eventTime;

    @Column
    private String eventContent;
}
