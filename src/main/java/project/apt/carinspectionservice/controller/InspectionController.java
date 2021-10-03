package project.apt.carinspectionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.apt.carinspectionservice.model.Inspection;
import project.apt.carinspectionservice.repository.InspectionRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class InspectionController {

    @Autowired
    private InspectionRepository inspectionRepository;

    @PostConstruct
    public void fillDB(){
        if(inspectionRepository.count()==0){
            inspectionRepository.save(new Inspection("1VQW871","Geen opmerking",true, LocalDate.of(2021,12,28)));
        }

        System.out.println("Inspections test: " + inspectionRepository.findInspectionsByInspectionDate(LocalDate.of(2021,12,28)).size());
    }

    //@GetMapping("/reviews/user/{userId}")
    //public List<Review> getReviewsByUserId(@PathVariable Integer userId){
     //   return reviewRepository.findReviewsByUserId(userId);
    //}
}
