package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_info")
@Data
@ToString
@NoArgsConstructor
public class UserNotifySetting implements Serializable {
    @Id
    @Column
    private Long userId;

    @Column
    private Boolean articleLiked;

    @Column
    private Boolean commentREplied;

    @Column
    private Boolean commentLiked;
}
