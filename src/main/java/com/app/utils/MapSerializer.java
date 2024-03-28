package com.app.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;


public class MapSerializer extends StdSerializer<Map<String, String>> {


    public MapSerializer() {
        this(null);
    }

    public MapSerializer(Class<Map<String, String>> t) {
        super(t);
    }

    @Override
    public void serialize(Map<String, String> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonGenerator.getCodec();
        ObjectNode node = mapper.createObjectNode();

        // Set the dataType field
        node.put("dataType", "Map");



        // Convert the map to an array of key-value pairs
        ArrayNode valueNode = mapper.createArrayNode();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ArrayNode entryNode = mapper.createArrayNode();
            entryNode.add(entry.getKey());
            entryNode.add(entry.getValue());
            valueNode.add(entryNode);
        }

        // Set the value field
        node.set("value ", valueNode);

        mapper.writeValue(jsonGenerator, node);
    }
}
