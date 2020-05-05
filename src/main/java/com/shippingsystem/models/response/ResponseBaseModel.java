package com.shippingsystem.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseBaseModel {
    private String statusCode;
    private Body body;

    public ResponseBaseModel() {
        body = new Body();
    }

    @Data
    public class Body {
        private String title;
        private String message;
    }
}
