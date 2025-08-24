package tt2.int221.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "saleItem_images")
public class SaleItemImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Size(max = 255)
    @NotNull
    @Column(name = "actualFileName", nullable = false)
    private String actualFileName;

    @Size(max = 80)
    @NotNull
    @Column(name = "status", nullable = false, length = 80)
    private String status;

    @NotNull
    @Column(name = "imageViewOrder", nullable = false)
    private Integer imageViewOrder;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sale_item_id", nullable = false)
    private SaleItem saleItem;

    @Column(name = "createdOn", insertable = false, nullable = false)
    private Instant createdOn;

    @Column(name = "updatedOn", insertable = false, nullable = false)
    private Instant updatedOn;

}