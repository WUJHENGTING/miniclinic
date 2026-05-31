package tw.edu.fju.miniclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @Column(name = "chart_no", length = 20)
    private String chartNo;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone", length = 20)
    private String phone;

    // JPA 需要無參數的建構子
    public Patient() {}

    public Patient(String chartNo, String name, String gender, LocalDate birthDate, String phone) {
        this.chartNo = chartNo;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // Getters（Spring 會透過這些方法讀取欄位）
    public String getChartNo() { return chartNo; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }

    // Setters（之後會用到）
    public void setChartNo(String chartNo) { this.chartNo = chartNo; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setPhone(String phone) { this.phone = phone; }
}