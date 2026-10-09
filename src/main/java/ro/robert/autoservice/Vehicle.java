package ro.robert.autoservice;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

 @Entity
public class Vehicle {
@Id @GeneratedValue 
    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private Integer mileage;
    
    protected Vehicle() {
        // Default constructor for JPA
    }

 public Vehicle(Long id, String brand, String model, String licensePlate, Integer mileage) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
    }


    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Integer getMileage() {
    return mileage;
}
}