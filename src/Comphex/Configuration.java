package Comphex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Configuration implements Serializable {

    private String theme;

    private int fontSize;

    public static final String FILE_CONFIG = "config.props";

    static {
        File fileConfig = new File(FILE_CONFIG);
        if (!fileConfig.exists()) {
            try {
                fileConfig.createNewFile();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public Configuration(String theme, int fontSize) {
        this.theme = theme;
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public static void config(Configuration c) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("config.props")));
            oos.writeObject(c);
            oos.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static Configuration load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("config.props")));
            return (Configuration) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }
}
