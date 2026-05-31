package tw.edu.fju.miniclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class MiniclinicApplication {
    private static final Logger logger = LoggerFactory.getLogger(MiniclinicApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MiniclinicApplication.class, args);
        logger.info("===== Miniclinic 應用程式啟動成功，請存取 http://localhost:8080 =====");
	}
}
