package tt2.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

@Data
public class UpdateBrandDTO {
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isActive = true;
    private void  setName(String name) {
        this.name = name.trim();
    }
    private void  setUrl(String url) {
        this.websiteUrl = url.trim();
    }
}

