package org.example.basespringbootproject.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contexts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Context extends BaseEntity {

    @Column(name = "context_type", nullable = false, length = 100)
    private String type; // e.g. DEPARTMENT, BRANCH, MODULE

    @Column(name = "context_value", nullable = false, length = 100)
    private String value; // e.g. "Sales", "North Branch"
}
