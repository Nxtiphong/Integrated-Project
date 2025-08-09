package tt2.int221.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tt2.int221.backend.entities.SaleItem;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    List<SaleItem> findAllByOrderByCreatedOnAscIdAsc();

    @Query("SELECT COUNT(s) FROM SaleItem s WHERE s.brand.id = :brandId")
    long countByBrandId(@Param("brandId") Integer brandId);

    List<SaleItem> findAllByBrandId(Integer brandId);

    Page<SaleItem> findByBrandNameIn(List<String> brandNames, Pageable pageable);

    @Query("SELECT s FROM SaleItem s " +
            "JOIN s.brand b " +
            "WHERE (:brandNames IS NULL OR b.name IN :brandNames) " +
            "AND (:minPrice IS NULL OR s.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR s.price <= :maxPrice) " +
            "AND (:storageGb IS NULL OR s.storageGb IN :storageGb) ")
    Page<SaleItem> filterItems(@Param("brandNames") List<String> brandNames,
                               @Param("minPrice") Integer minPrice,
                               @Param("maxPrice") Integer maxPrice,
                               @Param("storageGb") List<Integer> storageGb,
                               Pageable pageable);

}


