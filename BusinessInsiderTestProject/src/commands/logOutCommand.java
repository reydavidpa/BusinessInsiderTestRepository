package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;

public class logOutCommand extends SeleniumWowCommand<String,String>
{

	public void execute(String username, String v, DefaultSelenium ds)
	{
		System.out.println("Executing LogOut command ... " + username);
		
		if (ds.isElementPresent("//a[text()='"+username+" ']")){
			new clickCommand().execute("//a[text()='"+username+" ']", null, ds);
			//verify social networks are available
			new verifyElementPresentCommand().execute("//a[@id='tw_login_link']", null, ds);
			new verifyElementPresentCommand().execute("//a[@class='btn btn-social facebook']", null, ds);
			new verifyElementPresentCommand().execute("//span[@id='gplus_login_button']", null, ds);
			new captureEntirePageScreenShotCommand().execute("Log out light box", "LogOut_Lightbox", ds);
			new clickCommand().execute("//a[@id='logout_link']", null, ds);
			new waitForPageToLoadCommand().execute("10000", null, ds);
			new captureEntirePageScreenShotCommand().execute("Public Home Page", "Post_LogOut", ds);
			new verifyElementPresentCommand().execute("//a[@class and contains(text(),'Sign-in')]", null, ds);
		}
		else
			throw new SeleniumWowCommandException("Username is not set in the header");
	}

	public PrinterOutput get_output(){
		return null;
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(false);
	}
}
