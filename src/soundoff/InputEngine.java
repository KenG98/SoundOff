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
            if (notes[i][0] < Constants.HIGH_FREQ + 25 && notes[i][0] > Constants.HIGH_FREQ - 25 && notes[i][1] > 0.04) {
                return 10;
            }
            if (notes[i][0] < Constants.LOW_FREQ + 25 && notes[i][0] > Constants.LOW_FREQ - 25 && notes[i][1] > 0.04) {
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
        int samplesPerWave = Constants.BEEP_LENGTH / Constants.DELTA_CHECKS;
        boolean shouldStop = false;
        boolean hasStarted = false;
        while (!shouldStop) { 
            if (System.currentTimeMillis() > lastListen + Constants.DELTA_CHECKS - 1) {
                lastListen = System.currentTimeMillis();
                float[][] features = sp.getFeatures();
                if (features != null) {
                    int note = whatNote(features);
                    beeps.add(note);
                }
                if(!hasStarted && beeps.size() > 2*samplesPerWave + 1){
                    boolean hasNote = true;
                    for(int i = 0; i < 2 * samplesPerWave;i++){
                        if(beeps.get(beeps.size() - i - 1) != 0){
                            hasNote = hasNote && true;
                        }else{
                            hasNote = false;
                        }
                    }
                    if(hasNote){
                        hasStarted = true;
                    }
                }
                if(hasStarted){
                    boolean stopping = true;
                    for(int i = 0; i < 2 * samplesPerWave;i++){
                        if(beeps.get(beeps.size() - i - 1) == 0){
                            stopping = stopping && true;
                        }else{
                            stopping = false;
                        }
                    }
                    if(stopping){
                        shouldStop = true;
                    }
                }
            }
        }
        
        for(int beep : beeps){
            System.out.println(beep + " ");
        }
        
        ac.stop();
        
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
        

        return finalMessage;
    }
}
