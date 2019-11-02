package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class UserVO implements Serializable {
    private Long id;
    private String name;
    private String avatarUrl;
    private String selfIntroduction;
    private Boolean isLocked;

    private int articleCount;
    private int likeCount;
    private Date registerTime;
    private Date lastLoginTime;
}
