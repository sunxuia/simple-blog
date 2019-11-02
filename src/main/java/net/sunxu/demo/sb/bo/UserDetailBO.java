package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class UserDetailBO extends UserBO {

    private Integer articleCount;

    private Integer likeCount;

    private Integer collectCount;

    private Date lastLoginTime;

    private String city;
    
}
