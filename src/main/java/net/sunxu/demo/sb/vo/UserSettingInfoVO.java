package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;
import net.sunxu.demo.sb.entity.Gender;

import java.io.Serializable;

@Data
@ToString
public class UserSettingInfoVO implements Serializable {
    private String selfIntroduction;

    private Gender gender;

    private String city;
}
