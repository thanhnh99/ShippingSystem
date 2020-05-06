package com.shippingsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Item  extends BaseModel{

    @Column
    private String name;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders;

}
