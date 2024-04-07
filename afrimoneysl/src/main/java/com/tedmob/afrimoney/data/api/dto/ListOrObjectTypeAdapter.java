package com.tedmob.afrimoney.data.api.dto;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListOrObjectTypeAdapter<T> extends TypeAdapter<ListOrObject<T>> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != ListOrObject.class) {
                return null;
            }

            final ParameterizedType parameterizedType = (ParameterizedType) type.getType();
            final Type actualType = parameterizedType.getActualTypeArguments()[0];
            final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(actualType));
            return new ListOrObjectTypeAdapter(adapter);
        }
    };
    private final TypeAdapter<T> adapter;

    public ListOrObjectTypeAdapter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }


    @Override
    public ListOrObject<T> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            ArrayList<T> ls = new ArrayList<>();
            while (in.peek() != JsonToken.END_ARRAY) {
                ls.add(adapter.read(in));
            }
            in.endArray();
            return new ListOrObject<>(ls);
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            ArrayList<T> ls = new ArrayList<>();
            ls.add(adapter.read(in));
            return new ListOrObject<>(ls);
        } else {
            throw new IllegalStateException("Expected BEGIN_ARRAY or BEGIN_OBJECT as a candidate for ListOrObject, but was " + in.peek() + "\n" + in);
        }
    }

    @Override
    public void write(JsonWriter out, ListOrObject<T> value) throws IOException {
        // no-op
    }
}
