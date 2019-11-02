package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class CollectionVO {
    private Long id;

    private String name;

    private String description;

    private Integer articleCount;

    private Date createTime;
}
