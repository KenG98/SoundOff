/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
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
        WavePlayer highNote = new WavePlayer(ac, Constants.HIGH_FREQ, Buffer.SINE);
        WavePlayer lowNote = new WavePlayer(ac, Constants.LOW_FREQ, Buffer.SINE);
        lowNote.pause(true);
        highNote.pause(true); //might have to switch this and what is below
        ac.out.addInput(highNote);
        ac.out.addInput(lowNote);
        ac.start();
        
        //begin note (x ms high)
        System.out.println("Start note playing...");
        long startNoteBegin = System.currentTimeMillis();
        highNote.pause(false);
        while(startNoteBegin > System.currentTimeMillis() - Constants.START_END_BEEP_LEN){
            //this holds the highNote ON for x ms
        }
        highNote.pause(true);
        
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
        
        //end note (x ms high)
        System.out.println("End note playing...");
        long endNoteBegin = System.currentTimeMillis();
        lowNote.pause(true);
        highNote.pause(false);
        while(endNoteBegin > System.currentTimeMillis() - Constants.START_END_BEEP_LEN){
            //this holds the highNote ON for x ms
        }
        highNote.pause(true);
        
        ac.stop();
    }
    
}
