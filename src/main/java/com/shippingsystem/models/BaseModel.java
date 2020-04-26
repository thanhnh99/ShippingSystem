package com.shippingsystem.models;

import lombok.Data;
import sun.rmi.runtime.Log;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date created_at;

    @Column
    private Date updated_at;

    @Column
    private String created_by;

    @Column String updated_by;
}
