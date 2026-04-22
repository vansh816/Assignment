package z_project3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "z_project3")
@EnableScheduling //phase 3
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
