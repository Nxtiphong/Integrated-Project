package tt2.int221.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;


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
    private Instant createdOn;
    private Instant updatedOn;

    public BigDecimal getScreenSizeInch() {
        return screenSizeInch == null ? null : screenSizeInch.stripTrailingZeros();
    }
}
