package ro.robert.autoservice;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.mockito.Mockito;
class VehicleServiceTest {
@Test
void findsVehicleById() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Vehicle ford = new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000);
    Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(ford));

    VehicleService service = new VehicleService(mockRepository);

    Vehicle found = service.getVehicleById(2L).orElseThrow();

    assertEquals(Long.valueOf(2L), found.getId());
    assertEquals("Ford", found.getBrand());
    assertEquals("Focus", found.getModel());
}@Test
void deletingMissingVehicleDoesNotChangeList() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.existsById(99L)).thenReturn(false);

    VehicleService service = new VehicleService(mockRepository);

    boolean deleted = service.deleteVehicle(99L);

    assertFalse(deleted);
   
    Mockito.verify(mockRepository, Mockito.never()).deleteById(99L);
}

@Test
void returnsEmptyWhenVehicleDoesNotExist() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findById(99L)).thenReturn(Optional.empty());

    VehicleService service = new VehicleService(mockRepository);

    assertTrue(service.getVehicleById(99L).isEmpty());
}
    @Test
    void rejectsDuplicateLicensePlateWithoutAddingVehicle() {
        VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
        Mockito.when(mockRepository.findAll()).thenReturn(List.of(
            
            new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000)
         
        )
    );
        VehicleService service = new VehicleService(mockRepository);
        int initialCount = service.getVehicles().size();

        assertThrows(
            DuplicateLicensePlateException.class,
            () -> service.addVehicle("Honda", "Civic", "b123abc",85000)
        );

        assertEquals(initialCount, service.getVehicles().size());
         Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Vehicle.class));
    }
@Test
void addsVehicleWithGeneratedId() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.save(Mockito.any(Vehicle.class))).thenAnswer(invocation -> {
        Vehicle vehicle = invocation.getArgument(0);
        return new Vehicle(4L, vehicle.getBrand(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage());
        
    });
    VehicleService service = new VehicleService(mockRepository);

    Vehicle created = service.addVehicle(
        "Honda",
        "Civic",
        "B100XYZ",
        85000
    );

    assertEquals(Long.valueOf(4L), created.getId());
    assertEquals("Honda", created.getBrand());
    assertEquals("Civic", created.getModel());
    assertEquals("B100XYZ", created.getLicensePlate());
    assertEquals(Integer.valueOf(85000), created.getMileage());

    Mockito.verify(mockRepository).save(Mockito.any(Vehicle.class));
}@Test
void rejectedDuplicateDoesNotConsumeId() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000)
    ));
    VehicleService service = new VehicleService(mockRepository);

    assertThrows(
        DuplicateLicensePlateException.class,
        () -> service.addVehicle("Honda", "Civic", "b123abc",85000)
    );

 Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Vehicle.class));
}@Test
void deletesExistingVehicle() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.existsById(2L)).thenReturn(true);
    Mockito.doNothing().when(mockRepository).deleteById(2L);
    VehicleService service = new VehicleService(mockRepository);

    boolean deleted = service.deleteVehicle(2L);

    assertTrue(deleted);
    Mockito.verify(mockRepository).deleteById(2L);
}@Test
void rejectsDuplicatePlateWithoutChangingVehicle() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000),
        new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000)
    ));
    VehicleService service = new VehicleService(mockRepository);
    Vehicle masinaVeche = new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000);
Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(masinaVeche));
    assertThrows(
        DuplicateLicensePlateException.class,
        () -> service.updateVehicle(
            2L,
            "Ford",
            "Mondeo",
            "B123ABC", 60000
        )
    );

  Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Vehicle.class));
}@Test
void updatesVehicleKeepingItsOwnLicensePlate() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000),
        new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000)
    ));
    Vehicle masinaVeche = new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000);
    Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(masinaVeche));
    Mockito.when(mockRepository.save(Mockito.any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));
    VehicleService service = new VehicleService(mockRepository);

    Vehicle updated = service.updateVehicle(
        2L,
        "Ford",
        "Fiesta",
        "B456DEF", 60000
    ).orElseThrow();

    assertEquals(Long.valueOf(2L), updated.getId());
    assertEquals("Ford", updated.getBrand());
    assertEquals("Fiesta", updated.getModel());
    assertEquals("B456DEF", updated.getLicensePlate());

 Mockito.verify(mockRepository).save(Mockito.any(Vehicle.class));
}@Test
void rejectsMileageDecreaseWithoutChangingVehicle() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000),
        new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000)
    ));
    Vehicle masinaVeche = new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000);
    Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(masinaVeche));
    VehicleService service = new VehicleService(mockRepository);

    assertThrows(
        MileageDecreaseException.class,
        () -> service.updateVehicle(
            2L,
            "Ford",
            "Fiesta",
            "B456DEF",
            59000
        )
    );

   Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Vehicle.class));
}@Test
void updatesAndStoresIncreasedMileage() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Honda", "Civic", "B123ABC", 85000),
        new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000)
    ));
    Mockito.when(mockRepository.save(Mockito.any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));
    Vehicle masinaVeche = new Vehicle(2L, "Ford", "Focus", "B456DEF", 60000);
Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(masinaVeche));
    VehicleService service = new VehicleService(mockRepository);

    Vehicle updated = service.updateVehicle(
        2L,
        "Ford",
        "Focus",
        "B456DEF",
        65000
    ).orElseThrow();

    assertEquals(Integer.valueOf(65000), updated.getMileage());

  Mockito.verify(mockRepository).save(Mockito.any(Vehicle.class));;
}@Test
void findsVehiclesByBrand() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Dacia", "Logan", "B123ABC", 85000)
    ));
    VehicleService service = new VehicleService(mockRepository);

   List<Vehicle> found = service.getVehiclesByBrand("Dacia");

    assertEquals(1, found.size());
    assertEquals("Logan", found.get(0).getModel());


}
@Test
void findsVehiclesByBrandIgnoringCase() {
    VehicleRepository mockRepository = Mockito.mock(VehicleRepository.class);
    Mockito.when(mockRepository.findAll()).thenReturn(List.of(
        new Vehicle(1L, "Dacia", "Logan", "B123ABC", 85000)
    ));
    VehicleService service = new VehicleService(mockRepository);

   List<Vehicle> found = service.getVehiclesByBrand("dacia");

    assertEquals(1, found.size());
  assertEquals("Dacia", found.get(0).getBrand() );


}
}