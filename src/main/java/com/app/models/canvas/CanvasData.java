package com.app.models.canvas;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class CanvasData {

    @Id
    private String id;

    public String version;
    public ArrayList<CanvasObject> objects;
}
