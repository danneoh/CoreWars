import java.io.*;
import java.util.Random;
import java.util.Scanner;
public class CoreWars {
    public static void main(String[] args) throws Exception {

        /* Create and initialize core */
        int coreSize = 10;
        int ind = 0;
        String[] core = new String[coreSize];
        for(int c = 0; c < core.length; c++)
            core[c] = "DAT #0";

        /*For loop goes around twice to be able to read in two "warriors"*/
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
            ind = rand.nextInt(core.length);
            while (sol.hasNextLine()) {
                core[(ind + i) % coreSize] = sol.nextLine();
                i++;
            }//while loop
        }//for loop

        /*Prints out the "core" so we can see how it would look like*/
        for(int a = 0; a < core.length; a++)
        {
            System.out.print(core[a] + "| ");
        }//for loop

        /*Stores the contents of core at array to be able to read the move*/
        for(int l =0; l < 5; l++) {
            String coreIndex = core[ind];
            System.out.println("\n this is the core index" + coreIndex);

            /*This "opCode" stores the beginning 3 letters of the move*/
            String opCode = coreIndex.substring(0, 3);
            String number1;

            String number2;

            /*Reads first three letters of the move to determine what to do*/
            switch (opCode) {
                case "MOV":
                    System.out.println("This is move");
                    number1 = coreIndex.substring(3, 5);
                    number2 = coreIndex.substring(6, 8);
                    System.out.println("This is the first number" + number1);
                    System.out.println("This is the second number" + number2);
                    core[(ind%coreSize) + 1] = core[ind];
                    for (int a = 0; a < core.length; a++) {
                        System.out.print(core[a] + "| ");
                    }
                    break;

                case "JMP":
                    System.out.println("this is jump");
                    number1 = coreIndex.substring(3, 6);
                    System.out.println("This is the first number" + number1);
                    break;

                case "DAT":
                    System.out.println("this is data");
                    number1 = coreIndex.substring(3, 6);
                    System.out.print("This is the first number" + number1);
                    break;

                case "ADD":
                    System.out.println("this is add");
                    number1 = coreIndex.substring(3, 6);
                    number2 = coreIndex.substring(7, 9);
                    System.out.println("This is the first number" + number1);
                    System.out.println("This is the second number" + number2);
                    break;
            }//switch
            ind++;
        }
    }//main

}//coreWars
