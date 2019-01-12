package fm.ua.bacs.testtaskrestservice.controller;

import java.util.Timer;
import java.util.TimerTask;

public class OutCheckerController {

    public void checkOutFolder() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Go-go-go");
                    }
                },
                0,
                5000
        );
    }
}

