package com.shippingsystem.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Item  extends BaseModel{

    @Column
    private String name;

    @OneToMany(mappedBy = "item")
    private List<Order> orders;

}
