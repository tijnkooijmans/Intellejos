/*
 * SoundTest.java
 *
 * Created on August 19, 2003, 1:09 AM
 */

package simworldobjects;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.sound.midi.*;
import java.util.*;
import josx.platform.rcx.Sound.*;
import josx.util.*;


/**
 *
 * @author  Simon
 */
public class Sound extends Thread {
    
    private static Synthesizer synthesizer;
    private static Sequencer sequencer;
    private static Sequence sequence;
    private static Instrument instruments[];
    private  static ChannelData cc; 
    
    
   public static void playTune(int frequency, int duration){
        
        try {
            
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            
            synthesizer.open();
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 2);
            
        } catch (Exception ex) { ex.printStackTrace(); return; }
        
        Soundbank sb = synthesizer.getDefaultSoundbank();
	if (sb != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[341]);
            
        }
        
        MidiChannel midiChannels[] = synthesizer.getChannels();
        cc = new ChannelData(midiChannels[0], 0);
        
        cc.channel.programChange(341);
        
        cc.channel.allNotesOff();
                      
        cc.channel.noteOn(frequency, cc.velocity);
        
        try{Thread.sleep(duration);}
        catch(Exception e){}
        
        cc.channel.allNotesOff();
        
         
    }
    
    public static LinkedList getInstrumentList(){
        LinkedList instrumentList = new LinkedList();
        
            for(int i=0; i<instruments.length;i++){
                instrumentList.add(instruments[i].getName());
                //System.out.println(i+" "+instruments[i].getName());
            }
        
        return instrumentList;
             
    }
    
    public static void close(){
        if (synthesizer != null) {
            synthesizer.close();
        }
        if (sequencer != null) {
            sequencer.close();
        }
        sequencer = null;
        synthesizer = null;
        instruments = null;
        cc = null;
    }
    
    static class ChannelData {

        MidiChannel channel;
        boolean solo, mono, mute, sustain;
        int velocity, pressure, bend, reverb;
        int row, col, num;
 
        public ChannelData(MidiChannel channel, int num) {
            this.channel = channel;
            this.num = num;
            velocity = pressure = bend = reverb = 100;
            velocity = 127;
        }
    }
    
    /*
     //@param args the command line arguments
     
    public static void main(String[] args) {
        
        
        
        Sound.playTune(1,500);
        //Sound.playTune(62,500);
        //Sound.playTune(63,500);
        //Sound.playTune(64,500);
        //Sound.playTune(65,500);
        //Sound.playTune(66,500);
        
        //Sound.playTune(60,500);
        
        
        
        //test1.close();
        
    }
    */
    
}
