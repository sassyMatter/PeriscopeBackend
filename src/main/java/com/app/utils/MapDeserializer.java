package com.app.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapDeserializer extends StdDeserializer<Map<String, String>> {
    public MapDeserializer() {
        this(null);
    }

    public MapDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Map<String, String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        // custom deserialization logic for the Map<String, String> field
        // For example, we can read each entry from the JSON object
        // and create a Map<String, String> from it.

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        Map<String, String> map = new HashMap<>();

        if (node.isObject() && node.has("dataType") && node.has("value")) {
            String dataType = node.get("dataType").asText();
            if (dataType.equals("Map")) {
                JsonNode valueNode = node.get("value");
                if (valueNode.isArray()) {
                    ArrayNode arrayNode = (ArrayNode) valueNode;
                    for (JsonNode entryNode : arrayNode) {
                        if (entryNode.isArray() && entryNode.size() == 2) {
                            String key = entryNode.get(0).asText();
                            String value = entryNode.get(1).asText();
                            map.put(key, value);
                        }
                    }
                }
            }
        }

        return map;
    }
}
