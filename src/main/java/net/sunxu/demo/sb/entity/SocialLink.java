package net.sunxu.demo.sb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(indexes = @Index(columnList = "user_id"))
@Data
@ToString
@NoArgsConstructor
public class SocialLink implements Serializable {

    @Id
    @Column
    private String identity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column
    private String accountName;


}
