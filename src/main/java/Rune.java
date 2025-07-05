/**
 * Works around the long class name in the top bar on a Mac.
 * 
 * @author bill
 */
public class Rune {

    public static void main(String[] args) {

        // TODO - This only works in the REAL main method. Get rid of this class. Use the other main class.
        System.setProperty("sun.java2d.uiScale", "2");

        com.alteredmechanism.rune.Rune.main(args);
    }

}
