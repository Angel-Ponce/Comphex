package Comphex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Angel Ponce
 */
public class Project extends File {

    private String projectName;
    private File code;
    private File hex;
    private boolean exist;

    static {
        File rootDir = new File("Projects");
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
    }

    public Project(String name) {
        super("Projects/" + name + "/");
        try {
            if (this.mkdir() || this.exists()) {
                code = new File(this, name + ".code");
                hex = new File(this, name + ".hex");
                if ((code.createNewFile() && hex.createNewFile()) || (code.exists() && hex.exists())) {
                    this.exist = true;
                }
            }
        } catch (IOException ex) {
            this.exist = false;
            System.err.println(ex);
        }
        this.projectName = name;
    }

    public ArrayList<String> open() {
        FileReader fr;
        BufferedReader br;
        ArrayList<String> lines = new ArrayList<>();
        try {
            fr = new FileReader(code);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return lines;
    }

    public boolean save(ArrayList<String> lines) {
        FileWriter fw;
        PrintWriter pw;
        try {
            fw = new FileWriter(code);
            pw = new PrintWriter(fw, true);
            lines.forEach(l -> pw.append(l + "\n"));
            fw.close();
            pw.close();
            return true;
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }

    public String readCode() {
        FileReader fr;
        BufferedReader br;
        String code = "";
        try {
            fr = new FileReader(this.code);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                code += line + "\n";
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return code;
    }

    public String readHex() {
        FileReader fr;
        BufferedReader br;
        String code = "";
        try {
            fr = new FileReader(this.hex);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                code += line + "\n";
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return code;
    }

    public boolean projectExist() {
        return exist;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public File getCode() {
        return code;
    }

    public void setCode(File code) {
        this.code = code;
    }

    public File getHex() {
        return hex;
    }

    public void setHex(File hex) {
        this.hex = hex;
    }

    @Override
    public String toString() {
        return projectName;
    }
}
