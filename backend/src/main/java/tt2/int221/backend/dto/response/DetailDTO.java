package tt2.int221.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

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
    private Integer StorageGb;
    private String color;
}
