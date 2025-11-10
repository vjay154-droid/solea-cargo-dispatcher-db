package no.solea.cargodispatcher.service;

import lombok.extern.slf4j.Slf4j;
import no.solea.cargodispatcher.dto.OrderItemRequestDTO;
import no.solea.cargodispatcher.dto.OrderRequestDTO;
import no.solea.cargodispatcher.dto.OrderResponseDTO;
import no.solea.cargodispatcher.mapper.OrderMapper;
import no.solea.cargodispatcher.entities.*;
import no.solea.cargodispatcher.repository.OrderRepository;
import no.solea.cargodispatcher.repository.PlanetRepository;
import no.solea.cargodispatcher.repository.ProductRepository;
import no.solea.cargodispatcher.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service layer for managing orders.
 * Handles placing orders, calculating total volume, assigning vehicles, and retrieving orders.
 */
@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final PlanetRepository planetRepository;
    private final ProductRepository productRepository;
    private final VehicleRepository vehicleRepository;

    public OrderService(OrderRepository orderRepository,
                        PlanetRepository planetRepository,
                        ProductRepository productRepository,
                        VehicleRepository vehicleRepository) {
        this.orderRepository = orderRepository;
        this.planetRepository = planetRepository;
        this.productRepository = productRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Places a new order.
     *
     * @param orderRequestDTO order request containing planet ID and items
     * @return OrderResponseDTO with assigned vehicle and calculated travel time
     */
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO){
        log.info("Placing order {}", orderRequestDTO);

        Planet planet = fetchPlanet(orderRequestDTO.planetId());

        List<OrderItem> items = new ArrayList<>();
        double totalVolume = 0.0;

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.orderItemDTOList()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found with id " + itemDTO.productId()));
            totalVolume += product.getSize() * itemDTO.quantity();

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.quantity())
                    .build();

            items.add(orderItem);
        }

        Vehicle assignedVehicle = findAssignedVehicle(totalVolume, planet.getDistance());

        Order order = buildOrder(planet, assignedVehicle, totalVolume, items);

        // Set the back-reference for OrderItem to Order.
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        order = orderRepository.save(order);

        log.info("Order placed {}", order);
        return OrderMapper.toOrderResponseDTO(order, planet,
                items.stream().map(OrderItem::getProduct).toList());
    }

    /**
     * Retrieve all orders.
     *
     * @return list of OrderResponseDTO
     */
    public List<OrderResponseDTO> getOrders() {
        log.info("Getting all orders from DB");
        List<Order> orders = orderRepository.findAll();

        log.info("Found {} orders from DB", orders.size());
        return orders.stream()
                .map(order -> OrderMapper.toOrderResponseDTO(order,
                        order.getPlanet(),
                        order.getItems().stream().map(OrderItem::getProduct).toList()))
                .toList();
    }

    /**
     * Retrieve a specific order by ID.
     *
     * @param id order ID
     * @return OrderResponseDTO
     */
    public OrderResponseDTO getOrderById(Long id) {
        log.info("Getting order {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Order not found with id " + id));

        log.info("Order found {}", order);
        return OrderMapper.toOrderResponseDTO(order,
                order.getPlanet(),
                order.getItems().stream().map(OrderItem::getProduct).toList());
    }

    /**
     * Assigns the most optimal vehicle for the order.
     * Chooses the vehicle that can carry the total volume and reach the planet fastest.
     */
    private Vehicle findAssignedVehicle(Double totalVolume, Double distance) {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        double maxCapacity = vehicles.stream()
                .mapToDouble(Vehicle::getCapacity)
                .max()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No vehicles available"));

        if (totalVolume > maxCapacity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order volume exceeds maximum vehicle capacity");
        }

        return vehicles.stream()
                .filter(v -> v.getCapacity() >= totalVolume)
                .min(Comparator.comparingDouble(v -> distance / v.getSpeed()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No suitable vehicle found"));
    }

    private Planet fetchPlanet(Long planetId) {
        return planetRepository.findById(planetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Planet not found"));
    }

    private Order buildOrder(Planet planet,
                             Vehicle assignedVehicle,
                             double totalVolume,
                             List<OrderItem> items) {
        return Order.builder()
                .planet(planet)
                .assignedVehicle(assignedVehicle)
                .totalVolume(totalVolume)
                .travelTime(planet.getDistance() / assignedVehicle.getSpeed())
                .items(items)
                .build();
    }
}
