package net.sunxu.demo.sb.entity.es;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@ToString
public class UserEs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field
    private Long userId;

    @Field
    private String name;

    @Field
    private String selfIntroduction;

    @Field
    private String city;
}
