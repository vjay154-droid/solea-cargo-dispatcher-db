package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Order;
import no.solea.cargodispatcher.entities.OrderItem;
import no.solea.cargodispatcher.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndGetOrderById() {
        Product product = productRepository.save(new Product(null, "Test Product", 1.0));
        Order order = new Order();
        order.setItems(List.of(new OrderItem(null, 2, product, order)));
        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getItems().getFirst().getProduct().getName()).isEqualTo("Test Product");
    }

    @Test
    void testGetAllOrders() {
        orderRepository.save(new Order());
        orderRepository.save(new Order());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);
    }
}
