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
public class UserLoginHistory implements Serializable {

    @Id
    @ManyToOne
    private User user;

    @Id
    @Column
    private Long timeKey;

    @Column
    private Date loginTime;

    @Column
    private String ipAddress;

    @Column
    private String address;
}
