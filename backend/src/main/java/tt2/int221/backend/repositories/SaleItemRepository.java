package tt2.int221.backend.repositories;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tt2.int221.backend.entities.SaleItem;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Integer> {
    Page<SaleItem> findAllByOrderByCreatedOnAscIdAsc(Pageable pageable);
}
