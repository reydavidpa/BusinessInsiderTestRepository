package helpers;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import core.SeleniumWowCommand;

public class GlobalHelper {
	
	public static Logger logger;
	public static String server="";
	
	private static class SeleniumWowPrintStream extends PrintStream
    {
           public SeleniumWowPrintStream(OutputStream out)
           {
                  super(out);
           }
           //Overwrite the println method in PrintStream to also log to logger.debug()
           public void println(String o)
           {
        	   if(!o.isEmpty()){
        		   //Index check required as few scheduler log statements already have server name in beginning  
        		   if(o.contains("Error"))
        			   if (o.contains("line is  <table>"))
        				   logger.info(o.indexOf(server + ": ") == -1?(server + ": ") + o: o);
        			   else
        				   logger.error(o.indexOf(server + ": ") == -1?(server + ": ") + o: o);
        		   else
        			   logger.info(o.indexOf(server + ": ") == -1?(server + ": ") + o: o);
        	   }
        	   else
        		   super.println("");
           }
    }

	public static void trackUsers(int userType, String user){}
	
	/**
	 * This returns the logger object that enables log4j logging to a file.
	 * @param MyClass: Name of the class calling this method
	 */
	
	public static Logger getLogger(String logDir)
	{
		String osName = System.getProperty("os.name").toLowerCase();
		String userDir = System.getProperty("user.dir");
		//make the new PrintStream the standard Stream for out
		
		if (logger == null)
		{
			logger = Logger.getLogger(SeleniumWowCommand.class);
			System.setOut(new SeleniumWowPrintStream(System.out));
		}

		return logger;
	}
	
	/**
	 * This uses the current environment's operating system name to determine
	 * which separator to use in path names
	 * @return
	 */
	public static String getSeparator()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		String separator = "";
		if(osName != null && osName.contains("win"))
		{
			separator = "\\";
		}
		else
		{
			separator = "/";
		}

		return separator;
	}
	
	
	/**
	 * This uses the current environment's operating system name to determine
	 * which separator to use in path names
	 * @return
	 */
	public static String getPathStart()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		String path_start = "";
		if(osName != null && osName.contains("win"))
		{
			path_start = "src";
		}
		else
		{
			path_start = "/opt/selenium/SeleniumWow/src";
		}

		return path_start;
	}
	
	public static void sendMail(String content)
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", "192.168.22.96");
		props.put("mail.from", "no-reply@owlmail.onewire.com");
		Session session = Session.getInstance(props, null);

		try{
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom();
			msg.setRecipients(Message.RecipientType.TO, "ehartill@onewire.com");
			msg.setSubject("email from Regression");
			msg.setSentDate(new Date());
			msg.setText(content);
			
			Transport.send(msg);
		}
		catch (MessagingException me){
			System.out.println("Error sending email: " + me.getMessage());
		}
	}

	/**
	 * Prints each row in the table
	 * 
	 * @param table
	 */
	public static void debugTable(ArrayList<ArrayList<String>> table)
	{
		System.out.println("\n\n**********");
		System.out.println("Debugging Table");
		System.out.println("**********");
		for(int x = 0; x < table.size(); x++)
		{
			System.out.println(table.get(x));
		}
		System.out.println("**********\n\n");
	}
	
	/**
	 * Pause for the specified number of milliseconds
	 */
	public static void pause(int millis)
	{
		System.out.println("Pausing for " + (float)millis/1000 + " seconds");
		try
		{
			Thread.sleep(millis);
		}
		catch(InterruptedException ie)
		{
			System.out.println("There seems to be a problem with the Thread: " + ie.getMessage());
			ie.printStackTrace();
		}
	}
	
	public static void printHashMap(HashMap<String, SeleniumWowCommand> map)
	{
		Collection<String> c = map.keySet();
		Iterator<String> i = c.iterator();
		
		System.out.println("\n\nPrinting map of commands: ");
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
		System.out.println();
	}
}
