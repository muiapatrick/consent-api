package com.techminia.collection.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;

public class ConvertToJson {
    public static String setJsonString(Object content) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(content);
            return jsonInString;
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Object stringJsonToJsonObject(String stringJson){
        try {
            JSONParser jsonParser=new JSONParser();
            Object object =jsonParser.parse(stringJson);

            return object;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public static String trimedString(String str) {
        return str == null || str.trim().isEmpty() ? str : str.trim();
    }
}

