package net.sunxu.demo.sb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(RoleResource.ResourceKey.class)
@Data
@ToString
@NoArgsConstructor
public class RoleResource implements Serializable {

    @Id
    @ManyToOne
    private Role role;

    @Id
    @Column
    private String expression;

    @Column
    private String remark;

    @Data
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ResourceKey implements Serializable {

        private Role role;

        private String expression;
    }
}
