package tt2.int221.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "saleItem")
public class SaleItem {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 60)
    @NotNull
    @Column(name = "model", nullable = false, length = 60)
    private String model;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @NotNull
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "ramGb")
    private Integer ramGb;

    @Column(name = "screenSizeInch", precision = 3, scale = 1)
    private BigDecimal screenSizeInch;

    @Column(name = "storageGb")
    private Integer storageGb;

    @Lob
    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "saleItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("imageViewOrder ASC")
    private Set<SaleItemImage> saleItemImages = new LinkedHashSet<>();

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "createdOn", insertable = false, updatable = false)
    private Instant createdOn;

    @Column(name = "updatedOn", insertable = false, updatable = false)
    private Instant updatedOn;

}