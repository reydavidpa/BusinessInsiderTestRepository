package core;

import mappedTables.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import helpers.GlobalHelper;
import helpers.MySQLDatabaseManager;
import helpers.TestNGHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;



import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.SeleniumException;

public abstract class SeleniumWowTestNGScript extends SeleneseTestBase
{
	public static DefaultSelenium selenium;
	protected static SeleniumWowEngine engine;
	protected TestNGHelper helper;
	protected ArrayList<Object> commands = new ArrayList<Object>();
	protected ArrayList<String> errors = new ArrayList<String>();
	protected String environment = "";
	protected static String browser = "";
	protected static String schedulerId = "";
	protected static Grouping grouping;
	protected String email = "";
	// Relocate following local variables to make them global
	public static String environmentName = "";
	//protected static String serverName = "";
	//protected static int serverId = 0;
	protected static String browserName = "";	
	private MySQLDatabaseManager database = new MySQLDatabaseManager();
	protected String logdir = System.getProperty("user.dir");	
	
	// Variables needed when a second browser is required
	protected DefaultSelenium setUpSelenium;
	//protected DefaultSelenium secondSelenium;
	protected String secondBrowserName = ""; 
	boolean isBaseBrowserOpen = false;
	//protected boolean isSecondBrowserOpen = false;
	
	//Variable needed to dynamically set DB 
	public static String globalEnvironment = "";
	protected boolean passed = true;
	public static Selwowscheduler scheduler;
	public Logger logger;
	public static boolean readXml = true;
	public static boolean updateAgain = true;
	protected static Server Server;
	protected Browser Browser;
	protected static int PortProcessID[];
	protected Scheduledjobs scheduledJob;
	
	public SeleniumWowTestNGScript()
	{
	}
	
	@BeforeTest
	@Parameters({ "env", "server", "browser", "schedulerid", "email", "logdir"})
	public void setup(@Optional("QA")String env,
			@Optional("Makisupa")String server,
			@Optional("Firefox")String browser,
			@Optional("55555")String schedulerid,
			@Optional("reydavid_x@hotmail.com")String email,
			@Optional("C:") String logdir)
	{
		this.environment = env;
		this.browser = browser;
		this.schedulerId = schedulerid;
		this.email = email;
		this.logdir = logdir;

		if(schedulerId != null && !schedulerId.equalsIgnoreCase("4")){
			MySQLDatabaseManager dm = new MySQLDatabaseManager();
			scheduler = (Selwowscheduler) dm.get(Selwowscheduler.class, Integer.parseInt(schedulerid));
			grouping = (Grouping) dm.get(Grouping.class, scheduler.getGrouping().getGroupid()); 
		}
		
		GlobalHelper.server = server;
		logger = GlobalHelper.getLogger(this.logdir);
		
		globalEnvironment = environment;
		
		//by default, the test title is "Test did not run" - this changes when the child
		//sets the title
		SeleniumWowTestNGScript.engine = new SeleniumWowEngine("Test did not run", environment,
				server, browser,
				schedulerId, email, logdir);
		
		Server = database.getServer(server);
		
		if(this.getClass().getName().contains(".SignUp.") && database.getServerFromLocalHost().getIsSelwowschedulerServer()==1){
			environmentName = database.getEnvironment("INT4-Secure");
			if (browser.equalsIgnoreCase("Chrome"))
				browser="Firefox"; 
		}else
			environmentName = database.getEnvironment(environment);
			
		
		try{
			engine.load_classes();
		}
		catch (IOException ioe)
		{
			System.out.println("ERROR: Encountered a problem loading classes from the DB: " + ioe.getMessage());
			errors.add("ERROR: Encountered a problem loading classes from the DB");
		}
		
		//Starting Selenium Server
		if(this.getClass().getName().contains(".Unauthenticated.") || browser.equalsIgnoreCase("IE")){
			PortProcessID = new SeleniumServerControl().startSeleniumServer(schedulerId,Server,browser,SeleniumServerControl.MULTIPLE_WINDOWS,false);
		}else{
			PortProcessID = new SeleniumServerControl().startSeleniumServer(schedulerId,Server,browser,SeleniumServerControl.SINGLE_WINDOW,false);
		}
	}

	@BeforeClass
	protected void beforeClass(){
		if (database.JobExists(Integer.parseInt(schedulerId)) != null || schedulerId.equalsIgnoreCase("4")) {
			//Initialize the script browser
			engine.setStartTime(new Date());
			baseBrowser();
			engine.updateDatabaseTestStarted(readXml);
			if(updateAgain && !schedulerId.equalsIgnoreCase("4")){
				database.updateGoodToGo(schedulerId, 1);
			}
			updateAgain=false;
		}
	}
	
