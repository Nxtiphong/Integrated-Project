package tt2.int221.backend.dto.response;

import lombok.Data;

@Data
public class GelleryDTO {
    private Long id;
    private String model;
    private String brandName;
    private Integer price;
    private Integer StorageGb;
    private Integer ramGb;
    private String color;
}
