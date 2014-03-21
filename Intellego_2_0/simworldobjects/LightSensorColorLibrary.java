package simworldobjects;
/*
 * LightSensorColorLibrary.java
 *
 * Created on July 22, 2003, 3:59 PM
 */

import java.awt.*;
import java.util.*;
import main.*;

/**
 *
 * @author  Simon Zienkiewicz
 */
public class LightSensorColorLibrary {
    
    private String[] availableColors = {"Black","Blue","Cyan","Gray","Green","Magenta","Orange","Pink","Red","White","Yellow"};
    private Color desiredColor;
    private static boolean[] desiredColors;
    private static int[] lowerRange;
    private static int[] upperRange;
    private static int[] lowerError;
    private static int[] upperError;
    private static Random factor;
    
    /** Creates a new instance of LightSensorColorLibrary */
    public LightSensorColorLibrary(boolean[] d, int[] lr, int[] ur, int[] le, int[] ue){
        desiredColors = d;
        lowerRange = lr;
        upperRange = ur;
        lowerError = le;
        upperError = ue;
        factor = new Random();
    }
    
    public static int getValue(Color color){
    
        int index = getColorIndex(color);
        if(index != 9999){
            if(desiredColors[index]){
                int min = lowerRange[index]-lowerError[index];
                int max = upperRange[index]+upperError[index];
                int difference = max - min;
                
                return (min + (int)(factor.nextInt(difference+1)));
            }
            else return 911;
        }
        
        return 911;
    }
    
    private static int getColorIndex(Color c){
       
        if(c.getRGB()==(Color.black.getRGB()))return 0;
        else if(c.getRGB()==(Color.blue.getRGB()))return 1;
        else if(c.getRGB()==(Color.cyan.getRGB()))return 2;
        else if(c.getRGB()==(Color.gray.getRGB()))return 3;
        else if(c.getRGB()==(Color.green.getRGB()))return 4;
        else if(c.getRGB()==(Color.magenta.getRGB()))return 5;
        else if(c.getRGB()==(Color.orange.getRGB()))return 6;
        else if(c.getRGB()==(Color.pink.getRGB()))return 7;
        else if(c.getRGB()==(Color.red.getRGB()))return 8;
        else if(c.getRGB()==(Color.white.getRGB()))return 9;
        else if(c.getRGB()==(Color.yellow.getRGB()))return 10;
        else return 9999;
        
    }
    
    public void updateDesiredColors(boolean[] d, int[] lr, int[] ur, int[] le, int[] ue){
        desiredColors = d;
        lowerRange = lr;
        upperRange = ur;
        lowerError = le;
        upperError = ue;
    }
    
    public boolean[] getColorInfo(){
        return desiredColors;
    }
    
    public int[] getLowerRangeInfo(){
        return lowerRange;
    }
    
    public int[] getUpperRangeInfo(){
        return upperRange;
    }
    
    public int[] getLowerErrorInfo(){
        return lowerError;
    }
    
    public int[] getUpperErrorInfo(){
        return upperError;
    }
    
    
    
    
    
}
