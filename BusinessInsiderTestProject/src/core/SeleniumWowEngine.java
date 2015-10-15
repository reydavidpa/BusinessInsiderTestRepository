package core;

import helpers.MySQLDatabaseManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import mappedTables.Browser;
import mappedTables.Grouping;
import mappedTables.Testcase;



import com.thoughtworks.selenium.SeleneseTestCase;
import core.Printer.PrinterOutputType;
import core.PrinterOutput;

public class SeleniumWowEngine extends SeleneseTestCase{
	private HashMap<String, SeleniumWowCommand> hm;
	private Queue<PrinterOutput> output_queue;
	private Queue<PrinterOutput> header_queue;
	private static ArrayList<String> errors;
	private Date StartTime;
	private Date EndTime;
	private String TestTitle;
	private int resultsId;
	private int schedulerId;
	private String testerEmail = "";
	private int testerUserId;
	//these variables are assigned values from the XML file
	private String environment = "";
	private String browser = "";
	//private String browserPath = "";
	private String server = "";
	private String logdir = "";

	/**
	 * this starts a new SeleniumWowEngine for Scheduled TestNG sripts.
	 *  
	 * @param classes - classes.txt
	 * @param testTitle - title of the test; default is "Test did not run"
	 * @param schedulerId - id from the database for the scheduler initiating this test
	 * @param server - which server the test should run on
	 * @param browser - which browser the test should run on
	 */
	public SeleniumWowEngine(String testTitle, String environment,
			String server, String browser,
			String schedulerId, String email,String logdir)
	{
		hm = new HashMap<String, SeleniumWowCommand>();
		//selenium_queue = new LinkedList<SeleniumCommand>();
		output_queue = new LinkedList<PrinterOutput>();
		header_queue = new LinkedList<PrinterOutput>();
		errors = new ArrayList<String>();

		//separator = GlobalHelper.getSeparator();
		//String path_start = GlobalHelper.getPathStart();
//		this.StartTime = new Date();
		//this.class_input_file = path_start + separator + "core" + separator + "classes.txt";
		this.TestTitle = "Regression Test - " + testTitle;
		this.environment = environment;
		this.testerEmail = email;
		this.logdir = logdir;
		this.server = server;
		//this.setServer(server);
		this.browser = browser;

		try
		{
			this.schedulerId = Integer.parseInt(schedulerId);
		}
		catch(NumberFormatException nfe)
		{
			this.schedulerId = 7;
			System.out.println("The scheduler id is not in the correct format");
			errors.add("The scheduler id is not in the correct format");
		}
		
		System.out.println("TESTNG ENGINE: ");
		System.out.println("_________________________________________________________________________________________");
		System.out.println("Scheduler ID: " + schedulerId);
		System.out.println("Environment: " + environment);
		System.out.println("Browser: " + browser);
		System.out.println("Server: " + server);
		System.out.println("Tester Email: " + testerEmail);

	}

	public void add_timestamp()
	{
		EndTime = new Date();	
		PrinterOutput pout = new PrinterOutput();
		ArrayList<String> alist = new ArrayList<String>();

		pout.add_heading("Start Time");
		String temp_date = StartTime.toString();
		alist.add(temp_date);
		pout.add_value(alist);
		header_queue.add(pout);

		pout = new PrinterOutput();
		alist = new ArrayList<String>();
		pout.add_heading("End Time");
		temp_date = EndTime.toString();
		alist.add(temp_date);
		pout.add_value(alist);
		header_queue.add(pout);
	}

	public int updateDatabaseTestStarted(boolean readXml)
	{
		MySQLDatabaseManager database = new MySQLDatabaseManager();
		testerUserId = database.getUserId(testerEmail);
		return resultsId = database.setStartTime(schedulerId, testerUserId, readXml);
	}
	
	public void updateDatabaseTestFinished(String results)
	{
		//in MySQL; so passed = 0 means there was at least one error;
		//passed = 1 means there were no errors
		int passed = 0;
		if(errors.size() == 0)
		{
			passed = 1;
		}
		
		MySQLDatabaseManager database = new MySQLDatabaseManager();
		
		if (database.getServerFromLocalHost().getIsSelwowschedulerServer()==1){
			database.saveResults(results, resultsId, passed, errors);
		}
		//clean up the queues
		output_queue = new LinkedList<PrinterOutput>();
		header_queue = new LinkedList<PrinterOutput>();
	}

	static public String getContents(File aFile) {
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    
	    return contents.toString();
	  }
	
	public String process_email()
	{
		String emailBody = "";
		PrinterOutput mBody = new PrinterOutput();
		mBody = header_queue.peek();
		
		if(mBody == null)
		{
			System.out.println("mBody is null\n");
		}

		//Process the list of errors for output
		if(errors != null && errors.size() > 0)
		{
			PrinterOutput outputErrors = new PrinterOutput(PrinterOutputType.TABLE_ROW, "<div id=\'error\'>Errors</div>");
			ArrayList<String> line = new ArrayList<String>();

			outputErrors.add_heading("<span id=\'error\'>#</span>");
			outputErrors.add_heading("<span id=\'error\'>Error</span>");
			
			System.out.println("ERRORS:");
			for(int x = 0; x < errors.size(); x++)
			{
				line = new ArrayList<String>();
				line.add("<span id=\'error\'>" + (x + 1) + "</span>");
				line.add("<span id=\'error\'>" + errors.get(x) + "</span>");
				outputErrors.add_value(line);

				System.out.println((x + 1) + ": " + errors.get(x));
			}
			output_queue.add(outputErrors);
		}

		SeleniumWowEmailGenerator mail = 
			new SeleniumWowEmailGenerator(header_queue, output_queue, TestTitle + " - " + this.browser);
		
			System.out.println("Processing email in selwowengine ...");
			mail.process_output_heading();
			mail.process_output_values();
			try {
				emailBody = mail.send_mail(testerEmail);
			} catch (AddressException e) {
				System.out.println("Error on process_email (address): "+e.getMessage());
			} catch (MessagingException e) {
				System.out.println("Error on process_email (messaging): "+e.getMessage());
			}		
		return emailBody;
	}

