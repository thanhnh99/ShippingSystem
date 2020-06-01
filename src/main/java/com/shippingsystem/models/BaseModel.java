package com.shippingsystem.models;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private Date created_at = new Date();

    @Column
    private Date updated_at = new Date();

    @Column
    private String created_by;

    @Column String updated_by;
}
