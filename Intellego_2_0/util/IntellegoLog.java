package util;

import intellego.*;
import java.io.*;
import java.util.*;

/**
* Creates a log file and provides methods to add messages to it 
*
* @author Graham Ritchie
*/
public class IntellegoLog
{
	private File log;		// the log file
	private FileWriter fw;	// the filewriter
	       
	/**
	* Creates the log file and writes a header message
	*/
	public IntellegoLog()
	{
		// create the log file
		log=new File("logs/Intellego.txt");
                	
		// get the current time
		Date date=new Date();
                String temp=date.toString();
                String time=temp.substring(0,19);
		
		// try to write a header to the file
		try
		{
			log.createNewFile();
			fw=new FileWriter(log);
			fw.write("========================\n  Intellego Log File\n========================\n\n");
			fw.write("System started on: "+time+"\n\nMessages:\n\n");
			fw.flush();
		}
		catch (Exception e)
		{
			System.out.println("IntellegoLog.init(): Failed to create log file: "+e);
		}
	}
	
	/**
	* Adds a message string to the log file
	*
	* @param message the message to be added
	*/
	public synchronized void addMessage(String message)
	{
		// try to write the message to the file
		try
		{
			fw.write(message+"\n");
			fw.flush();
		}
		catch (Exception e)
		{
			System.out.println("IntellegoLog.addMessage(): Failed to add log message: "+e);
		}
	}
}
