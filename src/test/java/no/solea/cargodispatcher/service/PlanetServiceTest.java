package no.solea.cargodispatcher.service;

import no.solea.cargodispatcher.dto.PlanetResponseDTO;
import no.solea.cargodispatcher.entities.Planet;
import no.solea.cargodispatcher.repository.PlanetRepository;
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
public class PlanetServiceTest {
    @Mock
    private PlanetRepository planetRepository;

    @InjectMocks
    private PlanetService planetService;

    private Planet planet1;
    private Planet planet2;

    @BeforeEach
    void setUp() {
        planet1 = new Planet(1L, "Mars", 0.52);
        planet2 = new Planet(2L, "Venus", 0.8);
    }

    @Test
    void getPlanets_shouldReturnListOfPlanetResponseDTOs() {
        // Arrange
        when(planetRepository.findAll()).thenReturn(List.of(planet1, planet2));

        // Act
        List<PlanetResponseDTO> response = planetService.getPlanets();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Mars", response.get(0).name());
        assertEquals("Venus", response.get(1).name());

    }

    @Test
    void getPlanetById_shouldReturnPlanetResponseDTO() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(planet1));

        PlanetResponseDTO response = planetService.getPlanetById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mars", response.name());
        assertEquals(0.52, response.distance());
    }

    @Test
    void getPlanetById_shouldThrowExceptionWhenNotFound() {
        when(planetRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> planetService.getPlanetById(999L)
        );

        assertEquals("Planet not found for id 999", exception.getReason());
    }
}
