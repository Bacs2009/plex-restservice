package fm.ua.bacs.testtaskrestservice.controller;

import fm.ua.bacs.testtaskrestservice.helpers.FTP;
import fm.ua.bacs.testtaskrestservice.helpers.Helper;
import fm.ua.bacs.testtaskrestservice.helpers.Props;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RestController
public class FileCheckController {

    @GetMapping("/filescan")
    public Response fileScanner(@RequestParam(name = "filename") String filename) throws IOException {

        return searchFile(filename, "199350");
    }

    private Response searchFile(String filename, String search) throws IOException {
        Helper helper = new Helper();
        FTP ftp = new FTP();

        String message = "File not found";
        int status = 404;

/*-----------------------        Code for working with local folders ----------------------------------*/
        Props props = new Props();
        Collection<File> all = new ArrayList<>();
        helper.addTree(new File(props.getProperties().getProperty("local.in")), all);

        for (File file : all) {
            if (file.getName().equals(filename)) {
                if (searchInFile(file, search)) {
                    if (ftp.uploadToFTP(file)) {
                        message = "File has been found, checked and uploaded to server";
                        status = 200;
                    }
                } else {
                    message = "File has been found but string " + search + " was not found";
                    status = 404;
                }
            }
        }

        fm.ua.bacs.testtaskrestservice.helpers.Response response = new fm.ua.bacs.testtaskrestservice.helpers.Response();
        return response.makeResponse(filename, message, status);
    }

    private boolean searchInFile(File filename, String search) {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.equals(search)) {
                    System.out.println(sCurrentLine);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }
}