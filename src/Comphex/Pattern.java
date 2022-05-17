package Comphex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.BiConsumer;
import javax.swing.text.SimpleAttributeSet;

public class Pattern {

    public static final String PROJECT_NAME = "\\w+";

    public static final String[] PATTERNS = new String[]{
        "trisa\\s*=\\s*(0|1)",
        "trisb\\s*=\\s*(0|1)",
        "porta\\s*=\\s*(0|1)",
        "portb\\s*=\\s*(0|1)",
        "porta\\.[0-7]\\s*=\\s*(0|1)",
        "portb\\.[0-7]\\s*=\\s*(0|1)",
        "device\\s+[a-z0-9]+",
        "end",
        "goto\\s+([a-z]|_|\\d)?[a-z]\\w*",
        "([a-z]|_|\\d)?[a-z]\\w*:",
        "portb\\s*=\\s*%(0|1){8}",
        "dim\\s+([a-z]|_|\\d)?[a-z]\\w*\\s+as\\s+byte"
    };

    public static final String[] ERRORS_PATTERNS = new String[]{
        "trisa\\s*=\\s*.*",
        "trisb\\s*=\\s*.*",
        "porta\\s*=\\s*.*",
        "portb\\s*=\\s*(?!%.)*",
        "porta\\.\\d+\\s*=\\s*.*",
        "portb\\.\\d+\\s*=\\s*.*",
        "device\\s*.*",
        "end\\s*.+",
        "goto\\s*.*",
        "(\\w|[^\\w])+:.*",
        "portb\\s*=\\s*%.*",
        "dim.*"
    };

    public static final String[] ERRORS_MESSAGES = new String[]{
        "Bad assignament, the valid expression follow trisa = [0-1]",
        "Bad assignament, the valid expression follow trisb = [0-1]",
        "Bad assignament, the valid expression follow porta = [0-1]",
        "Bad assignament, the valid expression follow portb = [0-1]",
        "Bad annotation expression, the valid expression follow porta.[0-7] = [0-1]",
        "Bad annotation expression, the valid expression follow portb.[0-7] = [0-1]",
        "Bad device declaration, the valid expression follow device [a-zA-Z0-9]+",
        "Bad end file, the valid expression follow end",
        "Bad annotation expression, the valid expression follow goto([a-z]|_|\\d)?[a-z]\\w*",
        "Bad label declaration, the valid expression follow ([a-z]|_|\\d)?[a-z]\\w*:",
        "Bad annotation expression, the valid expression follow portb = %(0|1){8}",
        "Bad variable declaration, the valid expression follow dim\\s+([a-z]|_|\\d)?[a-z]\\w*\\s+as\\s+byte"
    };

    public static final String[] RESERVED_KEYS = new String[]{
        "trisa",
        "trisb",
        "porta",
        "portb",
        "device",
        "16f628a",
        "end",
        "goto",
        "dim",
        "as",
        "byte"
    };

    public static final String COMMENT = ";.*";

