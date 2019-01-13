package fm.ua.bacs.testtaskrestservice.helpers;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class FTP {
    public FTPFile[] getFiles(String dir) throws IOException {
        FTPClient ftpClient = getClient();
        // lists files and directories in the current working directory
        FTPFile[] files = ftpClient.listFiles(dir);

        ftpClient.logout();
        ftpClient.disconnect();

        return files;
    }

    public Boolean searchInFTPFile(String filename, String search) throws IOException {
        Props props = new Props();
        FTPClient ftpClient = getClient();
        InputStream stream = ftpClient.retrieveFileStream(props.getProperties().getProperty("ftp.in") + filename);
        Scanner sc = new Scanner(stream);

        //Reading the file line by line and printing the same
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (Objects.equals(line, search)) {
                System.out.println(line + " = " + search);
                return true;
            }
        }

        ftpClient.logout();
        ftpClient.disconnect();
        return false;
    }

    public void copyFile(String from, String to) throws IOException {
        FTPClient ftpClient = getClient();
        FTPClient ftpClient2 = getClient();

        InputStream stream = ftpClient.retrieveFileStream(from);
        ftpClient2.storeFile(to, stream);

        ftpClient.logout();
        ftpClient.disconnect();
        ftpClient2.disconnect();
        ftpClient2.disconnect();
    }

    public void moveFile(String from, String to) throws IOException {
        FTPClient ftpClient = getClient();

        InputStream stream = ftpClient.retrieveFileStream(from);
        ftpClient.storeFile(to, stream);

        ftpClient.logout();
        ftpClient.disconnect();
    }

    private FTPClient getClient() throws IOException {
        Props props = new Props();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(props.getProperties().getProperty("ftp.server"), Integer.parseInt(props.getProperties().getProperty("ftp.port")));
        ftpClient.login(props.getProperties().getProperty("ftp.user"), props.getProperties().getProperty("ftp.pass"));
        return ftpClient;
    }
}
