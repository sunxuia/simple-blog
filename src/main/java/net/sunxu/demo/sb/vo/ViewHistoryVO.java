package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ViewHistoryVO implements Serializable {
    private Date time;

    private String url;

    private String title;
}