    public static void compile(Project currentProject, String code, BiConsumer<String, SimpleAttributeSet> addToConsole) {
        long startTime = System.nanoTime();
        ArrayList<String> validTokens = new ArrayList<>();
        String rawCode = code;
        String[] tokens = rawCode.split("\n");
        boolean breaked = false;
        ArrayList<String> labels = new ArrayList();
        int i = 0;
        tk:
        for (String token : tokens) {
            boolean fine = false;
            token = token.replaceAll(";.*", "");
            token = token.trim();
            token = token.toLowerCase();
            if (!token.isEmpty() && !token.isBlank()) {
                pt:
                for (String pattern : PATTERNS) {
                    if (token.matches(pattern)) {
                        if (pattern.equals("end")) {
                            break tk;
                        }
                        if (pattern.equals("([a-z]|_|\\d)?[a-z]\\w*:")) {
                            String label = token.replace(":", "");
                            if (labels.stream().anyMatch(l -> l.equals(label))) {
                                breaked = true;
                                addToConsole.accept("Fatal error, the label \"" + label + "\" already exist!", Comphex.ERROR_SAS);
                                break tk;
                            }
                            for (String rk : RESERVED_KEYS) {
                                if (label.matches(rk)) {
                                    breaked = true;
                                    addToConsole.accept("Fatal error, the label \"" + label + "\" is a reserved key!", Comphex.ERROR_SAS);
                                    break tk;
                                }
                            }
                            labels.add(label);
                        }
                        if (pattern.equals("goto\\s+([a-z]|_|\\d)?[a-z]\\w*")) {
                            String label = token.replace("goto", "").trim();
                            ArrayList<String> arrTokens = new ArrayList<>(Arrays.asList(tokens));
                            if (!arrTokens.stream().anyMatch(at -> {
                                at = at.replaceAll(";.*", "");
                                at = at.trim();
                                at = at.toLowerCase();
                                return at.matches(label + ":");
                            })) {
                                breaked = true;
                                addToConsole.accept("Fatal error, label \"" + label + "\" not found", Comphex.ERROR_SAS);
                                break tk;
                            }
                        }
                        validTokens.add(token);
                        fine = true;
                        break;
                    }
                }
            } else {
                fine = true;
            }
            if (!fine) {
                addToConsole.accept(errors(token, i + 1), Comphex.ERROR_SAS);
            }
            if (!breaked && !fine) {
                breaked = !fine;
            }
            i++;
        }
        if (!breaked) {
            generateHex(validTokens, currentProject.getHex());
            //First comprobe if some token is not a device instruction
            if (validTokens.stream().filter(token -> token.matches("device\\s+[a-z0-9]+")).count() == 0) {
                addToConsole.accept("Warning! not device declared, asume PIC 16F628A", Comphex.WARNING_SAS);
            }
            if (validTokens.stream().filter(token -> token.matches("device\\s+[a-z0-9]+")).count() > 1) {
                addToConsole.accept("Warning! 2 device declared, asume the first device", Comphex.WARNING_SAS);
            }
            addToConsole.accept("Compiled hex code at: ", null);
            addToConsole.accept(currentProject.getHex().getAbsolutePath(), Comphex.UNDERLINE_SAS);
            addToConsole.accept("Code compiled successfully", Comphex.SUCCESS_SAS);
        }
        long endTime = System.nanoTime();
        addToConsole.accept("Program \"" + currentProject.getProjectName() + "\" Compiled at: \n~" + new Date(), null);
        addToConsole.accept("~Total time : " + (endTime - startTime) / Math.pow(10.0D, 9.0D) + " seconds. ", null);
        addToConsole.accept("~Total instructions: " + validTokens.size(), null);
    }

    public static String errors(String token, int line) {
        String value = "Error at line " + line + " : ";
        boolean existingToken = false;
        int i = 0;
        for (String ep : ERRORS_PATTERNS) {
            if (token.matches(ep)) {
                value += ERRORS_MESSAGES[i];
                existingToken = true;
                break;
            }
            i++;
        }
        if (!existingToken) {
            value += "Cannot find symbol: " + token;
        }
        return value;
    }

