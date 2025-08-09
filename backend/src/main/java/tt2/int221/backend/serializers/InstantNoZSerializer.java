package tt2.int221.backend.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InstantNoZSerializer extends JsonSerializer<Instant> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'/'HH:mm:ss")  // รูปแบบที่ต้องการ มี '/' คั่นเวลา
            .withZone(ZoneId.of("Asia/Bangkok")); // ตั้ง timezone ตามต้องการ

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        String formatted = FORMATTER.format(value);
        gen.writeString(formatted);
    }
}