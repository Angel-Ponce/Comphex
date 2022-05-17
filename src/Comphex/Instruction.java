package Comphex;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Angel Ponce
 */
public class Instruction {

    //TRISA Instructions    
    public static final Instruction TRISA_1 = new Instruction("trisa\\s*=\\s*1", new String[]{"0130", "8316", "8500"});
    public static final Instruction TRISA_0 = new Instruction("trisa\\s*=\\s*0", new String[]{"8316", "8501"});
    //TRISB Instructions
    public static final Instruction TRISB_1 = new Instruction("trisb\\s*=\\s*1", new String[]{"0130", "8316", "8600"});
    public static final Instruction TRISB_0 = new Instruction("trisb\\s*=\\s*0", new String[]{"8316", "8601"});
    //PORTA Instructions
    public static final Instruction PORTA_1 = new Instruction("porta\\s*=\\s*1", new String[]{"0130", "8312", "8500"});
    public static final Instruction PORTA_0 = new Instruction("porta\\s*=\\s*0", new String[]{"8312", "8501"});
    //PORTB Instructions
    public static final Instruction PORTB_1 = new Instruction("portb\\s*=\\s*1", new String[]{"0130", "8312", "8600"});
    public static final Instruction PORTB_0 = new Instruction("portb\\s*=\\s*0", new String[]{"8312", "8601"});
    public static final Instruction PORTB_MULTIPLE = new Instruction("portb\\s*=\\s*%(0|1){8}", new String[]{""});
    //PORTS Instructions
    public static final Instruction PORTA_0_0 = new Instruction("porta\\.0\\s*=\\s*0", new String[]{"8312", "0510"});
    public static final Instruction PORTA_0_1 = new Instruction("porta\\.0\\s*=\\s*1", new String[]{"8312", "0514"});
    public static final Instruction PORTA_1_0 = new Instruction("porta\\.1\\s*=\\s*0", new String[]{"8312", "8510"});
    public static final Instruction PORTA_1_1 = new Instruction("porta\\.1\\s*=\\s*1", new String[]{"8312", "8514"});
    public static final Instruction PORTA_2_0 = new Instruction("porta\\.2\\s*=\\s*0", new String[]{"8312", "0511"});
    public static final Instruction PORTA_2_1 = new Instruction("porta\\.2\\s*=\\s*1", new String[]{"8312", "0515"});
    public static final Instruction PORTA_3_0 = new Instruction("porta\\.3\\s*=\\s*0", new String[]{"8312", "8511"});
    public static final Instruction PORTA_3_1 = new Instruction("porta\\.3\\s*=\\s*1", new String[]{"8312", "8515"});
    public static final Instruction PORTA_4_0 = new Instruction("porta\\.4\\s*=\\s*0", new String[]{"8312", "0512"});
    public static final Instruction PORTA_4_1 = new Instruction("porta\\.4\\s*=\\s*1", new String[]{"8312", "0516"});
    public static final Instruction PORTA_5_0 = new Instruction("porta\\.5\\s*=\\s*0", new String[]{"8312", "8512"});
    public static final Instruction PORTA_5_1 = new Instruction("porta\\.5\\s*=\\s*1", new String[]{"8312", "8516"});
    public static final Instruction PORTA_6_0 = new Instruction("porta\\.6\\s*=\\s*0", new String[]{"8312", "0513"});
    public static final Instruction PORTA_6_1 = new Instruction("porta\\.6\\s*=\\s*1", new String[]{"8312", "0517"});
    public static final Instruction PORTA_7_0 = new Instruction("porta\\.7\\s*=\\s*0", new String[]{"8312", "8513"});
    public static final Instruction PORTA_7_1 = new Instruction("porta\\.7\\s*=\\s*1", new String[]{"8312", "8517"});
    public static final Instruction PORTB_0_0 = new Instruction("portb\\.0\\s*=\\s*0", new String[]{"8312", "0610"});
    public static final Instruction PORTB_0_1 = new Instruction("portb\\.0\\s*=\\s*1", new String[]{"8312", "0614"});
    public static final Instruction PORTB_1_0 = new Instruction("portb\\.1\\s*=\\s*0", new String[]{"8312", "8610"});
    public static final Instruction PORTB_1_1 = new Instruction("portb\\.1\\s*=\\s*1", new String[]{"8312", "8614"});
    public static final Instruction PORTB_2_0 = new Instruction("portb\\.2\\s*=\\s*0", new String[]{"8312", "0611"});
    public static final Instruction PORTB_2_1 = new Instruction("portb\\.2\\s*=\\s*1", new String[]{"8312", "0615"});
    public static final Instruction PORTB_3_0 = new Instruction("portb\\.3\\s*=\\s*0", new String[]{"8312", "8611"});
    public static final Instruction PORTB_3_1 = new Instruction("portb\\.3\\s*=\\s*1", new String[]{"8312", "8615"});
    public static final Instruction PORTB_4_0 = new Instruction("portb\\.4\\s*=\\s*0", new String[]{"8312", "0612"});
    public static final Instruction PORTB_4_1 = new Instruction("portb\\.4\\s*=\\s*1", new String[]{"8312", "0616"});
    public static final Instruction PORTB_5_0 = new Instruction("portb\\.5\\s*=\\s*0", new String[]{"8312", "8612"});
    public static final Instruction PORTB_5_1 = new Instruction("portb\\.5\\s*=\\s*1", new String[]{"8312", "8616"});
    public static final Instruction PORTB_6_0 = new Instruction("portb\\.6\\s*=\\s*0", new String[]{"8312", "0613"});
    public static final Instruction PORTB_6_1 = new Instruction("portb\\.6\\s*=\\s*1", new String[]{"8312", "0617"});
    public static final Instruction PORTB_7_0 = new Instruction("portb\\.7\\s*=\\s*0", new String[]{"8312", "8613"});
    public static final Instruction PORTB_7_1 = new Instruction("portb\\.7\\s*=\\s*1", new String[]{"8312", "8617"});
    //Label instructions
    public static final Instruction LABEL = new Instruction("([a-z]|_|\\d)?[a-z]\\w*:", new String[]{""});
    public static final Instruction GOTO_LABEL = new Instruction("goto\\s+([a-z]|_|\\d)?[a-z]\\w*", new String[]{""});
    //Total instructions
    public static final ArrayList<Instruction> INSTRUCTIONS = new ArrayList();

    static {
        for (Field field : Instruction.class.getDeclaredFields()) {
            if (field.getType() == Instruction.class) {
                try {
                    INSTRUCTIONS.add((Instruction) field.get(null));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    System.err.println(ex);
                }
            }
        }
    }
    private String pattern;
    private ArrayList<String> hexes;

    public Instruction() {
    }

    public Instruction(String pattern, String[] hexes) {
        this.pattern = pattern;
        this.hexes = new ArrayList<>(Arrays.asList(hexes));
    }

    public ArrayList<String> getHexes() {
        return hexes;
    }

    public void setHexes(String[] hexes) {
        this.hexes = new ArrayList(Arrays.asList(hexes));
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
