package ro.robert.autoservice;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class VehicleService {
private final VehicleRepository vehicleRepository;

public VehicleService(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
}
  public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    public Vehicle addVehicle(String brand, String model, String licensePlate, Integer mileage) {
         String cleanedPlate = licensePlate.trim();
         for (Vehicle existingVehicle : vehicleRepository.findAll()) {
        if (existingVehicle.getLicensePlate().equalsIgnoreCase(cleanedPlate)) {
            throw new DuplicateLicensePlateException(cleanedPlate);
        }
    }
    Vehicle vehicle = new Vehicle(null, brand, model, licensePlate.trim(), mileage);
    return vehicleRepository.save(vehicle);

}
public boolean deleteVehicle(Long id) {
    if (vehicleRepository.existsById(id)) {
        vehicleRepository.deleteById(id);
        return true;
    }
    return false;
}
public Optional<Vehicle> updateVehicle(Long id, String brand, String model, String licensePlate, Integer mileage) {
    Optional<Vehicle> existing = getVehicleById(id);

    if (existing.isEmpty()) {
        return Optional.empty();
    }

    Vehicle currentVehicle = existing.get();
    if (mileage < currentVehicle.getMileage()) {
    throw new MileageDecreaseException(currentVehicle.getMileage(), mileage);
}
String cleanedPlate = licensePlate.trim();

for (Vehicle vehicle : vehicleRepository.findAll()) {
    boolean samePlate = vehicle.getLicensePlate().equalsIgnoreCase(cleanedPlate);
    boolean differentVehicle = !vehicle.getId().equals(id);

    if (samePlate && differentVehicle) {
        throw new DuplicateLicensePlateException(cleanedPlate);
    }
}
Vehicle updated = new Vehicle(id, brand, model, cleanedPlate, mileage);
return Optional.of(vehicleRepository.save(updated));
}

public List<Vehicle> getVehiclesByBrand(String brand) {
    List<Vehicle> result = new ArrayList<>();

    for (Vehicle vehicle : vehicleRepository.findAll()) {
        if (vehicle.getBrand().equalsIgnoreCase(brand)) {
            result.add(vehicle);
        }
    }

    return result;
}


}