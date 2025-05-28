package tt2.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBrandDTO {
    private String name;
    private String websiteUrl;
    @NotBlank
    private String countryOfOrigin;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isActive = true;
}

