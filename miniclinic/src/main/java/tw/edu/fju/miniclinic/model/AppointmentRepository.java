package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // 依日期篩選掛號
    List<Appointment> findByApptDate(LocalDate apptDate);

    // 依醫師篩選掛號（透過 Doctor 內的 doctorId 查詢）
    List<Appointment> findByDoctor_DoctorId(String doctorId);

    // 依科別分組，計算各科的掛號數
    @Query("SELECT a.doctor.department, COUNT(a) FROM Appointment a GROUP BY a.doctor.department")
    List<Object[]> countAppointmentsByDepartment();
    
    List<Appointment> findByDoctorAndApptDate(Doctor doctor, LocalDate apptDate);

    // 依狀態計算掛號數
    long countByStatus(String status);
}