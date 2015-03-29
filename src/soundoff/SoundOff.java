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
        
<<<<<<< HEAD
        OutputEngine oe = new OutputEngine("Hi Ken");
        oe.play();
=======
//        OutputEngine oe = new OutputEngine("at.com");
//        oe.play();
>>>>>>> origin/master
        
        InputEngine ie = new InputEngine();
        String message = ie.listen();
        System.out.println(message);

//        char[] someBin = BinStringConverter.stringToBin("cnn.com");
//        String input = "";
//        for(int i = 0;i < someBin.length; i++){
//            input += someBin[i];
//        }
//        BinStringConverter.binToString(input);
    }
}
