package tt2.int221.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tt2.int221.backend.entities.Brand;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Boolean existsByName(String name);
}
