package com.shippingsystem.models.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @NotNull
    private String latitude;

    @NotNull
    private String longitude;
}
