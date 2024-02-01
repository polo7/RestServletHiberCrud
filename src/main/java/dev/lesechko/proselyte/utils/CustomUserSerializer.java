package dev.lesechko.proselyte.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import dev.lesechko.proselyte.model.User;


public class CustomUserSerializer extends StdSerializer<User> {
    public CustomUserSerializer() {
        this(null);
    }

    public CustomUserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("user", user.getId());
        jsonGenerator.writeEndObject();
    }
}
