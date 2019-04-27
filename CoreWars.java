import java.io.*;
import java.util.Random;
import java.util.Scanner;
public class CoreWars {

    /* Creates Core to be used in multiple methods */
    static int coreSize = 10;
    static String[] core = new String[coreSize];

    /* Separates the instruction String into opCode and its variables */
    private static String[] sepInst(String inst){

        String[] separated = new String[3];
        separated[0] = inst.substring(0, inst.indexOf(' '));
        if(-1 == inst.indexOf(',')){//enters if when only one variable
            separated[1] = inst.substring(inst.indexOf(' ')+1, inst.length());
        }else{//enters else when only when two variables
            separated[1] = inst.substring(inst.indexOf(' ')+1, inst.indexOf(','));
            separated[2] = inst.substring(inst.indexOf(',')+2, inst.length());
        }//end ifelse

        return separated;
    }//end sepInst

    /* returns the integar value of the string variable passed and take into account indirect address modes */
    private static int getVar(int initInd, String var)throws Exception{
        int num;
        int perInd;
        char addMode;
        String[] inst;

        try{//tries to retrieve relative field
            num = Integer.parseInt(var);
        }catch(Exception e){//if not relative then seperates the address mode
            addMode = var.charAt(0);
            num = Integer.parseInt(var.substring(1, var.length()));
            perInd = (initInd + num) % coreSize; //this makes sure that the index wraps around without being a negative number
            inst = sepInst(core[perInd]); //this is used if addMode is indirect

            switch(addMode){

                case '*':
                    num = num + getVar(perInd, inst[1]);
                    break;

                case '@':
                    if(inst[2] == null){
                        num = num + getVar(perInd, inst[1]);
                    }else{
                        num = num + getVar(perInd, inst[2]);
                    }//end ifelse
                    break;

                default:
                    break;
            }//end switch
        }//end try

        return num;
    }//end getVar

    public static void main(String[] args) throws Exception {

        /* Initialize core */
        int[] ind = new int[2];
        for(int c = 0; c < core.length; c++)
            core[c] = "DAT #0, #0";

        /* Creates an array of strings to hold warriors */
        String[][] Soldier = new String[2][12];
        int[] inst = new int[2];

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
            while (sol.hasNextLine()) {
                Soldier[s][i] = sol.nextLine();
                i++;
            }//while loop
            inst[s] = i;
        }//for loop

        System.out.println("Length of 1: "+inst[0]);
        System.out.println("length of 2: "+inst[1]+"\n");

        /* Place Warriors in Core */
        Random rand = new Random();
        ind[0] = rand.nextInt(core.length);
        do{
            ind[1] = rand.nextInt(core.length);
            System.out.print(ind[1]);
        }while((ind[1] >= ind[0] && ind[1] <= (ind[0]+inst[0])) || (ind[1]+inst[1]) >= ind[0] && (ind[1]+inst[1]) <= (ind[0]+inst[0]));

        System.out.println("\nInit index of 1: "+ind[0]);
        System.out.println("Init index of 2: "+ind[1]);

        for(int b = 0; b < 2; b++){
            for(int y = 0; y < inst[b]; y++)
                core[(ind[b]+y)%coreSize] = Soldier[b][y];
        }//end for


        /*Prints out the "core" so we can see how it would look like*/
        for(int a = 0; a < core.length; a++)
        {
            System.out.print(core[a] + "| ");
        }//for loop

        /* Starts the Game CoreWars */
        int moves = 0;
        int turn;
        String[] currentInst;

        do{
            turn = moves % 2;
            System.out.println("\nProgram " + (turn+1) + " current instruction: " + core[ind[turn]]);

            /*This "currentInst" stores the beginning 3 letters of the move*/
            currentInst = sepInst(core[ind[turn]]);
            char addMode1 = ' ';
            char addMode2 = ' ';
            int fieldA = 0;
            int fieldB = 0;

            System.out.println("This is the first number " + currentInst[1]);
            System.out.println("This is the second number " + currentInst[2]);

            //Obtains the value of the first variable along with the address mode
            //tries to retrieve relative field
            fieldA = getVar(ind[turn], currentInst[1]);
            addMode1 = currentInst[1].charAt(0);

            //Obtains the value of the second variable along with the address mode
            if(currentInst[2] != null){
                fieldB = getVar(ind[turn], currentInst[2]);
                addMode1 = currentInst[2].charAt(0);
            }//end if

            switch(currentInst[0])
            {
                case "MOV":
                    System.out.println("This is move");
                    if(addMode1 == '#'){
                        String data = "DAT #" + Integer.toString(fieldA);
                        core[(ind[turn] + fieldB) % coreSize] = data;
                    }else{
                        core[(ind[turn] + fieldB) % coreSize] = core[(ind[turn] + fieldA) % coreSize];
                    }//end ifelse
                    for(int a = 0; a < core.length; a++)
                    {
                        System.out.print(core[a] + "| ");
                    }//for loop
                    System.out.println("\n");
                    ind[turn] = (ind[turn]+1)%coreSize;
                    break;


                case "ADD":
                    System.out.println("this is add");
                    System.out.println("This is the beginning instruction: " + core[(ind[turn] + fieldB)]);
                    System.out.println("This is the new added 4 instruction: " + (core[(ind[turn] + fieldB)] + fieldA));
                    core[(ind[turn] + fieldB)] = core[(ind[turn] + fieldB)] + fieldA;
                    for(int a = 0; a < core.length; a++)
                    {
                        System.out.print(core[a] + "| ");
                    }//for loop
                    System.out.println("\n");
                    ind[turn] = (ind[turn]+1)%coreSize;
                    break;

                case "SUB":
                    System.out.println("this is sub");
                    break;

                case "JMZ":
                    System.out.println("this is jump if zero");
                    break;

                case "DIN":
                    System.out.println("this is decrement and jump if non zero");
                    break;

                case "CMP":
                    System.out.println("this is compare, skip if equal");
                    break;

                case "JMP":
                    ind[turn] = (ind[turn] + fieldA - 1 ) % coreSize;
                    for(int a = 0; a < core.length; a++)
                    {
                        System.out.print(core[a] + "| ");
                    }//for loop
                    System.out.println("\n");
                    System.out.println("this is jump");
                    ind[turn] = (ind[turn]+1)%coreSize;
                    break;

                case "SPL":
                    System.out.println("this is split");
                    break;

                case "DAT":
                    ind[turn] = (ind[turn]+1)%coreSize;
                    System.out.println("this is data");
                    break;
            }//switch

            //ind[turn] = (ind[turn]+1)%coreSize;
            moves++;
        }while(moves != 10/*!(currentInst[0].equals("DAT"))*/);

        /*Prints out the "core" so we can see how it would look like*/
        for(int a = 0; a < core.length; a++)
        {
            System.out.print(core[a] + "| ");
        }//for loop
        System.out.println("\n");

    }//main
}//coreWars


