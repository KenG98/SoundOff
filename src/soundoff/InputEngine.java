/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundoff;

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
    public InputEngine(){
        
    }
    
    public String listen(){
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
        
        boolean continueListen = true;
        boolean collectingMessage = false;
        
        //if it hears the right pitch for x ms,
        
        boolean isHighBeep = false;
        boolean isLowBeep = false;
        long highBeepStart;
        long lowBeepStart;
        long beepLength;
        
        while(continueListen){
            float[][] features = sp.getFeatures();
            
            if(features != null){
                for(int i = 0; i < features.length; i++){
                    if(features[i][0] > Constants.HIGH_FREQ - 10 && features[i][0] < Constants.HIGH_FREQ + 10 && features[i][1] > 1){
                        //a high note is playing
                        
                    }
                    if(features[i][0] > Constants.LOW_FREQ - 10 && features[i][0] < Constants.LOW_FREQ + 10 && features[i][1] > 1){
                        //a low note is playing
                        
                    }
                }
            }
        }
        
        ac.stop();
        
        //it begins recording all the beeps and boops
        
        //if it hears the right pitch for x ms, then neither of the right pitches, it stops collecting
        
        //process things into 1s and 0s
        
        //return
        
        
        return "";
    }
}
