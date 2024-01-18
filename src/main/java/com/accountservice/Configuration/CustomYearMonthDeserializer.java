package account.Configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CustomYearMonthDeserializer extends JsonDeserializer<YearMonth> {
    @Override
    public YearMonth deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return YearMonth.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("MM-yyyy"));
    }
}
