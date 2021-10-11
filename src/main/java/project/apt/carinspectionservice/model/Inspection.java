package project.apt.carinspectionservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "inspections")
public class Inspection {
    @Id
    private String id;
    private Long inspectionNumber;
    private String licensePlate;
    private String comment;
    private Boolean passed;
    private LocalDate inspectionDate;

    public Inspection() {
    }

    public Inspection(Long inspectionNumber,String licensePlate, String comment, Boolean passed, LocalDate inspectionDate) {
        setInspectionNumber(inspectionNumber);
        setLicensePlate(licensePlate);
         setComment(comment);
         setPassed(passed);
         setInspectionDate(inspectionDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public Long getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(Long inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }
}
