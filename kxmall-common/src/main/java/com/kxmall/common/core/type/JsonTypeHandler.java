package com.kxmall.common.core.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonTypeHandler extends AbstractJsonTypeHandler<List<List<Object>>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected List<List<Object>> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<List<Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(List<List<Object>> obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
