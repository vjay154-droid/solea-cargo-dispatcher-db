package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VehicleRepositoryTest {
    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setup() {
        vehicleRepository.deleteAll();
    }

    @Test
    void testSaveAndGetById() {
        Vehicle vehicle = new Vehicle(null, "Falcon",0.7,200.0);
        Vehicle saved = vehicleRepository.save(vehicle);

        Optional<Vehicle> found = vehicleRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Falcon");
    }

    @Test
    void testGetAllVehicles() {
        vehicleRepository.save(new Vehicle(null, "X",0.7,200.0));
        vehicleRepository.save(new Vehicle(null, "Y",0.4,500.0));

        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(2);
    }
}
