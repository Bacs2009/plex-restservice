package fm.ua.bacs.testtaskrestservice.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Props {
    public Properties getProperties() throws FileNotFoundException {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("./application.properties");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
}
