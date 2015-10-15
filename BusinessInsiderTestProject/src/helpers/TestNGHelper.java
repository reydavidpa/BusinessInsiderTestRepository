package helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
import commands.logOutCommand;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;
import core.SeleniumWowEngine;

public class TestNGHelper
{
	private static DefaultSelenium sel;
	private static SeleniumWowEngine eng;
	private static String environment;
	private static String server;
	private static String browser;
	private static String testerEmail;
	private static String logdir;
	private static Logger logger = GlobalHelper.getLogger("");
	
	//TODO: save until we're sure TFSID is working correctly - testCaseId should work the way it's expected to
	//	private static long tfsid = 0;

	public TestNGHelper(DefaultSelenium selenium, SeleniumWowEngine eng)
	{
		TestNGHelper.sel = selenium;
		TestNGHelper.setEng(eng);
	}

	
	public TestNGHelper(DefaultSelenium selenium, SeleniumWowEngine eng, String env, String server, String browser, String email, String logdir)
	{
		TestNGHelper.sel = selenium;
		TestNGHelper.setEng(eng);
		TestNGHelper.environment = env;
		TestNGHelper.server = server;
		TestNGHelper.browser = browser;
		TestNGHelper.testerEmail = email;
		TestNGHelper.logdir = logdir;
		
		System.out.println("TESTNG: \n");
		System.out.println("Environment: " + TestNGHelper.environment);
		System.out.println("Server: " + TestNGHelper.server);
		System.out.println("Browser: " + TestNGHelper.browser);
		System.out.println("Email of Tester: " + TestNGHelper.testerEmail);
		System.out.println("Log Directory: " + TestNGHelper.logdir);
	}

	public static void executeCommand(String command, Object target, Object value) throws IOException
	{	
		checkErrors();
	
		HashMap<String, SeleniumWowCommand> map = getEng().getCommands();
		SeleniumWowCommand com;
		
		if(command.contains("Command")){
			com = map.get(command);
		}
		else{
			com = map.get(command + "Command");
		}
		try
		{
			//This is for when the command name is misspelled or has not
			//been entered in classes.txt
			//This block of code willadd an error to SelWowEng that
			//will display in the output
			if(com == null)
			{
				ArrayList<String> nullCom = new ArrayList<String>();
				nullCom.add("There is no such command as " + command);
				getEng().addErrors(nullCom);
				return;
			}
			
			//added so that it opens the admin tools
			//needs to be modified later so that it gets the admin-tools ip address from the DB based on the environment and puts it in the target,
			//instead of being hard coded.
			else if("open".equalsIgnoreCase(command) || "openCommand".equalsIgnoreCase(command))
			{
				if("/Adminlogin.aspx".equalsIgnoreCase((String) target))
				{
					target = new MySQLDatabaseManager().getEnvironment("QA_Admin"); 
				}
				if ("/Default.aspx".equalsIgnoreCase((String) target))
					target = "";
				com.execute(target, value, sel);
			}
			
			//Added this code to TestNG helper so the variables work with .java files
			else if("type".equalsIgnoreCase(command) || "typeCommand".equalsIgnoreCase(command))
			{
				if("@@testerEmail@@".equalsIgnoreCase((String) value))
				{
					value="reydavid_x@hotmail.com";
				}
				com.execute(target, value, sel);
			}			
			
			else if("TESTCASECommand".equalsIgnoreCase(command) || "TESTCASECommand".equalsIgnoreCase(command))
			{
				//TODO: save until we're sure TFSID is working correctly - testCaseId should work the way it's expected to
//				TestNGHelper.tfsid = Long.parseLong(target);
//				eng.setTFSID(tfsid);
				
				//need to pass it to the command as a string, then parse it on the other side
				value = getEng().getResultsId() + "";
				com.execute(target, value, sel);
				
				MySQLDatabaseManager.cleanUpConnections();
				
				//delete images at the beginning of each script
				//String path = "Q:\\FRN\\QA\\ScriptImagesTemp";
				String path = "\\\\\\\\192.168.22.17\\\\images\\\\";
				
				File[] files = new File(path).listFiles();
				
				if(files != null){
					//System.out.println(files.length);
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {
							if(files[i].getName().contains(TestNGHelper.getEng().getTestTitle())){
								files[i].delete();						
					    	}
					    } 
					}
				}
				
			}
			else
			{
				com.execute(target, value, sel);
			}
		}
		catch(NullPointerException npe)
		{
			logger.error("There is a problem running " + command + " command: " + npe.getMessage());
			ArrayList<String> selError = new ArrayList<String>();
			selError.add("There was an error executing the " + command + " command: " + getCustomStackTrace(npe));
			getEng().addErrors(selError);
			
			if (!command.equalsIgnoreCase("captureEntirePageScreenShot"))
				TestNGHelper.executeCommand("captureEntirePageScreenShot", npe.getMessage(), getEng().getTestTitle());
		}
		catch(SeleniumException se)
		{
			logger.error("Error executing command: " + se.getMessage());
			ArrayList<String> selError = new ArrayList<String>();
			selError.add("There was an error executing the command: " + command + " ( " + target + ", " + value + " )");
			getEng().addErrors(selError);
			
			if (!command.equalsIgnoreCase("captureEntirePageScreenShot"))
				TestNGHelper.executeCommand("captureEntirePageScreenShot", se.getMessage(), getEng().getTestTitle());
		}
		catch(SeleniumWowCommandException swce)
		{
			if (swce.getMessage() != null) {
				logger.error("Error executing command: " + swce.getMessage());
				ArrayList<String> selError = new ArrayList<String>();
				selError.add("There was an error executing the " + command + " command:\n" + swce.getMessage());
				getEng().addErrors(selError);
				
				if (!command.equalsIgnoreCase("captureEntirePageScreenShot"))
					TestNGHelper.executeCommand("captureEntirePageScreenShot", swce.getMessage(), getEng().getTestTitle());
			}
		}

		if(com != null && com.has_output())
		{
			if(com.is_header())
			{
				getEng().addHeader(com.get_output());
			}
			else
			{
				getEng().addOutput(com.get_output());
			}
		}
	}
	
	private static void checkErrors() {
		//Do not execute commands if there are five or more errors
		if(sel!=null){
			if(getEng().getErrors().size() >= 5)
			{
				if(getEng().getErrors().size() == 5)
				{
					ArrayList<String> oneMoreError = new ArrayList<String>();
					oneMoreError.add("There have been too many errors with this script; no more commands will be executed");
					logger.fatal("There have been too many errors with this script; no more commands will be executed");
					getEng().addErrors(oneMoreError);

					new logOutCommand().execute(null, null, sel);
				}
				return;
			}

		}
	}
	 /**
	  * GP: Defines a custom format for the stack trace as String.
	  */
	  public static String getCustomStackTrace(Throwable aThrowable) {
	    //add the class name and any message passed to constructor
	    final StringBuilder result = new StringBuilder("");
	    result.append(aThrowable.toString());
	    final String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }

	public static SeleniumWowEngine getEng() {
		return eng;
	}

	public static void setEng(SeleniumWowEngine eng) {
		TestNGHelper.eng = eng;
	}
}
