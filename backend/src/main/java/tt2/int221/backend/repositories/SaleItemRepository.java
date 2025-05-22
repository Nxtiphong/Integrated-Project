package tt2.int221.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tt2.int221.backend.entities.SaleItem;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Integer> {
    Page<SaleItem> findAllByOrderByCreatedOnAscIdAsc(Pageable pageable);
    List<SaleItem> findAllByOrderByCreatedOnAscIdAsc();
    @Query("SELECT COUNT(s) FROM SaleItem s WHERE s.brand.id = :brandId")
    long countByBrandId(@Param("brandId") Integer brandId);
    List<SaleItem> findAllByBrandId(Integer brandId);
    Page<SaleItem> findAllByBrand_NameIn(List<String> brandNames, Pageable pageable);
    Page<SaleItem> findByBrandNameIn(List<String> brandNames, Pageable pageable);

}
