package no.solea.cargodispatcher.service;

import lombok.extern.slf4j.Slf4j;
import no.solea.cargodispatcher.dto.PlanetResponseDTO;
import no.solea.cargodispatcher.mapper.PlanetMapper;
import no.solea.cargodispatcher.entities.Planet;
import no.solea.cargodispatcher.repository.PlanetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service layer responsible for handling planet-related operations.
 * Communicates with repository layer to retrieve planet data from the database.
 */
@Service
@Slf4j
public class PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    /**
     * Fetches all planets stored in the database.
     *
     * @return list of PlanetResponseDTO objects
     */
    public List<PlanetResponseDTO> getPlanets(){
        log.info("Fetching all planets from DB");
        List<Planet> planets = planetRepository.findAll();
        log.info("Found {} planets", planets.size());
        return PlanetMapper.toPlanetResponseDTOs(planets);
    }

    /**
     * Retrieves a planet by ID.
     *
     * @param id unique identifier of the planet
     * @return PlanetResponseDTO object
     * @throws ResponseStatusException if planet is not found
     */
    public PlanetResponseDTO getPlanetById(long id){
        log.info("Fetching planet with id {} from DB", id);
        Planet planet = planetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Planet not found for id " + id));
        log.info("Planet found {}", planet);
        return PlanetMapper.toPlanetResponseDTO(planet);
    }
}
