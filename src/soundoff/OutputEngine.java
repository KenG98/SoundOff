/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 *
 * @author kengarber
 */
public class OutputEngine {
    private String textToSend;
    private char[] binaryToSend;
    
    public OutputEngine(String text){
        textToSend = text;
        binaryToSend = BinStringConverter.stringToBin(textToSend);
    }
    
    public void play(){
        AudioContext ac = new AudioContext();
        Gain g = new Gain(ac, 1, (float) 1.0);
        WavePlayer highNote = new WavePlayer(ac, Constants.HIGH_FREQ, Buffer.SINE);
        WavePlayer lowNote = new WavePlayer(ac, Constants.LOW_FREQ, Buffer.SINE);
        g.addInput(highNote);
        g.addInput(lowNote);
        lowNote.pause(true);
        highNote.pause(true);
        ac.out.addInput(g);
        ac.start();
        
        long delayStart = System.currentTimeMillis();
        while(delayStart > System.currentTimeMillis() - 500){
            
        }
        
        //play message
        System.out.println("Message playing...");
        for(int i = 0; i < binaryToSend.length; i++){
            long noteBegan = System.currentTimeMillis(); 
            if(binaryToSend[i] == '1'){
                highNote.pause(false);
                lowNote.pause(true);
                while(noteBegan > System.currentTimeMillis() - Constants.BEEP_LENGTH - 1){
                    //stall time during the duration of the note
                }
            }
            if(binaryToSend[i] == '0'){
                highNote.pause(true);
                lowNote.pause(false);
                while(noteBegan > System.currentTimeMillis() - Constants.BEEP_LENGTH - 1){
                    //stall time during the duration of the note
                }
            }
        }
        
        ac.stop();
    }
    
}
