package tt2.int221.backend.dto.response;

import lombok.Data;

@Data
public class GelleryDTO {
    private int id;
    private String model;
    private String brandName;
    private int price;
    private int StorageGb;
    private int ramGb;
    private String color;
}
