package ro.robert.autoservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
public record CreateVehicleRequest(

    @NotBlank(message = "Marca este obligatorie")
    String brand,

    @NotBlank(message = "Modelul este obligatoriu")
    String model,

    @NotBlank(message = "Numarul de inmatriculare este obligatoriu")
    String licensePlate,
    @NotNull(message = "Kilometrajul este obligatoriu")
@PositiveOrZero(message = "Kilometrajul nu poate fi negativ")
Integer mileage

){
}
