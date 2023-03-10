package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class FieldUtils {

    public static String getValueFromJsonNode(JsonNode jsonNode, String key) {
        return Optional.ofNullable(jsonNode.get(key))
                       .map(JsonNode::asText)
                       .orElse(null);
    }
}
