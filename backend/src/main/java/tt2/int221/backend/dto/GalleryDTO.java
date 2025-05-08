package tt2.int221.backend.dto;

import lombok.Data;

@Data
public class GalleryDTO {
    private int id;
    private String model;
    private String brandName;
    private int price;
    private int storageGb;
    private int ramGb;
    private String color;
}
