/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

import java.util.ArrayList;
import java.util.Arrays;
import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.featureextractors.SpectralPeaks;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 *
 * @author kengarber
 */
public class InputEngine {

    public InputEngine() {

    }

    private int whatNote(float[][] notes) {
        for (int i = 0; i < notes.length; i++) {
            if (notes[i][0] < Constants.HIGH_FREQ + 20 && notes[i][0] > Constants.HIGH_FREQ - 20 && notes[i][1] > 0.05) {
                return 10;
            }
            if (notes[i][0] < Constants.LOW_FREQ + 20 && notes[i][0] > Constants.LOW_FREQ - 20 && notes[i][1] > 0.05) {
                return -10;
            }
//            if(notes[i][0] < Constants.TERMINAL_BEEP_FREQ + 10 && notes[i][0] > Constants.TERMINAL_BEEP_FREQ - 10 && notes[i][1] > 0.5){
//                return 0;
//            }
        }
        return 0;
    }

    public String listen() {
        //setup the listener 
        //begin listening, don't record anything
        int numPeaks = 32;
        AudioContext ac = new AudioContext();
        PowerSpectrum ps = new PowerSpectrum();
        SpectralPeaks sp;

        UGen micIn = ac.getAudioInput();

        ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
        sfs.addInput(micIn);
        FFT fft = new FFT();
        sfs.addListener(fft);
        fft.addListener(ps);
        sp = new SpectralPeaks(ac, numPeaks);
        ps.addListener(sp);
        ac.out.addDependent(sfs);
        ac.start();
        
        ArrayList<Integer> beeps = new ArrayList<>();
        long startedListen = System.currentTimeMillis();
        long lastListen = System.currentTimeMillis();
        while (startedListen > System.currentTimeMillis() - 15000) { // ms
            if (System.currentTimeMillis() > lastListen + Constants.DELTA_CHECKS - 1) {
                lastListen = System.currentTimeMillis();
                float[][] features = sp.getFeatures();
                if (features != null) {
                    int note = whatNote(features);
                    beeps.add(note);
                }
            }
        }
        
        for(int beep : beeps){
            System.out.println(beep + " ");
        }
        
        int samplesPerWave = Constants.BEEP_LENGTH / Constants.DELTA_CHECKS;
        
        Integer[] beepArray = beeps.toArray(new Integer[beeps.size()]);
        
        int startIndex = 0;
        int endIndex = 0;
        for(int i = 0; i < beepArray.length - 2; i++){
            if(beepArray[i] != 0 && beepArray[i+1] != 0 && beepArray[i+2] != 0){
                startIndex = i;
                break;
            }
        }
        for(int i = 0; i < beepArray.length - 2; i++){
            if(i > startIndex && beepArray[i] == 0 && beepArray[i+1] == 0 && beepArray[i+2] == 0){
                endIndex = i;
                break;
            }
        }
        
//        Integer[] beepArrayShort = Arrays.copyOfRange(beepArray, startIndex, endIndex);
        int[] beepArrayShort = new int[endIndex - startIndex];
        for(int i = 0; i < endIndex - startIndex; i++){
            beepArrayShort[i] = beepArray[i + startIndex];
        }
        
        String finalBinary = "";
        
        for(int i = 0; i < beepArrayShort.length; i += samplesPerWave){
            int[] shortSample = Arrays.copyOfRange(beepArrayShort, i, i+5);
            int sumSample = 0;
            for(int n = 0; n < shortSample.length; n++){
                sumSample += shortSample[n];
            }
            if(sumSample > 0){
                finalBinary += "1";
            }
            if(sumSample < 0){
                finalBinary += "0";
            }
        }
        
        String finalMessage = BinStringConverter.binToString(finalBinary);
        
        
        

        //----------------
//        boolean messageNotYetStarted = true;
//        boolean highNoteWasPlaying = false;
//        long highNoteStarted = System.currentTimeMillis();
//        while(messageNotYetStarted){
//            float[][] features = sp.getFeatures();
//            if(features != null){
//                for(int i = 0; i < features.length; i++){
//                    if(features[i][0] > Constants.HIGH_FREQ - 10 && features[i][0] < Constants.HIGH_FREQ + 10 && features[i][1] > 1){
//                        //a high note is playing
//                        if(highNoteWasPlaying){
//                            long delta = System.currentTimeMillis() - highNoteStarted;
//                            if(delta > Constants.START_END_BEEP_LEN - 1){
//                                messageNotYetStarted = false;
//                            }
//                        }
//                        else{
//                            highNoteWasPlaying = true;
//                            highNoteStarted = System.currentTimeMillis();
//                        }
//                    }
//                    else{
//                        highNoteWasPlaying = false;
//                    }
//                }
//            }
//        }
//        System.out.println("DING MESSAGE STARTED");
        //-----------------------
//        boolean continueListen = true;
//        boolean collectingMessage = false;
//        
//        //if it hears the right pitch for x ms,
//        
//        boolean isHighBeep = false;
//        boolean isLowBeep = false;
//        long highBeepStart;
//        long lowBeepStart;
//        long beepLength;
//        
//        while(continueListen){
//            float[][] features = sp.getFeatures();
//            if(features != null){
//                for(int i = 0; i < features.length; i++){
//                    if(features[i][0] > Constants.HIGH_FREQ - 10 && features[i][0] < Constants.HIGH_FREQ + 10 && features[i][1] > 1){
//                        //a high note is playing
//                        
//                    }
//                    if(features[i][0] > Constants.LOW_FREQ - 10 && features[i][0] < Constants.LOW_FREQ + 10 && features[i][1] > 1){
//                        //a low note is playing
//                        
//                    }
//                }
//            }
//        }
        ac.stop();

        //it begins recording all the beeps and boops
        //if it hears the right pitch for x ms, then neither of the right pitches, it stops collecting
        //process things into 1s and 0s
        //return
        return finalMessage;
    }
}
