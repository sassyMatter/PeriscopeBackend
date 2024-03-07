package com.app.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    String response;

    public static Response ok() {
            return  null;
    }
}
