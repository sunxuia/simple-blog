package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.Gender;

import java.io.Serializable;

@ToString
@Data
public class UserEditBO implements Serializable {

    private String selfIntroduction;

    private Gender gender;

    private String city;

}
