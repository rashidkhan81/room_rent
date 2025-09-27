package com.room_rent.Room_Rent_Application.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Abstract base class for entities providing auditing fields.
 * <p>
 * Includes automatic tracking of creation and modification dates and users.
 * <ul>
 *   <li><b>createdDate</b>: Timestamp when the entity was created.</li>
 *   <li><b>modifiedDate</b>: Timestamp when the entity was last modified.</li>
 *   <li><b>createdBy</b>: User ID who created the entity.</li>
 *   <li><b>modifiedBy</b>: User ID who last modified the entity.</li>
 * </ul>
 * <p>
 * Uses Spring Data JPA auditing via {@link AuditingEntityListener}.
 *
 * @param <PK> Type of the primary key, must be {@link Serializable}
 */
@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

    @Basic
    @CreatedDate
    @Column(updatable = false, name = "created_date")
    @JsonIgnore
    private LocalDateTime createdDate;

    @Basic
    @LastModifiedDate
    @Column(name = "modified_date")
    @JsonIgnore
    private LocalDateTime modifiedDate;

    @Basic
    @CreatedBy
    @Column(updatable = false, name = "created_by")
    @JsonIgnore
    private Long createdBy;

    @Basic
    @LastModifiedBy
    @Column(name = "modified_by")
    @JsonIgnore
    private Long modifiedBy;
}
