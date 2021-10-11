package project.apt.carinspectionservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import project.apt.carinspectionservice.model.Inspection;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InspectionRepository extends MongoRepository<Inspection, String> {
    List<Inspection> findInspectionsByLicensePlate(String licensePlate);
    List<Inspection> findInspectionByLicensePlateAndInspectionDate(String licensePlate, LocalDate inspectionDate);
    Inspection findInspectionByLicensePlateAndInspectionDateAndPassed(String licensePlate, LocalDate inspectionDate,Boolean passed);
    Inspection findInspectionByInspectionNumber(Integer inspectionNumber);

}
