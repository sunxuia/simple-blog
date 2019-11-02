package net.sunxu.demo.sb.vo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class ArticleEditVO {

    @NotNull
    private Long id;

    @NotEmpty
    @Length(max = 30)
    private String title;

    @Length(max = 200)
    private String abstractText;

    private String contentText;

    @NotNull
    private Boolean visible;// = Boolean.FALSE;

    @NotNull
    private Long categoryId;
}
