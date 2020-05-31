package com.shippingsystem.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseListModel<T> extends ResponseBaseModel {
    private List<T> data;
}
