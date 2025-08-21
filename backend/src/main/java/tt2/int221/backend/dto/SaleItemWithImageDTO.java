package tt2.int221.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleItemWithImageDTO {
    private SaleItemDTO saleItem;
    private List<SaleItemImageRequestDTO> imageInfos;
}
