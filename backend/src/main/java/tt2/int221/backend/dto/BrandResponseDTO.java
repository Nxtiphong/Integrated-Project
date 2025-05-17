package tt2.int221.backend.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class BrandResponseDTO {
    private Integer id;
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;
    private String noOfSaleItems;
//    private Instant createdOn;
//    private Instant updatedOn;
}
