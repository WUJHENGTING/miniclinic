package tw.edu.fju.miniclinic.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {
        return args -> {
            logger.info("===== 正在檢查資料庫初始化狀態... =====");
            
            // 暫時改為 true：強制每次啟動都清空並重建資料，確保不會讀到舊的亂碼
            if (true) {
                logger.info("檢測到舊的測試資料，正在清除並重新寫入完整資料...");

                // 注意刪除順序：先刪除有關聯的預約，再刪除醫生和病患，避免外鍵衝突
                appointmentRepository.deleteAll();
                patientRepository.deleteAll();
                doctorRepository.deleteAll();

                // 1. 建立並儲存 5 位醫生
                Doctor doc1 = new Doctor("D001", "陳志明醫師", "家醫科", "一般內科、慢性病管理");
                Doctor doc2 = new Doctor("D002", "林佩君醫師", "內科", "心臟血管、高血壓");
                Doctor doc3 = new Doctor("D003", "王建華醫師", "復健科", "運動傷害、脊椎復健");
                Doctor doc4 = new Doctor("D004", "李美玲醫師", "小兒科", "兒童感冒、疫苗接種");
                Doctor doc5 = new Doctor("D005", "張雅筑醫師", "身心科", "焦慮、失眠、情緒調適");
                doctorRepository.saveAll(Arrays.asList(doc1, doc2, doc3, doc4, doc5));

                // 2. 建立並儲存 3 位病患
                Patient pat1 = new Patient("TEST00001", "測試病患甲", "男", LocalDate.parse("1985-03-15"), "0912-345-678");
                Patient pat2 = new Patient("TEST00002", "王小明", "男", LocalDate.parse("1990-07-22"), "0923-456-789");
                Patient pat3 = new Patient("TEST00003", "李小華", "女", LocalDate.parse("1988-11-30"), "0934-567-890");
                patientRepository.saveAll(Arrays.asList(pat1, pat2, pat3));

                // 3. 建立並儲存 3 筆預約
                Appointment appt1 = new Appointment();
                appt1.setDoctor(doc1); appt1.setPatient(pat1);
                appt1.setApptDate(LocalDate.now()); appt1.setTimeSlot("上午"); appt1.setStatus("BOOKED");

                Appointment appt2 = new Appointment();
                appt2.setDoctor(doc2); appt2.setPatient(pat2);
                appt2.setApptDate(LocalDate.now()); appt2.setTimeSlot("上午"); appt2.setStatus("BOOKED");

                Appointment appt3 = new Appointment();
                appt3.setDoctor(doc3); appt3.setPatient(pat3);
                appt3.setApptDate(LocalDate.now()); appt3.setTimeSlot("下午"); appt3.setStatus("BOOKED");

                appointmentRepository.saveAll(Arrays.asList(appt1, appt2, appt3));

                logger.info("===== 測試資料已成功寫入資料庫 =====");
            }

            // 強制將所有醫師的密碼重設為正確的 pass1234 雜湊 (移除 == null 判斷，覆寫原本可能無效的舊資料)
            List<Doctor> doctors = doctorRepository.findAll();
            for (Doctor d : doctors) {
                d.setPasswordHash(BCrypt.hashpw("pass1234", BCrypt.gensalt()));
            }
            doctorRepository.saveAll(doctors);
            logger.info("===== 已強制為所有醫師重設預設密碼 'pass1234' =====");
        };
    }
}