package tw.edu.fju.miniclinic.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 作業要求：開放 /api/stats 讓它不需要登入即可存取
        if (request.getRequestURI().equals("/api/stats")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("loggedInDoctorId") == null) {
            if (request.getRequestURI().startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 回傳 401
            } else {
                response.sendRedirect("/login"); // 頁面重新導向
            }
            return false;
        }
        return true;
    }
}