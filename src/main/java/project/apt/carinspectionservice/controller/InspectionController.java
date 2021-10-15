package project.apt.carinspectionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
           Inspection i1 = new Inspection(1L,"1VQW871","Geen opmerking",true, LocalDate.now());

            inspectionRepository.save(i1);
            Inspection i2 = new Inspection(2L,"1VCJ543","Banden versleten",false, LocalDate.now());
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
    public List<Inspection> findInspectionByLicensePlateAndInspectionDate(@PathVariable String licensePlate,@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inspectionDate){
        return inspectionRepository.findInspectionByLicensePlateAndInspectionDate(licensePlate,inspectionDate);
    }
    @GetMapping("/inspections/inspection_number/{inspectionNumber}")
    public Inspection findInspectionByInspectionNumber(@PathVariable Long inspectionNumber){
        return inspectionRepository.findInspectionByInspectionNumber(inspectionNumber);
    }
    @PostMapping("/inspections")
    public Inspection addInspection(@RequestBody Inspection inspection){
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
    public ResponseEntity deleteInspection(@PathVariable Long inspectionNumber){
        Inspection inspection = inspectionRepository.findInspectionByInspectionNumber(inspectionNumber);
        if(inspection!=null){
            inspectionRepository.delete(inspection);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
