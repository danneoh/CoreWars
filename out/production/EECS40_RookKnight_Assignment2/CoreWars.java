import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
public class CoreWars {

    /* Creates Core to be used in multiple methods */
    static int coreSize = 8000;
    static String[] core = new String[coreSize];


    /* Prints the Core grid '*/
    private static void printCore(){
        for(int a = 0; a < core.length; a++)
        {
            if(a%10 == 0){ System.out.println();}
            System.out.print(core[a] + "| ");
        }//for loop
        System.out.println();
    }//end printCore

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
            perInd = (initInd + num + coreSize) % coreSize; //this makes sure that the index wraps around without being a negative number
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
        Vector<Vector<Integer>> ind = new Vector<Vector<Integer>>();
        for(int c = 0; c < core.length; c++)
        {
            core[c] = "DAT #0";
        }
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
        Vector<Integer> v1 = new Vector<Integer>();
        Vector<Integer> v2 = new Vector<Integer>();

        Random rand = new Random();
        v1.add(rand.nextInt(core.length));
        v2.add(rand.nextInt(core.length));

        ind.add(v1);
        ind.add(v2);
        do{
            ind.get(1).set(0, rand.nextInt(core.length));
            System.out.print(ind.get(1).get(0));
        }while((ind.get(1).get(0) >= ind.get(0).get(0) && ind.get(1).get(0) <= (ind.get(0).get(0)+inst[0])) || (ind.get(1).get(0)+inst[1]) >= ind.get(0).get(0) && (ind.get(1).get(0)+inst[1]) <= (ind.get(0).get(0)+inst[0]));


        int nextFive = ind.get(0).get(0);
        int prevFive = ind.get(0).get(0);
        System.out.println("\nInit index of 1: "+nextFive/*ind.get(0).get(0)*/);
        System.out.println("Init index of 2: "+ind.get(1).get(0));

        for(int b = 0; b < 2; b++){
            for(int y = 0; y < inst[b]; y++)
                core[(ind.get(b).get(0)+y)%coreSize] = Soldier[b][y];
        }//end for


        /*Prints out the "core" so we can see how it would look like*/
        //printCore();
        /* Starts the Game CoreWars */
        int moves = 0;
        int turn = 0;
        int split = 0;
        int[] thrd = new int[2];
        thrd[0] = 1;
        thrd[1] = 1;
        int[] mvs = new int[2];
        String[] currentInst;
        String[] instB;
        boolean dontSkip;
        boolean nextTurn = true;

