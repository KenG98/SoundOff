/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

/**
 *
 * @author kengarber
 */
public class BinStringConverter {
    
    public static char[] stringToBin(String text){
        String collectionString = "";
        System.out.println();
        System.out.println(text);
        
        for(int i = 0; i < text.length(); i++){
            String binString = Integer.toBinaryString((int)text.charAt(i));
            int strLen = binString.length();
            for(int n = 0; n < 7-strLen;n++){
                binString = "0" + binString;
            }
            collectionString += binString;
        }
        System.out.println(collectionString);
        System.out.println();
        return collectionString.toCharArray();
    }
    
    public static String binToString(String binary){
        String outString = "";
        System.out.println();
        System.out.println(binary);
        for(int i=0; i<binary.length() / 7; i++){
            String tempStr = binary.substring(7*i, 7*i + 7);
            outString += (char)Integer.parseInt(tempStr, 2);
        }
        System.out.println(outString);
        System.out.println();
        return outString; //doesnt work, need to change it
    }
    
}
