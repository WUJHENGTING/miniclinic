package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    // 根據科別尋找醫生
    List<Doctor> findByDepartment(String department);

    // 找出所有不重複的科別，給前端下拉選單使用
    @Query("SELECT DISTINCT d.department FROM Doctor d ORDER BY d.department")
    List<String> findAllDepartments();
}