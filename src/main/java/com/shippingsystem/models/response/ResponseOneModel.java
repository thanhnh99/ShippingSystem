package com.shippingsystem.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOneModel<T> extends ResponseBaseModel{

    private T data;
}
