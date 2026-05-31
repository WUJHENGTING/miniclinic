package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PasswordForm;

@Controller
public class PasswordController {

    @Autowired
    private DoctorRepository doctorRepo;

    @GetMapping("/password")
    public String passwordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "password";
    }

    @PostMapping("/password")
    public String updatePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        if (result.hasErrors()) { return "password"; }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.passwordForm", "新密碼與確認密碼不一致");
            return "password";
        }
        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");
        Doctor doctor = doctorRepo.findById(loggedInDoctorId).orElse(null);
        if (doctor == null || doctor.getPasswordHash() == null || !BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.rejectValue("oldPassword", "error.passwordForm", "舊密碼錯誤");
            return "password";
        }
        doctor.setPasswordHash(BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt()));
        doctorRepo.save(doctor);
        model.addAttribute("successMessage", "密碼修改成功！");
        return "password";
    }
}