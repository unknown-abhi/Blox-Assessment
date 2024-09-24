package com.question3.blox;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonParser {

    public static Object parseJson(String jsonString) {
        JSONTokener tokener = new JSONTokener(jsonString);
        Object value = tokener.nextValue();

        if (value instanceof JSONObject) {
            return parseJSONObject((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return parseJSONArray((JSONArray) value);
        } else {
            return convertPrimitive(value);
        }
    }

    private static Map<String, Object> parseJSONObject(JSONObject jsonObject) {
        Map<String, Object> resultMap = new LinkedHashMap<>();

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            resultMap.put(key, parseValue(value));
        }

        return resultMap;
    }

    private static List<Object> parseJSONArray(JSONArray jsonArray) {
        List<Object> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            resultList.add(parseValue(value));
        }

        return resultList;
    }

    private static Object parseValue(Object value) {
        if (value instanceof JSONObject) {
            return parseJSONObject((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return parseJSONArray((JSONArray) value);
        } else {
            return convertPrimitive(value);
        }
    }

    private static Object convertPrimitive(Object value) {
        if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
            return new BigDecimal(value.toString());
        }
        return value; // Return value as is (String, Boolean, etc.)
    }

    public static void main(String[] args) {
        String jsonString = "{\"name\":\"Abhishek\", \"age\":25, \"balance\":12345.6789, \"married\":false}";

        String jsonArrayString = "[{\"name\":\"Abhishek\", \"age\":25, \"balance\":12345.6789}, [1, 2, 3, 4.5678901234567890123456789], \"stringValue\", true]";

        Object result = parseJson(jsonString);
        Object arrayResult = parseJson(jsonArrayString);

        System.out.println(result);
        System.out.println(arrayResult);
    }
}