	private String trimCommandString(String str)
	{
		int index = str.lastIndexOf('.');
		String last = str.substring(index + 1, str.length());
		return last;
	}

	public void load_classes() throws IOException
	{
		MySQLDatabaseManager database = new MySQLDatabaseManager();
		ArrayList<String> commands = new ArrayList<String>();  
		commands = database.getDBCommands();
		
		System.out.println("Starting to parse classes from the DB ... ");
		
		for (int index = 0; index < commands.size(); index++){
			String line = commands.get(index);
			String commandString;

			//remove the package names
			commandString = trimCommandString(line);
			try
			{
				if(!(hm.containsKey(commandString)))
				{
					ClassLoader cl = SeleniumWowTestNGScript.class.getClassLoader();
					Class<?> c = cl.loadClass(line);
						SeleniumWowCommand commandClass = (SeleniumWowCommand)c.newInstance();
						commandClass.initialiseLogger(this.logdir);
					hm.put(commandString, commandClass);
				}
				else
				{
					System.out.println("Already loaded " + line + " ... check for problems if this has not been loaded");
				}
			}
			catch(ClassNotFoundException cnfex)
			{
				//Do not use the stack trace with the scheduler - it enters an infinite loop
				//					cnfex.printStackTrace();
				System.out.println("-> ClassNotFoundException : " + cnfex.getMessage());
				errors.add("Class Not Found while loading commands:\n" + cnfex.getMessage());
			}
			catch(Exception ex)
			{
				errors.add("Exception caught while loading commands:\n" + ex.getMessage());
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("Done loading classes");
	}
	
	public void load_classes_perGroup(String schedulerId) throws IOException
	{
		MySQLDatabaseManager database = new MySQLDatabaseManager();
		ArrayList<String> commands = new ArrayList<String>();  
		
		
		//Browser browser = (Browser) database.get("select sws.browser from Selwowscheduler sws where sws.schedulerid="+schedulerId, true);
		Grouping Group =  (Grouping) database.get("select sws.grouping From Selwowscheduler sws where sws.schedulerid = "+schedulerId, true);
		List<Testcase> TestCase =  (List<Testcase>) database.get("from Testcase tc where tc.grouping.groupid = "+Group.getGroupid(), false);
				
		 try {
			Class<?> lop = Class.forName("testNGscripts."+Group.getGroupprefix());
			Browser browser = (Browser) database.get("select sws.browser from Selwowscheduler sws where sws.schedulerid="+schedulerId, true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		System.out.println("Starting to parse classes from the DB ... ");
		
		for (int index = 0; index < commands.size(); index++){
			String line = commands.get(index);
			String commandString;

			//remove the package names
			commandString = trimCommandString(line);
			try
			{
				if(!(hm.containsKey(commandString)))
				{
					ClassLoader cl = SeleniumWowTestNGScript.class.getClassLoader();
					Class<?> c = cl.loadClass(line);
						SeleniumWowCommand commandClass = (SeleniumWowCommand)c.newInstance();
						commandClass.initialiseLogger(this.logdir);
					hm.put(commandString, commandClass);
				}
				else
				{
					System.out.println("Already loaded " + line + " ... check for problems if this has not been loaded");
				}
			}
			catch(ClassNotFoundException cnfex)
			{
				//Do not use the stack trace with the scheduler - it enters an infinite loop
				//					cnfex.printStackTrace();
				System.out.println("-> ClassNotFoundException : " + cnfex.getMessage());
				errors.add("Class Not Found while loading commands:\n" + cnfex.getMessage());
			}
			catch(Exception ex)
			{
				errors.add("Exception caught while loading commands:\n" + ex.getMessage());
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("Done loading classes");
	}

	public static ArrayList<String> getErrors()
	{
		return errors;
	}
	public void addErrors(ArrayList<String> errors)
	{
		this.errors.addAll(errors);
	}
	public void addErrorsIndex(int index, ArrayList<String> errors)
	{
		this.errors.addAll(index,errors);
	}

	/**
	 * For external processing of commands.
	 * 
	 * When running SeleniumWow with the TestNG framework, we need to be able to
	 * run commands outside SeleniumWowEngine; this method is the getter for the
	 * hashmap of commands.
	 * 
	 * @return
	 */
	public HashMap<String, SeleniumWowCommand> getCommands()
	{
		return this.hm;
	}
	
	/**
	 * This method adds a single PrinterOutput object to the header queue
	 * @param header
	 */
	public void addHeader(PrinterOutput header)
	{
		this.header_queue.add(header);
	}
	/**
	 * This method adds a single PrinterOutput object to the output queue
	 * @param output
	 */
	public void addOutput(PrinterOutput output)
	{
		this.output_queue.add(output);
	}
	/**
	 * This sets the test title when running the engine with TestNG
	 * @param title
	 */
	public void setTestTitle(String title)
	{
		this.TestTitle = "Regression Test - " + title;
	}
	public String getTestTitle()
	{
		return this.TestTitle.replace("Regression Test - ", "");
	}
	
	/**
	 * Get the results id
	 */
	public long getResultsId()
	{
		return this.resultsId;
	}
	/*public void setResultsId(int resultsid)
	{
		this.resultsId = resultsid;
	}*/

	public int getSchedulerId() {
		return schedulerId;
	}
	
	public String getEmail() {
		return testerEmail;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}

}