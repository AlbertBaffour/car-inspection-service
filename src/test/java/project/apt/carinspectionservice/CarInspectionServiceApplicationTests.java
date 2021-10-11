package project.apt.carinspectionservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import project.apt.carinspectionservice.model.Inspection;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CarInspectionServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void getInspectionInfo_happy(){
        final Inspection inspection= new Inspection(1L,"1VQW871","Geen opmerking",true, LocalDate.of(2021,9,28));
        assertThat(inspection).isNotNull();
        assertThat(inspection.getComment()).isEqualTo("Geen opmerking");
        assertThat(inspection.getPassed()).isEqualTo(true);
        assertThat(inspection.getInspectionDate()).isEqualTo(LocalDate.of(2021,9,28));
    }
    @Test
    void getSet_happy(){
        final Inspection inspection= new Inspection();
        inspection.setInspectionNumber(3L);
        inspection.setComment("Alles in orde");
        inspection.setPassed(true);
        assertThat(inspection).isNotNull();
        assertThat(inspection.getInspectionNumber()).isEqualTo(3L);
        assertThat(inspection.getComment()).isEqualTo("Alles in orde");
        assertThat(inspection.getPassed()).isEqualTo(true);
    }


}
