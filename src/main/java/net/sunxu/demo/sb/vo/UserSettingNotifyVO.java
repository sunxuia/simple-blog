package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserSettingNotifyVO implements Serializable {
    private Boolean articleLiked;

    private Boolean articleCommented;

    private Boolean commentReplied;

    private Boolean commentLiked;
}
