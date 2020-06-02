package com.shippingsystem.models.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    private Date created_at = new Date();

    @Column
    private Date updated_at = new Date();

    @Column
    private String created_by;

    @Column String updated_by;
}
