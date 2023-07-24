package com.app.models.canvas;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class CanvasObject {
    public String type;
    public String version;
    public String originX;
    public String originY;
    public double left;
    public double top;
    public double width;
    public double height;
    public String fill;
    public String stroke;
    public int strokeWidth;
    public CanvasObject strokeDashArray;
    public String strokeLineCap;
    public int strokeDashOffset;
    public String strokeLineJoin;
    public boolean strokeUniform;
    public int strokeMiterLimit;
    public int scaleX;
    public int scaleY;
    public int angle;
    public boolean flipX;
    public boolean flipY;
    public int opacity;
    public CanvasObject shadow;
    public boolean visible;
    public String backgroundColor;
    public String fillRule;
    public String paintFirst;
    public String globalCompositeOperation;
    public int skewX;
    public int skewY;
    public String id;
    public ArrayList<CanvasObject> canvasObjects;
    public ArrayList<Connection> connections;
    public double x1;
    public double x2;
    public double y1;
    public double y2;
    public int cropX;
    public int cropY;
    public String src;
    public CanvasObject crossOrigin;
    public ArrayList<CanvasObject> filters;

    // custom parameters

//    function
    public Map<String, String> parameters;
    public String returnType;
    public String functionBody;
    public String functionName;
    public String topic;
    public String deserializationClass;
    public String functionType;

//   database
    public ArrayList<String> tableNames;
    public ArrayList<String> tableDefinitions;

//  Rest

    public String url;
    public Map<String, String> headers;
    public Map<String, String> requestBody;
    public String requestUrl;
    public String apiType;
    public String httpMethod;
    public String methodName;



}
