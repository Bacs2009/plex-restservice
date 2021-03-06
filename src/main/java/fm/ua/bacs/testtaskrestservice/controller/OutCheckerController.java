package fm.ua.bacs.testtaskrestservice.controller;

import fm.ua.bacs.testtaskrestservice.helpers.FTP;
import fm.ua.bacs.testtaskrestservice.helpers.Props;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static fm.ua.bacs.testtaskrestservice.helpers.Helper.FILE_COUNTER;

public class OutCheckerController {

    public void checkOutFolder() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Checking out folder");
                        try {
                            checker();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                5000
        );

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println(FILE_COUNTER + " files already have been proceed");
                    }
                },
                0,
                60000
        );
    }

    private void checker() throws IOException {

        FTP ftp = new FTP();
        Props props = new Props();
        FTPFile[] files = ftp.getFiles(props.getProperties().getProperty("ftp.out"));

        for (FTPFile file : files) {
            if (!file.isDirectory()) {
                if (ftp.downloadFromFTP(props.getProperties().getProperty("ftp.out") + file.getName(), new File(props.getProperties().getProperty("local.ready") + file.getName()))) {
                    ftp.copyFile(props.getProperties().getProperty("ftp.out") + file.getName(), props.getProperties().getProperty("ftp.ready") + file.getName(), true);
                    ftp.copyFile(props.getProperties().getProperty("ftp.ready") + file.getName(), props.getProperties().getProperty("ftp.bkp") + file.getName(), false);
                    fm.ua.bacs.testtaskrestservice.helpers.Response response = new fm.ua.bacs.testtaskrestservice.helpers.Response();
                    System.out.println(response.makeResponse(file.getName(), "File is ready", 200));
                }
            }
        }
    }
}