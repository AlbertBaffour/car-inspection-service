package project.apt.carinspectionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.apt.carinspectionservice.model.Inspection;
import project.apt.carinspectionservice.repository.InspectionRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InspectionControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InspectionRepository inspectionRepository;


     ObjectMapper defaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    private ObjectMapper mapper =defaultMapper();

    @Test
     void givenInspection_whenGetInspections_thenReturnJsonInspections() throws Exception {
        Inspection inspection1 = new Inspection(444L,"1VQW871","lamp stuk",false,LocalDate.now());
        Inspection inspection2 = new Inspection(555L,"1VQW871","Geen opmerking",true, LocalDate.now());

        List<Inspection> inspectionList = new ArrayList<>();
        inspectionList.add(inspection1);
        inspectionList.add(inspection2);

        given(inspectionRepository.findAll()).willReturn(inspectionList);

        mockMvc.perform(get("/inspections"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].inspectionNumber",is(444)))
                .andExpect(jsonPath("$[0].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[0].passed",is(false)))
                .andExpect(jsonPath("$[1].inspectionNumber",is(555)))
                .andExpect(jsonPath("$[1].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[1].passed",is(true)));
    }
    @Test
     void givenInspection_whenGetInspectionByInspectionNr_thenReturnJsonInspection() throws Exception {
        Inspection inspection = new Inspection(888L,"1VQW871","Geen opmerking",true, LocalDate.now());

        given(inspectionRepository.findInspectionByInspectionNumber(888L)).willReturn(inspection);

        mockMvc.perform(get("/inspections/inspection_number/{inspectionNumber}",888L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inspectionNumber",is(888)))
                .andExpect(jsonPath("$.licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$.passed",is(true)));
    }

    @Test
     void givenInspection_whenGetInspectionsByLicensePlate_thenReturnJsonInspections() throws Exception {
        Inspection inspection1 = new Inspection(444L,"1VQW871","lamp stuk",false,LocalDate.now());
        Inspection inspection2 = new Inspection(555L,"1VQW871","Geen opmerking",true, LocalDate.now());

        List<Inspection> inspectionList = new ArrayList<>();
        inspectionList.add(inspection1);
        inspectionList.add(inspection2);

        given(inspectionRepository.findInspectionsByLicensePlate("1VQW871")).willReturn(inspectionList);

        mockMvc.perform(get("/inspections/license_plate/{licensePlate}","1VQW871"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].inspectionNumber",is(444)))
                .andExpect(jsonPath("$[0].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[0].passed",is(false)))
                .andExpect(jsonPath("$[1].inspectionNumber",is(555)))
                .andExpect(jsonPath("$[1].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[1].passed",is(true)));
    }
    @Test
     void givenInspection_whenGetByLicensePlateAndInspectionDate_thenReturnJsonInspections() throws Exception {
        Inspection inspection1 = new Inspection(333L,"1VQW871","lamp stuk",false,LocalDate.now());

        List<Inspection> inspectionList = new ArrayList<>();
        inspectionList.add(inspection1);

        given(inspectionRepository.findInspectionByLicensePlateAndInspectionDate("1VQW871",LocalDate.now())).willReturn(inspectionList);

        mockMvc.perform(get("/inspections/license_plate/{licensePlate}/inspection_date/{inspectionDate}","1VQW871", LocalDate.now()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].inspectionNumber",is(333)))
                .andExpect(jsonPath("$[0].licensePlate",is("1VQW871")))
                .andExpect(jsonPath("$[0].passed",is(false)));

    }
    @Test
     void whenPostInspection_thenReturnJsonInspection() throws Exception{
        Inspection inspection = new Inspection(333L,"1VCJ854","all good",true,LocalDate.now());

        mockMvc.perform(post("/inspections")
                .content(mapper.writeValueAsString(inspection))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inspectionNumber",is(333)))
                .andExpect(jsonPath("$.licensePlate",is("1VCJ854")))
                .andExpect(jsonPath("$.passed",is(true)))
                .andExpect(jsonPath("$.comment",is("all good")));
    }

    @Test
     void givenInspection_whenPutInspection_thenReturnJsonInspection() throws Exception{
        Inspection inspection = new Inspection(999L,"1VCJ854","all good",true,LocalDate.now());

        given(inspectionRepository.findInspectionByInspectionNumber(999L)).willReturn(inspection);

        Inspection updatedInspection =  new Inspection(999L,"1VCJ854","not good",false,LocalDate.now());

        mockMvc.perform(put("/inspections")
                .content(mapper.writeValueAsString(updatedInspection))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passed",is(false)))
                .andExpect(jsonPath("$.comment",is("not good")))
                .andExpect(jsonPath("$.inspectionNumber",is(999)));
    }
    @Test
     void givenInspection_whenDeleteInspection_thenStatusOk() throws Exception{
        Inspection inspectionToBeDeleted = new Inspection(111L,"1VCJ854","all good",true,LocalDate.now());

        given(inspectionRepository.findInspectionByInspectionNumber(111L)).willReturn(inspectionToBeDeleted);

        mockMvc.perform(delete("/inspection/inspection_number/{inspectionNumber}",111L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void givenNoInspection_whenDeleteInspection_thenStatusNotFound() throws Exception{
        given(inspectionRepository.findInspectionByInspectionNumber(111L)).willReturn(null);

        mockMvc.perform(delete("/inspection/inspection_number/{inspectionNumber}",222L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
