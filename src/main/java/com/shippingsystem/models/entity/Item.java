package com.shippingsystem.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties("orders")
public class Item  extends BaseModel{

    @Column
    private String name;

    @Column(unique = true)
    private String code;

    @Column
    private int multiplicity;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Order> orders;

}
