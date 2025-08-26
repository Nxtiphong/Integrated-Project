package tt2.int221.backend.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tt2.int221.backend.serializers.InstantNoZSerializer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class DetailDTO {
    private Integer id;
    private String model;
    private String brandName;
    private String description;
    private Integer price;
    private Integer ramGb;
    private BigDecimal screenSizeInch;
    private Integer quantity;
    private Integer storageGb;
    private String color;
    private Set<ImageDTO> saleItemImages;
    @JsonSerialize(using = InstantNoZSerializer.class)
    private Instant createdOn;
    @JsonSerialize(using = InstantNoZSerializer.class)
    private Instant updatedOn;

    public BigDecimal getScreenSizeInch() {
        return screenSizeInch == null ? null : screenSizeInch.stripTrailingZeros();
    }
}
