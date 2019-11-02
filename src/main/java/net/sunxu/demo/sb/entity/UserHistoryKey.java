package net.sunxu.demo.sb.entity;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserHistoryKey implements Serializable {
    private User user;

    private Long timeKey;
}
