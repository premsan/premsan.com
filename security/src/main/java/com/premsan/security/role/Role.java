package com.premsan.security.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id private String id;

    @Version private Long version;

    @Column private String name;

    @Column private Long updatedAt;

    @Column private String updatedBy;
}
