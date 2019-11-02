package net.sunxu.demo.sb.config.security.gitlab;

import lombok.Data;
import lombok.ToString;

import java.awt.print.PrinterAbortException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * gitlab的用户数据, 通过 https://api.weibo.com/2/users/show.json 获得的结果
 * 这个是从sdk 压缩包拿来的数据修改了一一下.
 */
@Data
@ToString
public class GitlabUserProfile implements Serializable {
    private Long id;

    private String name;

    private String username;

    private String avatarUrl;

    private String webUrl;

    private String email;

}
