package tt2.int221.backend.dto;

import lombok.Data;

@Data
public class BrandResponseDTO {
    private Integer id;
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;
}