        do{

            System.out.println("This is the previous 5 moves: ");
            for(int y = prevFive; y > prevFive - 5; y--)
            {
                System.out.println(core[(y-1+coreSize)%coreSize] + "| ");
            }//for loop
            System.out.println();

            System.out.println("This is the next 5 moves: ");
            for(int z = nextFive; z+1 < nextFive + 6; z++)
            {
                System.out.println(core[z%coreSize] + "| ");
            }//for loop
            System.out.println();


            turn = moves % 2;
            if(mvs[turn] >= thrd[turn]){
                mvs[turn] = 0;
            }//end if
            split = mvs[turn];
            dontSkip = true;
            System.out.println("ind " + (turn+1) + " is " + ind.get(turn).get(split));


            System.out.println("\nProgram " + (turn+1) + " current instruction: " + core[(ind.get(turn).get(split))]);
            System.out.println("Move: " + moves + ", Threads: " + thrd[turn]);


            /*This "opCode" stores the beginning 3 letters of the move*/
            currentInst = sepInst(core[ind.get(turn).get(split)]);

            char addMode1 = ' ';
            char addMode2 = ' ';
            int fieldA = 0;
            int fieldB = 0;

            System.out.println("This is the first number " + currentInst[1]);
            System.out.println("This is the second number " + currentInst[2]);

            //Obtains the value of the first variable along with the address mode
            //tries to retrieve relative field
            fieldA = getVar(ind.get(turn).get(split), currentInst[1]);
            addMode1 = currentInst[1].charAt(0);


            //Obtains the value of the second variable along with the address mode
            if(currentInst[2] != null){
                fieldB = getVar(ind.get(turn).get(split), currentInst[2]);
                addMode2 = currentInst[2].charAt(0);
            }//end if

            switch(currentInst[0])
            {
                case "MOV":
                    System.out.println("This is move");
                    if(addMode1 == '#'){
                        String data = "DAT #" + Integer.toString(fieldA);
                        core[(ind.get(turn).get(split) + fieldB) % coreSize] = data;
                    }else{
                        core[(ind.get(turn).get(split) + fieldB) % coreSize] = core[(ind.get(turn).get(split) + fieldA) % coreSize];
                    }//end if else
                    break;


                case "ADD":
                    System.out.println("this is add");
                    System.out.println("The addMode is " + addMode1);
                    instB = sepInst(core[(ind.get(turn).get(split) + fieldB) % coreSize]);
                    if(addMode1 == '#'){
                        if(instB[0].equals("DAT")){
                            instB[1] = "#" + Integer.toString(getVar(ind.get(turn).get(split), instB[1]) + fieldA);
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT " + instB[1];
                        }else{
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT " + currentInst[1];
                        }//end ifelse
                    }else{
                        if(instB[0].equals("DAT")){
                            instB[1] = "#" + Integer.toString(getVar(ind.get(turn).get(split), instB[1]) + fieldB);
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT " + instB[1];

                        }else{
                            if(instB[1].charAt(0) == '#' || instB[1].charAt(0) == '*'){
                                instB[1] = instB[1].charAt(0) + Integer.toString(Integer.parseInt(instB[1].substring(1, instB[1].length())) + fieldA);
                            }else{
                                instB[1] = Integer.toString(Integer.parseInt(instB[1]) + fieldA);
                            }//end ifelse

                            if(instB[2].charAt(0) == '#' || instB[2].charAt(0) == '@'){
                                instB[2] = instB[2].charAt(0) + Integer.toString(Integer.parseInt(instB[2].substring(1, instB[2].length())) + fieldB);
                            }else{
                                instB[2] = Integer.toString(Integer.parseInt(instB[2]) + fieldB);
                            }//end ifelse
                        }//end ifelse
                    }//end ifelse
                    break;

                case "SUB":
                    System.out.println("this is sub");
                    System.out.println("The addMode is " + addMode1);
                    instB = sepInst(core[(ind.get(turn).get(split) + fieldB) % coreSize]);
                    if(addMode1 == '#'){
                        if(instB[0].equals("DAT")){
                            instB[1] = "#" + Integer.toString(getVar(ind.get(turn).get(split), instB[1]) - fieldA);
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT " + instB[1];
                        }else{
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT #" + Integer.toString(fieldA*-1);
                        }//end ifelse
                    }else{
                        if(instB[0].equals("DAT")){
                            instB[1] = "#" + Integer.toString(getVar(ind.get(turn).get(split), instB[1]) - fieldB);
                            core[(ind.get(turn).get(split) + fieldB) % coreSize] = "DAT " + instB[1];

                        }else{
                            if(instB[1].charAt(0) == '#' || instB[1].charAt(0) == '*'){
                                instB[1] = instB[1].charAt(0) + Integer.toString(Integer.parseInt(instB[1].substring(1, instB[1].length())) - fieldA);
                            }else{
                                instB[1] = Integer.toString(Integer.parseInt(instB[1]) - fieldA);
                            }//end ifelse

                            if(instB[2].charAt(0) == '#' || instB[2].charAt(0) == '@'){
                                instB[2] = instB[2].charAt(0) + Integer.toString(Integer.parseInt(instB[2].substring(1, instB[2].length())) - fieldB);
                            }else{
                                instB[2] = Integer.toString(Integer.parseInt(instB[2]) - fieldB);
                            }//end ifelse
                        }//end ifelse
                    }//end ifelse

                    break;

                case "JMZ":
                    System.out.println("this is jump if zero");
                    if(fieldB == 0)
                    {
                        System.out.println("This is the new instruction if 0: " + core[(ind.get(turn).get(split) + fieldA)]);
                        ind.get(turn).set(split, (ind.get(turn).get(split) + fieldA - 1 ) % coreSize);
                    }
                    else
                    {
                        System.out.println("Field B was not 0 so skips");
                    }
                    break;

                case "DJN":
                    System.out.println("this is decrement and jump if non zero");
                    if(fieldB != 0)
                    {
                        int newFieldB = fieldB - 1;
                        System.out.println("This is the new field B after decremented by 1: " + newFieldB);
                        if(newFieldB != 0)
                        {
                            System.out.println("This is the new instruction if new field is not 0: " + core[(ind.get(turn).get(split) + fieldA)]);
                            ind.get(turn).set(split, (ind.get(turn).get(split) + fieldA - 1 ) % coreSize);
                        }//if
                        else
                        {
                            System.out.println("New field B was 0 so skips");
                        }//else
                    }//if
                    else
                    {
                        System.out.println("New Field B was already 0 so skip");
                    }//else
                    break;

                case "CMP":
                    System.out.println("this is compare, skip if equal");
                    instB = sepInst(core[(ind.get(turn).get(split) + fieldB) % coreSize]);
                    String check = Integer.toString(getVar(ind.get(turn).get(split), instB[1]));
                    System.out.println("this is check " + check);
                    System.out.println("this is fieldA " + fieldA);
                    if(addMode1 == '#')
                    {
                        if(Integer.toString(fieldA).equals(check))
                        {
                            System.out.println("They are the same and it will skip");
                            ind.get(turn).set(split, (ind.get(turn).get(split)+1)%coreSize);
                        }
                    }
                    else
                    {
                        System.out.println("This is just for debugging");
                    }
                    break;

                case "JMP":
                    System.out.println("this is jump");
                    ind.get(turn).set(split, (ind.get(turn).get(split) + fieldA - 1 + coreSize) % coreSize);
                    break;

                case "SPL":
                    System.out.println("this is split");
                    ind.get(turn).add((ind.get(turn).get(split) + fieldA + coreSize) % coreSize);
                    thrd[turn]++;
                    break;

                case "DAT":
                    System.out.println("this is data");
                    if(ind.get(turn).size() > 1){
                        ind.get(turn).remove(split);
                        thrd[turn]--;
                        dontSkip = false;
                    }else{
                        nextTurn = false;
                        System.out.println("Program " + (turn+1) + "crashed");
                    }//end if else

                    break;
            }//switch

            /*Prints out the "core" so we can see how it would look like*/
            //printCore();
            if(dontSkip){
                ind.get(turn).set(split, (ind.get(turn).get(split)+1)%coreSize);
                System.out.println("ind " + (turn+1) + " is " + ind.get(turn).get(split));

            }//endif
            moves++;
            mvs[turn]++;
            nextFive = ind.get(turn).get(split);
            prevFive = ind.get(turn).get(split) - 1;

        }while(nextTurn);

        System.out.println("\n");

    }//main
}//corewars
