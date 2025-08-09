package tt2.int221.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemDTO {
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

    public void setModel(String model) {
        this.model = model != null ? model.trim() : null;
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : null;
    }

    public void setColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            this.color = null;
        } else {
            this.color = color.trim();
        }
    }

    public void setQuantity(Integer quantity) {
        this.quantity = (quantity == null || quantity < 0) ? 1 : quantity;
    }


}