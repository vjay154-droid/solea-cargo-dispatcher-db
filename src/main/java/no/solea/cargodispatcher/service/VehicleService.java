package no.solea.cargodispatcher.service;

import lombok.extern.slf4j.Slf4j;
import no.solea.cargodispatcher.dto.VehicleResponseDTO;
import no.solea.cargodispatcher.mapper.VehicleMapper;
import no.solea.cargodispatcher.entities.Vehicle;
import no.solea.cargodispatcher.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service layer for managing vehicles.
 * Provides operations for retrieving vehicles and converting them to response DTOs.
 */
@Service
@Slf4j
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Fetch all vehicles from the database.
     *
     * @return list of VehicleResponseDTO objects
     */
    public List<VehicleResponseDTO> getVehicles(){
        log.info("Fetching all vehicles from DB");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        log.info("Found {} vehicles", vehicles.size());
        return VehicleMapper.toVehicleResponseDTO(vehicles);
    }

    /**
     * Fetch a vehicle by its ID.
     *
     * @param id unique vehicle ID
     * @return Vehicle entity
     * @throws ResponseStatusException if not found
     */
    public VehicleResponseDTO getVehicleById(long id){
        log.info("Fetching vehicle with ID {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehicle not found for id "+id));
        log.info("Found vehicle {}", vehicle);
        return VehicleMapper.toVehicleResponseDTO(vehicle);
    }

}
