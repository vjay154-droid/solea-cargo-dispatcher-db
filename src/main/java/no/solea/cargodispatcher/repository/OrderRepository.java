package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
