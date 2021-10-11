package project.apt.carinspectionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.apt.carinspectionservice.model.Inspection;
import project.apt.carinspectionservice.repository.InspectionRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@RestController
public class InspectionController {

    @Autowired
    private InspectionRepository inspectionRepository;

    @PostConstruct
    public void fillDB(){
        if(inspectionRepository.count()==0){
           Inspection i1 = new Inspection("1VQW871","Geen opmerking",true, LocalDate.of(2021,9,28));
           i1.setInspectionNumber(1);
            inspectionRepository.save(i1);
            Inspection i2 = new Inspection("1VCJ543","Banden versleten",false, LocalDate.of(2021,9,30));
            i2.setInspectionNumber(2);
            inspectionRepository.save(i2);
        }

        System.out.println("Inspections test: " + inspectionRepository.findAll().size());
    }

    @GetMapping("/inspections")
    public List<Inspection> findAll(){
        return inspectionRepository.findAll();
    }
    @GetMapping("/inspections/license_plate/{licensePlate}")
    public List<Inspection> findInspectionsByLicensePlate(@PathVariable String licensePlate){
        return inspectionRepository.findInspectionsByLicensePlate(licensePlate);
    }
    @GetMapping("/inspections/license_plate/{licensePlate}/inspection_date/{inspectionDate}")
    public List<Inspection> findInspectionByLicensePlateAndInspectionDate(@PathVariable String licensePlate,@PathVariable LocalDate inspectionDate){
        return inspectionRepository.findInspectionByLicensePlateAndInspectionDate(licensePlate,inspectionDate);
    }
    @GetMapping("/inspections/license_plate/{licensePlate}/inspection_date/{inspectionDate}/passed/{passed}")
    public Inspection findInspectionByLicensePlateAndInspectionDateAndPassed(@PathVariable String licensePlate,@PathVariable LocalDate inspectionDate,@PathVariable Boolean passed){
        return inspectionRepository.findInspectionByLicensePlateAndInspectionDateAndPassed(licensePlate,inspectionDate, passed);
    }
    @PostMapping("/inspections")
    public Inspection addInspection(@RequestBody Inspection inspection){
        Integer inspection_number= inspectionRepository.findAll().size()+1;
        inspection.setInspectionNumber(inspection_number);
        inspectionRepository.save(inspection);
        return inspection;
    }
    @PutMapping("/inspections")
    public Inspection updateInspection(@RequestBody Inspection updatedInspection){
        Inspection retrievedInspection = inspectionRepository.findInspectionByInspectionNumber(updatedInspection.getInspectionNumber());
        retrievedInspection.setComment(updatedInspection.getComment());
        retrievedInspection.setPassed(updatedInspection.getPassed());
        retrievedInspection.setInspectionDate(updatedInspection.getInspectionDate());
        inspectionRepository.save(retrievedInspection);
        return retrievedInspection;
    }
    @DeleteMapping("/inspection/inspection_number/{inspectionNumber}")
    public ResponseEntity deleteInspection(@PathVariable Integer inspectionNumber){
        Inspection inspection = inspectionRepository.findInspectionByInspectionNumber(inspectionNumber);
        if(inspection!=null){
            inspectionRepository.delete(inspection);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
