package ro.robert.autoservice;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; 
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc
class VehicleApiTest {
   
    @Autowired 
    private MockMvc mockMvc;

    @MockBean 
    private VehicleService vehicleService;

    @Test
    void returnsBadRequestWhenMileageIsNegative() throws Exception {
        mockMvc.perform(
            post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "brand": "Honda",
                        "model": "Civic",
                        "licensePlate": "B100XYZ",
                        "mileage": -100
                    }
                    """)
        ).andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.mileage").value(
             "Kilometrajul nu poate fi negativ"
         ));
    }

    @Test
    void returnsConflictWhenMileageDecreases() throws Exception {
        Mockito.when(vehicleService.updateVehicle(
            Mockito.eq(2L), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.eq(59000)
        )).thenThrow(new MileageDecreaseException(60000, 59000));

        mockMvc.perform( 
            put("/api/vehicles/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "brand": "Ford",
                        "model": "Focus",
                        "licensePlate": "B456DEF",
                        "mileage": 59000
                    }
                    """)
       ).andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(
            "Kilometrajul nu poate scadea de la 60000 la 59000"
        ));
    }
}