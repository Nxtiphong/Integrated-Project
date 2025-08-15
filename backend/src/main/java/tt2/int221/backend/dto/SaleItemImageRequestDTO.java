package tt2.int221.backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaleItemImageRequestDTO {
    private Integer order;
    private String fileName;
    private Integer status;
    private MultipartFile imageFile;
}
