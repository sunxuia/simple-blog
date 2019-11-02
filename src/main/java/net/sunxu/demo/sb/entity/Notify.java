package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sunxu.demo.sb.entity.NotifyType;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(UserHistoryKey.class)
@Data
@ToString
@NoArgsConstructor
public class Notify {
    @Id
    @ManyToOne
    private User user;

    @Id
    @Column
    private Long timeKey;

    @Column
    private NotifyType notifyType;

    @Column
    private Date time;

    @Column
    private String action;

    @Column
    private String text;

    @Column
    private String url;

    @Column
    private Boolean hasReference;

    @Column
    private String referText;

    @Column
    private String referUrl;
}
