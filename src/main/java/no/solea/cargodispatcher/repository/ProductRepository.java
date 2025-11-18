package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.size > :minSize")
    List<Product> findBySizeGreaterThan(Double minSize);
}
