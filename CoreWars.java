import java.io.*;
import java.util.Random;
import java.util.Scanner;
public class CoreWars {
    public static void main(String[] args) throws Exception {

        /* Create and initialize core */
        int coreSize = 20;
        String[] core = new String[coreSize];
        for(int c = 0; c < core.length; c++)
            core[c] = "DAT #0";

        for(int s = 0; s < 2; s++) {
            String input;
            Scanner scanIn = new Scanner(System.in);
            System.out.println("Which warrior do you want to send to war?");
            input = scanIn.nextLine();

            String Warrior = input + ".txt";

            File file = new File("out\\production\\EECS40_RookKnight_Assignment2\\" + Warrior);
            Scanner sol = new Scanner(file);

            int i = 0;
            Random rand = new Random();
            int ind = rand.nextInt(core.length);
            while (sol.hasNextLine()) {
                core[(ind + i) % coreSize] = sol.nextLine();
                i++;
            }//while loop
        }//for loop
        for(int a = 0; a < core.length; a++)
            System.out.print(core[a] + "| ");
    }
}
