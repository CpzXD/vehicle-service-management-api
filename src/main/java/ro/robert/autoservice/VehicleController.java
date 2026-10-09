package ro.robert.autoservice;

import org.springframework.web.bind.annotation.PutMapping;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.net.URI;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
public class VehicleController {
    @DeleteMapping("/api/vehicles/{id}")
public ResponseEntity<Void> deleteVehicle(
        @PathVariable("id") Long id) {

    boolean deleted = vehicleService.deleteVehicle(id);

    if (deleted) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
}@PutMapping("/api/vehicles/{id}")
public ResponseEntity<Vehicle> updateVehicle(
        @PathVariable("id") Long id,
        @Valid @RequestBody UpdateVehicleRequest request) {

    Optional<Vehicle> updated = vehicleService.updateVehicle(
        id,
        request.brand(),
        request.model(),
        request.licensePlate(),
        request.mileage()
    );

    if (updated.isPresent()) {
        return ResponseEntity.ok(updated.get());
    }

    return ResponseEntity.notFound().build();
}

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/api/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicleById(
            @PathVariable("id") Long id) {

        Optional<Vehicle> result = vehicleService.getVehicleById(id);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }

        return ResponseEntity.notFound().build();
    }@PostMapping("/api/vehicles")
public ResponseEntity<Vehicle> addVehicle(
        @Valid @RequestBody CreateVehicleRequest request) {

    Vehicle vehicle = vehicleService.addVehicle(
        request.brand(),
        request.model(),
        request.licensePlate(),
        request.mileage()
    );

    URI location = URI.create("/api/vehicles/" + vehicle.getId());

    return ResponseEntity.created(location).body(vehicle);


}
@GetMapping("/api/vehicles")
public List<Vehicle> getVehicles(@RequestParam(required = false) String brand) {
    if (brand != null && !brand.isBlank()) {
        return vehicleService.getVehiclesByBrand(brand);
    }
    return vehicleService.getVehicles();
}
}