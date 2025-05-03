package tt2.int221.backend.dto.response;

import lombok.Data;

@Data
public class DetailDTO {
    private Integer id;
    private String model;
    private String brandName;
    private String description;
    private Integer price;
    private Integer ramGb;
    private double screenSizeInch;
    private Integer quantity;
    private Integer StorageGb;
    private String color;
}
