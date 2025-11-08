package org.example.basespringbootproject.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userName;


    private String email;

    /**
     * Accept password on input but do not expose it in JSON responses.
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String fullName;

    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    /**
     * Comma-separated roles (e.g. "ROLE_USER,ROLE_ADMIN").
     */
    private String roles;

    // Auditing fields from AbstractAuditEntity
    private String createdBy;
    private OffsetDateTime createdDate;
    private String lastModifiedBy;
    private OffsetDateTime lastModifiedDate;
    private Boolean deleted;
    private Long version;
}