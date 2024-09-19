package com.premsan.blog24;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "blogs")
public class Blog {

    @Id
    private String id;

    @Version
    private Long version;

    @Column
    private String title;

    @Column
    private String content;
}
