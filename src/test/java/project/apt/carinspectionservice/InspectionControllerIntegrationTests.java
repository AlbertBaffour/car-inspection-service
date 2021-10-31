package project.apt.carinspectionservice;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.apt.carinspectionservice.model.Inspection;
import project.apt.carinspectionservice.repository.InspectionRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
 class InspectionControllerIntegrationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InspectionRepository inspectionRepository;

    private Inspection inspection1= new Inspection(1L,"1VQW871","banden versleten",false, LocalDate.now().minusDays(2));
    private Inspection inspection2= new Inspection(2L,"1VQW871","Geen opmerking",true, LocalDate.now());
    private Inspection inspection3= new Inspection(3L,"1ABC871","koplamp stuk",false, LocalDate.now().minusDays(2));
    private Inspection inspection4= new Inspection(4L,"1ABC871","Geen opmerking",true, LocalDate.now());

    @BeforeEach
     void beforeAllTests() {
        inspectionRepository.deleteAll();
        inspectionRepository.save(inspection1);
        inspectionRepository.save(inspection2);
        inspectionRepository.save(inspection3);
        inspectionRepository.save(inspection4);
    }
    @AfterEach
     void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        inspectionRepository.deleteAll();
    }

     ObjectMapper defaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    private ObjectMapper mapper =defaultMapper();
    @Test
     void givenInspection_whenGetInspections_thenReturnJsonInspections() throws Exception {

        mockMvc.perform(get("/inspections"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].inspectionNumber",is(1)))
                .andExpect(jsonPath("$[0].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[0].passed",is(false)))
                .andExpect(jsonPath("$[1].inspectionNumber",is(2)))
                .andExpect(jsonPath("$[1].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[1].passed",is(true)));
    }
    @Test
     void givenInspection_whenGetInspectionByInspectionNr_thenReturnJsonInspection() throws Exception {

        mockMvc.perform(get("/inspections/inspection_number/{inspectionNumber}",2L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inspectionNumber",is(2)))
                .andExpect(jsonPath("$.licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$.passed",is(true)));
    }

    @Test
     void givenInpsection_whenGetReviewByPlate_thenReturnJsonInspection() throws Exception {

        mockMvc.perform(get("/inspections/license_plate/{licensePlate}", "1VQW871"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate", is("1VQW871")));

    }
    @Test
     void givenInpsection_whenGetReviewByPlateAndDate_thenReturnJsonInspection() throws Exception {

        mockMvc.perform(get("/inspections/license_plate/{licensePlate}/inspection_date/{inspectionDate}", "1VQW871",LocalDate.now() ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate", is("1VQW871")))
                .andExpect(jsonPath("$[0].inspectionDate", is(LocalDate.now().toString())))
                .andExpect(jsonPath("$[0].passed", is(true)));
    }

    @Test
     void whenPostReview_thenReturnJsonReview() throws Exception {

    Inspection inspection = new Inspection(5L,"1VCJ854","all good",true,LocalDate.now());

        mockMvc.perform(post("/inspections")
                .content(mapper.writeValueAsString(inspection))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.inspectionNumber",is(5)))
            .andExpect(jsonPath("$.licensePlate",is("1VCJ854")))
            .andExpect(jsonPath("$.passed",is(true)))
            .andExpect(jsonPath("$.comment",is("all good")));
}

    @Test
     void givenReview_whenPutReview_thenReturnJsonReview() throws Exception {

        Inspection updatedInspection =  new Inspection(2L,"1VCJ854","not good",false, LocalDate.now());

        mockMvc.perform(put("/inspections")
                .content(mapper.writeValueAsString(updatedInspection))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passed",is(false)))
                .andExpect(jsonPath("$.comment",is("not good")))
                .andExpect(jsonPath("$.inspectionNumber",is(2)));

    }

    @Test
     void givenReview_whenDeleteReview_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/inspections/inspection_number/{inspectionNumber}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void givenNoInspection_whenDeleteInspection_thenStatusNotFound() throws Exception{

        mockMvc.perform(delete("/inspections/inspection_number/{inspectionNumber}",9L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
