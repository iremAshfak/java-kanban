package httpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        String str = value.format(FORMATTER);
        out.value(str);
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String str = in.nextString();
        return LocalDateTime.parse(str, FORMATTER);
    }
}

