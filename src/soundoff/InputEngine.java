/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

import java.util.ArrayList;
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
            if (notes[i][0] < Constants.HIGH_FREQ + 10 && notes[i][0] > Constants.HIGH_FREQ - 10 && notes[i][1] > 0.5) {
                return 10;
            }
            if (notes[i][0] < Constants.LOW_FREQ + 10 && notes[i][0] > Constants.LOW_FREQ - 10 && notes[i][1] > 0.5) {
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
        while (startedListen > System.currentTimeMillis() - 10000) { // ms
            if (System.currentTimeMillis() > lastListen + Constants.DELTA_CHECKS) {
                lastListen = System.currentTimeMillis();
                float[][] features = sp.getFeatures();
                if (features != null) {
                    int note = whatNote(features);
                    if(note != 0){
                        beeps.add(note);
                    }
                }
            }
        }
        for(int beep : beeps){
            System.out.println(beep + " ");
        }

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
        return "";
    }
}
