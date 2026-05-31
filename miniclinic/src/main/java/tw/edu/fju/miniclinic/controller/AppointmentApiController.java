package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class AppointmentApiController {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/api/appointments/count")
    public long getAppointmentCount() {
        return appointmentRepo.count();
    }

    @GetMapping("/api/appointments")
    public List<Appointment> getAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String doctorId) {
        
        if (date != null) return appointmentRepo.findByApptDate(date);
        if (doctorId != null) return appointmentRepo.findByDoctor_DoctorId(doctorId);
        return appointmentRepo.findAll();
    }
    @PutMapping("/api/appointments/{apptId}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long apptId,
            @RequestBody Map<String, String> payload,
            HttpSession session) {

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");

        Appointment appt = appointmentRepo.findById(apptId).orElse(null);
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }

        // 只能修改自己的掛號
        if (!appt.getDoctor().getDoctorId().equals(loggedInDoctorId)) {
            return ResponseEntity.status(403).build();
        }

        String newStatus = payload.get("status");
        if (!List.of("BOOKED", "COMPLETED", "CANCELLED").contains(newStatus)) {
            return ResponseEntity.badRequest().build();
        }

        appt.setStatus(newStatus);
        return ResponseEntity.ok(appointmentRepo.save(appt));
    }
}