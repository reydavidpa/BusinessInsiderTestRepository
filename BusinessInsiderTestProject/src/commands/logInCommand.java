package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class logInCommand extends SeleniumWowCommand<String,String> {

	public String _target;
	public String _value;
	
	public void execute(String username, String password, DefaultSelenium ds){
		System.out.println("Executing LogIn command ... " + username);
		
		new clickCommand().execute("//a[@class and contains(text(),'Sign-in')]", null, ds);
		new captureEntirePageScreenShotCommand().execute("Log In light box", "LogIn_Lightbox", ds);
		new typeCommand().execute("//input[@name='username']", username, ds);
		new typeCommand().execute("//input[@name='password']", password, ds);
		new clickCommand().execute("//input[@value='Login']", null, ds);
		new waitForPageToLoadCommand().execute(null, null, ds);
		new captureEntirePageScreenShotCommand().execute("Authenticated home page", "Post_LogIn", ds);
		new verifyTextPresentCommand().execute(username, null, ds); 	
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
