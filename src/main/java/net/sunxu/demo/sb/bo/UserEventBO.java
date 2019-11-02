package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class UserEventBO implements Serializable {

    private UserDetailBO user;

    private Date eventTime;

    private String eventName;

    private String eventContent;

    private ArticleBO article;
}
