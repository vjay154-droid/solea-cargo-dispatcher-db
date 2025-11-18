package no.solea.cargodispatcher.service;

import no.solea.cargodispatcher.dto.OrderItemRequestDTO;
import no.solea.cargodispatcher.dto.OrderRequestDTO;
import no.solea.cargodispatcher.dto.OrderResponseDTO;
import no.solea.cargodispatcher.entities.*;
import no.solea.cargodispatcher.repository.OrderRepository;
import no.solea.cargodispatcher.repository.PlanetRepository;
import no.solea.cargodispatcher.repository.ProductRepository;
import no.solea.cargodispatcher.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private OrderService orderService;

    private Planet planet;
    private Vehicle vehicle;
    private Product product1, product2;
    private OrderRequestDTO orderRequest;

    @BeforeEach
    void setUp() {
        planet = new Planet(1L, "Mars", 10.0);
        vehicle = new Vehicle(1L, "Falcon", 100.0, 20.0);
        product1 = new Product(1L, "Widget", 2.0);
        product2 = new Product(2L, "Gadget", 1.0);

        orderRequest = new OrderRequestDTO(
                1L,
                List.of(
                        new OrderItemRequestDTO(1L, 3),
                        new OrderItemRequestDTO(2L, 2)
                )
        );
    }

    @Test
    void placeOrder_shouldCreateOrderSuccessfully() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(planet));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO response = orderService.placeOrder(orderRequest);

        assertNotNull(response);
        assertEquals(planet.getName(), response.planetName());
        assertEquals(2, response.items().size());
        assertEquals("Widget", response.items().get(0).productName());
        assertEquals("Gadget", response.items().get(1).productName());
    }

    @Test
    void placeOrder_shouldThrowWhenPlanetNotFound() {
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.placeOrder(orderRequest));

        assertEquals("Planet not found", exception.getReason());
    }

    @Test
    void deleteOrder_shouldDeleteOrderSuccessfully() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteOrderById(1L);
    }

    @Test
    void deleteOrder_shouldThrowException(){
        when(orderRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> orderService.deleteOrderById(99L));
    }

    @Test
    void placeOrder_shouldThrowWhenProductNotFound() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(planet));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.placeOrder(orderRequest));

        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Product not found"));
    }

    @Test
    void placeOrder_shouldThrowWhenNoSuitableVehicle() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(planet));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        // Planet distance must not be null
        planet.setDistance(10.0);

        // Vehicle with too small capacity, valid speed
        Vehicle smallVehicle = new Vehicle(2L, "Tiny", 1.0, 1.0); // capacity < totalVolume
        when(vehicleRepository.findAll()).thenReturn(List.of(smallVehicle));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.placeOrder(orderRequest));

        assertEquals("Order volume exceeds maximum vehicle capacity", exception.getReason());
    }

    @Test
    void getOrders_shouldReturnAllOrders() {
        Order order = Order.builder()
                .id(1L)
                .planet(planet)
                .assignedVehicle(vehicle)
                .totalVolume(8.0)
                .travelTime(0.5)
                .items(List.of(
                        OrderItem.builder().product(product1).quantity(3).build(),
                        OrderItem.builder().product(product2).quantity(2).build()
                ))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponseDTO> orders = orderService.getOrders();
        assertEquals(1, orders.size());
        assertEquals("Mars", orders.getFirst().planetName());
    }

    @Test
    void getOrderById_shouldReturnOrder() {
        Order order = Order.builder()
                .id(1L)
                .planet(planet)
                .assignedVehicle(vehicle)
                .totalVolume(8.0)
                .travelTime(0.5)
                .items(List.of(
                        OrderItem.builder().product(product1).quantity(3).build(),
                        OrderItem.builder().product(product2).quantity(2).build()
                ))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.getOrderById(1L);
        assertEquals(1L, response.id());
        assertEquals("Mars", response.planetName());
    }

    @Test
    void getOrderById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.getOrderById(999L));

        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Order not found"));
    }
}
