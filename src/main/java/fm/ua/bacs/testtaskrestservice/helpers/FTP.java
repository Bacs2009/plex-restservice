package fm.ua.bacs.testtaskrestservice.helpers;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import static fm.ua.bacs.testtaskrestservice.helpers.Helper.FILE_COUNTER;

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

    public boolean copyFile(String from, String to, Boolean remove) throws IOException {
        FTPClient ftpClient = getClient();
        FTPClient ftpClient2 = getClient();
        Props props = new Props();

        InputStream stream = ftpClient.retrieveFileStream(from);
        if (ftpClient2.storeFile(to, stream)) {
            ftpClient.logout();
            ftpClient.disconnect();
            ftpClient2.logout();
            ftpClient2.disconnect();
            FILE_COUNTER++;
            if (remove) {
                deleteFile(from);
            }
            return true;
        }

        return false;
    }

    private void deleteFile(String file) throws IOException {
        FTPClient ftpClient = getClient();

        if (ftpClient.deleteFile(file)) {
            ftpClient.logout();
            ftpClient.disconnect();
        }

    }

    private FTPClient getClient() throws IOException {
        Props props = new Props();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(props.getProperties().getProperty("ftp.server"), Integer.parseInt(props.getProperties().getProperty("ftp.port")));
        ftpClient.login(props.getProperties().getProperty("ftp.user"), props.getProperties().getProperty("ftp.pass"));
        ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public boolean uploadToFTP(File file) throws IOException {
        Props props = new Props();
        FTPClient ftpClient = getClient();
        FileInputStream fis = null;

        fis = new FileInputStream(file);
        return ftpClient.storeFile(props.getProperties().getProperty("ftp.out") + file.getName(), fis);

    }
}
