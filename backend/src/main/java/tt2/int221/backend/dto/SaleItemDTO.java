package tt2.int221.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemDTO {
    private Integer id;
    private String model;
    private BrandDTO brand;
    private String description;
    private Integer price;
    private Integer ramGb;
    private BigDecimal screenSizeInch;
    private Integer quantity;
    private Integer storageGb;
    private String color;

    public BigDecimal getScreenSizeInch() {
        return screenSizeInch == null ? null : screenSizeInch.stripTrailingZeros();
    }
}
