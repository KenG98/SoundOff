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
public class SoundOff {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        OutputEngine oe = new OutputEngine("cnn.com");
        oe.play();
        
//        InputEngine ie = new InputEngine();
//        String message = ie.listen();
//        System.out.println(message);

//        BinStringConverter.stringToBin("cnn.com");
//        char[] someBin = BinStringConverter.stringToBin("cnn.com");
//        String input = "";
//        for(int i = 0;i < someBin.length; i++){
//            input += someBin[i];
//        }
//        System.out.println(input);
//        BinStringConverter.binToString(input);
    }
}
