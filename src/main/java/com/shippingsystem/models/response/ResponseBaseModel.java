package com.shippingsystem.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseBaseModel {
    private String statusCode;
    private Message message;

    public ResponseBaseModel() {
        message = new Message();
    }

    @Data
    public class Message {
        private String title;
        private String content;
    }
}
