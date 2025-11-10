package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    void testSaveAndGetById() {
        Product product = new Product(null, "Widget", 5.0);
        Product saved = productRepository.save(product);

        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Widget");
    }

    @Test
    void testGetAllProducts() {
        productRepository.save(new Product(null, "A", 1.0));
        productRepository.save(new Product(null, "B", 2.0));

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }
}
