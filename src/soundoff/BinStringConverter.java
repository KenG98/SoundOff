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
        for(int i = 0; i < text.length(); i++){
            String binString = Integer.toBinaryString((int)text.charAt(i));
            int strLen = binString.length();
            for(int n = 0; n < 7-strLen;n++){
                binString = "0" + binString;
            }
            System.out.println(binString + " " + text.charAt(i));
            collectionString += binString;
        }
        return collectionString.toCharArray();
    }
    
    public static String binToString(String binary){
        return binary; //doesnt work, need to change it
    }
    
}
