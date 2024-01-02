package sustech.ooad.mainservice.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateTimeString = parser.getText();
        return LocalDate.parse(dateTimeString, formatter);
    }
}
