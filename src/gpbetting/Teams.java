/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpbetting;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author mateusz
 */
public class Teams  {   
    

    
//    public static int countLines(String filename) throws IOException {
//    InputStream is = new BufferedInputStream(new FileInputStream(filename));
//    try {
//        byte[] c = new byte[1024];
//        int count = 0;
//        int readChars = 0;
//        boolean empty = true;
//        while ((readChars = is.read(c)) != -1) {
//            empty = false;
//            for (int i = 0; i < readChars; ++i) {
//                if (c[i] == '\n') {
//                    ++count;
//                }
//            }
//        }
//        return (count == 0 && !empty) ? 1 : count;
//    } finally {
//        is.close();
//    }
//}
    
    public static ArrayList createListWithTeamsNames(String file) throws IOException{
        ArrayList TeamsNamesList = new ArrayList();
        FileReader file1ready = new FileReader(file);
        Scanner teamGP = new Scanner(file1ready);
        BufferedReader bufferedReader = new BufferedReader(file1ready);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while((line = bufferedReader.readLine()) != null){

            stringBuffer.append(line);
            stringBuffer.append("\n");
            TeamsNamesList.add(line);
        }
        
        return TeamsNamesList;
    }
    
    
//    public static void main (String[] args) throws FileNotFoundException, IOException {
////        FileReader file = new FileReader("nazwyKlubow.txt");
////        Scanner in = new Scanner(file);
////        
////        String zdanie = in.nextLine();
////        System.out.println(zdanie);
//        ArrayList TeamsGP = createListWithTeamsNames("nazwyKlubow.txt");
//        System.out.print(TeamsGP);
//        ArrayList TeamsWS = createListWithTeamsNames("nazwy.txt");
//        System.out.print(TeamsWS);
//    }
    
}