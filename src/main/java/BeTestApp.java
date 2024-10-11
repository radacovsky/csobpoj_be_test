import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cz.softec"})
public class BeTestApp {
    public static void main(String[] args) {
        SpringApplication.run(BeTestApp.class, args);
    }
}
