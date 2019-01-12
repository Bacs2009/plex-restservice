package fm.ua.bacs.testtaskrestservice;

import fm.ua.bacs.testtaskrestservice.controller.OutCheckerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestTaskRestserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskRestserviceApplication.class, args);

        OutCheckerController outCheckerController = new OutCheckerController();
        Thread thread = new Thread(outCheckerController::checkOutFolder);
        thread.start();
    }
}