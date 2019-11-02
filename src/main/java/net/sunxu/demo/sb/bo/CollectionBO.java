package net.sunxu.demo.sb.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class CollectionBO implements Serializable {
    private Long id;

    private String name;

    private String remark;

    private UserBO creator;

    private Date createTime;

    private Integer articleCount;
}
