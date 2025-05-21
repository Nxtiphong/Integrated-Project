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
    private void  setName(String name) {
        this.name = name.trim();
//        if (this.name.length() < 31) {
//            this.name = this.name.substring(0, 30);
//        }
    }
    private void  setUrl(String url) {
        this.websiteUrl = url.trim();
//        if (this.websiteUrl.length() < 41) {
//            this.websiteUrl = this.websiteUrl.substring(0, 40);
//        }
    }
}

