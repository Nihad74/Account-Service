package account.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CustomYearMonthSerializer extends JsonSerializer<YearMonth> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("MMMM-yyyy");

    @Override
    public void serialize(YearMonth value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String currentRequestPath = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRequestURI();
        DateTimeFormatter formatter = currentRequestPath.contains("/api/empl/payment") ? CUSTOM_FORMATTER : DEFAULT_FORMATTER;
        String formattedDate = value.format(formatter);
        gen.writeString(formattedDate);
    }
}
