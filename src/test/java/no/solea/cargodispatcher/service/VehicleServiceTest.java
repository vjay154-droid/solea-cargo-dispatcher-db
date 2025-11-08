package no.solea.cargodispatcher.service;

import no.solea.cargodispatcher.dto.VehicleResponseDTO;
import no.solea.cargodispatcher.entities.Vehicle;
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
public class VehicleServiceTest {
    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle(1L, "Cheetah Gonzales", 15.0, 50.0);
        vehicle2 = new Vehicle(2L, "Tiny Truck", 5.0, 20.0);
    }

    @Test
    void getVehicles_shouldReturnAllVehicles() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<VehicleResponseDTO> vehicles = vehicleService.getVehicles();

        assertNotNull(vehicles);
        assertEquals(2, vehicles.size());
        assertEquals("Cheetah Gonzales", vehicles.get(0).name());
        assertEquals("Tiny Truck", vehicles.get(1).name());
    }

    @Test
    void getVehicleById_shouldReturnVehicle() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        VehicleResponseDTO response = vehicleService.getVehicleById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Cheetah Gonzales", response.name());
        assertEquals(50.0, response.capacity());
        assertEquals(15.0, response.speed());
    }

    @Test
    void getVehicleById_shouldThrowWhenNotFound() {
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> vehicleService.getVehicleById(999L)
        );

        assertEquals("Vehicle not found for id 999", exception.getReason());
    }
}