    public static void generateHex(ArrayList<String> tokens, File hexFile) {
        ArrayList<String> hexes = new ArrayList<>();
        ArrayList<String> cleanHexes = new ArrayList();
        //Instruction for execute code
        hexes.add("0128");
        try {
            FileWriter fw = new FileWriter(hexFile);
            PrintWriter pw = new PrintWriter(fw, true);
            for (String token : tokens) {
                for (Instruction instruction : Instruction.INSTRUCTIONS) {
                    if (token.matches(instruction.getPattern())) {
                        if (instruction == Instruction.LABEL) {
                            hexes.add("*" + token.replaceAll(":", "") + "*");
                            hexes.add("8312");
                            break;
                        } else if (instruction == Instruction.GOTO_LABEL) {
                            hexes.add("*" + token.replaceAll("\\s", "") + "*");
                            break;
                        } else if (instruction == Instruction.PORTB_MULTIPLE) {
                            String serialString = token.replace("portb", "").replace("%", "").replace("=", "").trim();
                            String[] serial = serialString.split("");
                            int sum = 0;
                            for (int j = serial.length - 1; j >= 0; j--) {
                                int value = (int) Math.pow(2, j);
                                if (serial[serial.length - 1 - j].equals("1")) {
                                    sum += value;
                                }
                            }
                            String sumHex = Integer.toHexString(sum);
                            if (sumHex.length() == 1) {
                                sumHex = "0" + sumHex;
                            }
                            hexes.add(sumHex.toUpperCase() + "30");
                            hexes.add("8312");
                            hexes.add("8600");
                            break;
                        } else {
                            if (hexes.addAll(instruction.getHexes())) {
                                break;
                            }
                        }
                    }
                }
            }
            //First remove all 8312 instructions if the TRIS(A|B) is not exists
            if (!hexes.stream().anyMatch(hex -> hex.equals("8316"))) {
                //If a 8316 instructions not exsits then a TRIS(A|B) instructions not exist
                hexes.removeIf(hex -> hex.equals("8312"));
            }
            //Second remove the 8316 tris and 8312 ports instructions repeated
            for (int j = 0; j < hexes.size(); j++) {
                if (hexes.get(j).equals("8316") && !cleanHexes.contains("8316")) {
                    cleanHexes.add(hexes.get(j));
                } else if (hexes.get(j).equals("8312") && !cleanHexes.contains("8312")) {
                    cleanHexes.add(hexes.get(j));
                } else {
                    if (!hexes.get(j).equals("8312") && !hexes.get(j).equals("8316")) {
                        cleanHexes.add(hexes.get(j));
                    }
                }
            }
            hexes = cleanHexes;
            //Third locale labels
            for (int j = 0; j < hexes.size(); j++) {
                if (hexes.get(j).matches("\\*goto.+\\*")) {
                    int index = hexes.indexOf(hexes.get(j).replace("goto", ""));
                    int counter = 0;
                    for (int i = 0; i < index; i++) {
                        if (hexes.get(i).matches("\\*(?!goto)(([a-z]|_|\\d)?[a-z]\\w*)+\\*")) {
                            counter++;
                        }
                    }
                    index -= counter;
                    int place = index;
                    place -= ((int) (index / 256)) * 256;
                    if (index > 0) {
                        String placeHex = Integer.toHexString(place);
                        if (placeHex.length() == 1) {
                            placeHex = "0" + placeHex;
                        }
                        int location = 40 + ((int) (index / 256));
                        placeHex += Integer.toHexString(location);
                        hexes.set(j, placeHex.toUpperCase());
                    }
                }
            }
            //Fourth delete all references of instructions
            hexes.removeIf(hex -> hex.matches("\\*.+\\*"));
            //Once translate instructions, truncate 8 per 8 instructions per line and count it
            int generalCounter = 0;
            int instructionsSetCounter = 0;
            for (int i = 0; i < hexes.size(); i += 8) {
                if (generalCounter == 256) {
                    generalCounter = 0;
                    instructionsSetCounter++;
                }
                String line = "";
                int individualCounter = 0;
                for (int j = i; j < (i + 8) && j < hexes.size(); j++) {
                    individualCounter += 2;
                }
                //Append the size of instructions to hex file
                pw.print(":");
                //line += ":";
                String ic = Integer.toHexString(individualCounter);
                String lc = Integer.toHexString(instructionsSetCounter);
                String gc = Integer.toHexString(generalCounter);
                if (ic.length() == 1) {
                    ic = "0" + ic;
                }
                if (lc.length() == 1) {
                    lc = "0" + lc;
                }
                if (gc.length() == 1) {
                    gc = "0" + gc + "00";
                } else if (gc.length() > 1 && gc.length() < 5) {
                    gc += "0".repeat(4 - gc.length());
                }
                pw.append(ic.toUpperCase() + lc.toUpperCase() + gc.toUpperCase());
                line += ic.toUpperCase() + lc.toUpperCase() + gc.toUpperCase();
                generalCounter += individualCounter;
                //Next append the instructions
                for (int j = i; j < (i + 8) && j < hexes.size(); j++) {
                    pw.append(hexes.get(j));
                    line += hexes.get(j);
                }
                //Append the size of set instructions
                int size = 0;
                for (int j = 0; j < line.length(); j += 2) {
                    String minHex = line.substring(j, j + 2);
                    size += Integer.parseInt(minHex, 16);
                }
                size = 256 - size;
                while (size < 0) {
                    size += 256;
                }
                String sizeHex = Integer.toHexString(size);
                if (sizeHex.length() == 1) {
                    sizeHex = "0" + sizeHex;
                }
                pw.append(sizeHex.toUpperCase() + "\n");
            }
            pw.append(":02400E00223F4F\n");
            pw.append(":00000001FF");
            fw.close();
            pw.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
