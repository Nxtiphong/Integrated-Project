package tt2.int221.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tt2.int221.backend.entities.SaleItemImage;

import java.util.List;

@Repository
public interface SaleItemImageRepository extends JpaRepository<SaleItemImage, Integer> {
    List<SaleItemImage> findBySaleItemIdOrderByImageViewOrderAsc(Integer saleItemId);

    SaleItemImage findByFileName(String fileName);
}
