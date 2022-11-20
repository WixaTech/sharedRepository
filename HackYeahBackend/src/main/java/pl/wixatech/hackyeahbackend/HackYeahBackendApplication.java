package pl.wixatech.hackyeahbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackYeahBackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(HackYeahBackendApplication.class, args);
  }

}
