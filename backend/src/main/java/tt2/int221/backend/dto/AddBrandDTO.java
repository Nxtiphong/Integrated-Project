package tt2.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class AddBrandDTO {
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isActive = true;
}