	//method to start the first selenium for the script browser
	public void baseBrowser() 
	{
		Browser = database.getBrowser(browser,Server.getServerid());
		selenium = setUpBrowser(Browser.getBrowserpath());

		try{
			if(schedulerId.equalsIgnoreCase("4") || database.JobExists(Integer.parseInt(schedulerId)) != null || database.isJobKilled(Integer.parseInt(schedulerId))){
				selenium.start();
				selenium.open("/");
				//selenium.setSpeed("300");
				if(environment.equalsIgnoreCase("INT4-Secure")){
					GlobalHelper.pause(12000);
				}
				selenium.windowMaximize();
			}
		}catch(RuntimeException ie)	{
			System.out.println("Error on baseBrowser: " + ie.getMessage());
			errors.add("Error on baseBrowser: " + ie.getMessage());
		}

		isBaseBrowserOpen = true;
		setTestNGHelper(selenium, browser);
	}
	
	//method to start the server based on the required browser
	public DefaultSelenium setUpBrowser(String browserPath)
	{
		setUpSelenium = new DefaultSelenium(Server.getServerip(),PortProcessID[0],browserPath,environmentName);
		SeleniumServerControl.Selenium=setUpSelenium;
		return setUpSelenium;
	} 
	
	//method to set the TestNGHelper for each browser
	public void setTestNGHelper(DefaultSelenium seleniumrey, String browser)
	{
		helper = new TestNGHelper(seleniumrey, engine);
	}
		
	@AfterClass
	public void processEmail()
	{
		
			engine.add_timestamp();
			engine.addErrorsIndex(0,errors);
			String results = engine.process_email();
		
			if (database.getServerFromLocalHost().getIsSelwowschedulerServer()==1){
				try{
					engine.updateDatabaseTestFinished(results);
				}catch(Exception e){
					System.out.println("Error on processEmail: "+e.getMessage());
					if(results==null){
						System.out.println("results is null");
					}else{
						System.out.println("results:"+results);
					}
				}
				//update the grouping table, based on whether the group passed or not
				if(engine.getErrors().size() > 0 && passed){
					passed = false;
					grouping.setHaspassed(0);
					database.update(grouping);
					System.out.println("Group failed");
				}
			}
			if (selenium != null){
				try{
					selenium.close();
					selenium.stop();
					GlobalHelper.pause(2000);
				}catch(SeleniumException se){
					if (se.getMessage().contains("place before new command close could be added"))
						System.out.println("ERROR on processEmail closing selenium: "+se.getMessage());	
					if (se.getMessage().contains("Connection refused"))
						System.out.println("ERROR on processEmail stopping selenium: "+se.getMessage());	
				}
			}
		
		System.out.println("DONE EXECUTING TEST: "+this.getClass().getSimpleName()+"\n");
		System.out.println("_________________________________________________________________________________________");
	}
	
	@AfterSuite
	public void endTest()
	{
		Selwowscheduler sw = (Selwowscheduler) database.get(Selwowscheduler.class, Integer.parseInt(schedulerId));
		if(sw!=null){
			
			Scheduledjobs sj = (Scheduledjobs) database.get("from Scheduledjobs sj where sj.jobstatusid in (1,4) and sj.selwowscheduler.schedulerid="+schedulerId, true);
			
			//delete records for jobs killed and jobs not scheduled in official server
			//schedulersource 1 is Apache in official scheduler 
			if(sw.getSchedulersource().getSchedulersourceId()!=1 || sj==null){
				
				if(sw.getSchedulersource().getSchedulersourceId()!=1) database.delete(sj);
				
				ArrayList<Testcaseresults> tcr = (ArrayList<Testcaseresults>) database.get("from Testcaseresults tcr where tcr.selwowscheduler.schedulerid="+schedulerId, false);
				while (tcr.iterator().hasNext()){
					database.delete(tcr.iterator().next());
					tcr.remove(tcr.iterator().next());
				}
								
				database.delete(sw);
			}
		
		if(TestNGHelper.getEng().getErrors().isEmpty()){
			grouping.setHaspassed(1);
			database.update(grouping);
			System.out.println("Group passed");
		}		
	}

	if (selenium!=null)
		try{
			selenium.shutDownSeleniumServer();
		}catch(SeleniumException se){
			if(!se.getMessage().contains("Connection refused"))
				throw se;
		}
	}
	
	protected void setTitle(String title)
	{
		engine.setTestTitle(title);
	}
	
}
