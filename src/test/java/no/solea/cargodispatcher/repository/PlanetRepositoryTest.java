package no.solea.cargodispatcher.repository;

import no.solea.cargodispatcher.entities.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @BeforeEach
    void setup() {
        planetRepository.deleteAll();
    }

    @Test
    void testSaveAndGetById() {
        Planet planet = new Planet(null, "Mars",0.52);
        Planet saved = planetRepository.save(planet);

        Optional<Planet> found = planetRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mars");
    }

    @Test
    void testGetAllPlanets() {
        planetRepository.save(new Planet(null, "Venus",2.0));
        planetRepository.save(new Planet(null, "Jupiter",4.2));

        List<Planet> planets = planetRepository.findAll();
        assertThat(planets).hasSize(2);
    }
}
