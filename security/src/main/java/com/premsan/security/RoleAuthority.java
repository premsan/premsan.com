package com.premsan.security;

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
@Table(name = "role_authorities")
public class RoleAuthority {

    @Id private String id;

    @Version private Long version;

    @Column private String roleId;

    @Column private String authorityId;

    @Column private Long updatedAt;

    @Column private String updatedBy;
}
