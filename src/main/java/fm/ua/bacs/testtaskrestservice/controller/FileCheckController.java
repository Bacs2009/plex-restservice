package fm.ua.bacs.testtaskrestservice.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;

@RestController
public class FileCheckController {

    @GetMapping("/")
    public String helloWorld() {

        return "Hello, world";
    }

    @GetMapping("/filescan")
    public Response fileScanner(@RequestParam(name = "filename") String filename) throws IOException {

        return searchFile(filename, "199350");
    }

    private Response searchFile(String filename, String search) throws IOException {
        Collection<File> all = new ArrayList<>();
        addTree(new File("C:\\Users\\Bacs\\Downloads\\rest-service-master\\in"), all);

        String message = "File not found";
        int status = 404;
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonarray = new JsonArray();

        jsonObj.addProperty("filename", filename);
        jsonarray.add(jsonObj);

        JsonObject jsonMain = new JsonObject();
        jsonMain.add("Search", jsonarray);

        for (File file : all) {
            if (file.getName().equals(filename)) {
                if (searchInFile(file, search)) {
                    copyFile(file, new File("C:\\Users\\Bacs\\Downloads\\rest-service-master\\out\\" + filename));
                    message = "File has been found and string " + search + " was found. File has been copied to 'out' directory";
                    status = 200;
                } else {
                    message = "File has been found but string " + search + " was not found";
                    status = 404;
                }
            }
        }

        jsonObj.addProperty("message", message);

        return Response.status(status).entity(jsonMain.toString()).build();
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

    private void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                all.add(child);
                addTree(child, all);
            }
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source;
        FileChannel destination;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        destination.close();

    }
}