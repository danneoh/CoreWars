package io;
import java.nio.file.*;
public class CoreWars
{
    public static void main(String[] args) throws Exception
    {
        String data = readFileAsString("src\\test.txt");
        System.out.println(data);
    }

    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }


} 
