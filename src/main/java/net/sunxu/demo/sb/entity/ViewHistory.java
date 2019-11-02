package net.sunxu.demo.sb.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@IdClass(UserHistoryKey.class)
@ToString
@NoArgsConstructor
public class ViewHistory {
    @Id
    @ManyToOne
    private User user;

    @Id
    private Long timeKey;

    @ManyToOne
    private Article article;
}
