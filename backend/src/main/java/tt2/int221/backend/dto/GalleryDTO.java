package tt2.int221.backend.dto;

import lombok.Data;

@Data
public class GalleryDTO {
    private Integer id;
    private String model;
    private String brandName;
    private Integer price;
    private Integer storageGb;
    private Integer ramGb;
    private String color;
}
