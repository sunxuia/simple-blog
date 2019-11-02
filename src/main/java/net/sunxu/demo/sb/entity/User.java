package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user_info")
@Data
@ToString
@NoArgsConstructor
public class User implements Serializable {

    public static final String ANONYMOUS = "ANONYMOUS";

    public static final String ADMIN = "ADMIN";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20, nullable = false)
    private String name;

    @Column
    private String remark;

    @Column(nullable = false)
    private UserState userState;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column
    private String password;

    @Column
    private Date registerTime;

    @Column
    private String createIpAddress;

    @Column(unique = true, nullable = false)
    private String mailAddress;

    @Column
    private String avatarUrl;

    @Column
    private Gender gender;

    @Column
    private String city;

    @Column
    private String selfIntroduction;
}
