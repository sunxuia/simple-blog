package net.sunxu.demo.sb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ToString
@Data
@NoArgsConstructor
public class Role implements Serializable {

    public static final String ADMIN = "ADMIN";

    public static final String ANONYMOUS = "ANONYMOUS";

    public static final String NORMAL = "NORMAL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String remark;
}
