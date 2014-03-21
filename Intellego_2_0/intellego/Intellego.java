package intellego;

import main.*;
import util.*;

import javax.swing.*;

/**
* Startup class - starts the whole system
*/
public class Intellego
{
	private static boolean debugMode;	// debug mode flag
	private static IntellegoLog log;	// the log for this run
               
	/**
	* Adds a message to the log
	*
	* @param message the message to be added
	*/
	public static void addToLog(String message)
	{
		// if we're in debug mode, print the messages out to the scren as well
		if(debugMode)
		{
			System.out.println("Log message: "+message);
		}

		// add the message to the log
		log.addMessage(message);
	}

	/**
	* Prints usage instructions to stdout
	*/
	public static void printUsage()
	{
		System.out.println(	"\nUsage: java Intellego [-option]\n"+
							"\nwhere option is one of:\n\n"+
							"\t-d -debug\truns Intellego in debug mode (prints messages to stdout)\n"+
							"\t-h -help\tdisplays this message");
	}

	/**
	* Fires up the whole system
	*/
	public static void main(String args[])
	{
		// set debug mode
		debugMode=false;

		// check command line args
		if (args.length==0)
		{
			// do nothing
		}
		else if (args.length==1)
		{
			if (args[0].equalsIgnoreCase("-d") || args[0].equalsIgnoreCase("-debug"))
			{
				debugMode=true;
			}
			else if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help"))
			{
				printUsage();
				System.exit(0);
			}
			else
			{
				printUsage();
				System.exit(0);
			}
		}
		else // unknown args
		{
			printUsage();
			System.exit(0);
		}

		// set up the log
		log=new IntellegoLog();
                
		// fire up the main GUI
		MainInterface gui=new MainInterface();
		gui.setVisible(true);

		addToLog("Intellego.main(): GUI set up successfully");
	}
}
