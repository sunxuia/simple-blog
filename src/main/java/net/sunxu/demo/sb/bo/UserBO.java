package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sunxu.demo.sb.entity.Gender;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class UserBO implements Serializable {

    private Long id;

    private String name;

    private String avatarUrl;

    private String selfIntroduction;

    private Gender gender;

    private Date registerTime;

    private boolean locked;
}
