package tt2.int221.backend.dto.response;

import lombok.Data;

@Data
public class DetailDTO {
    private int id;
    private String model;
    private String brandName;
    private String description;
    private int price;
    private int ramGb;
    private double screenSizeInch;
    private int quantity;
    private int StorageGb;
    private String color;

}
