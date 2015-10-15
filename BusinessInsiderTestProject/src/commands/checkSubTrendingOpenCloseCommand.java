package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class checkSubTrendingOpenCloseCommand extends SeleniumWowCommand<String,String> {

	public String _target;
	public String _value;
	
	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing checkSubTrendingOpenClose command ... ");
		
		new clickCommand().execute("//div[@class='hamburger']", null, ds);
		new verifyElementPresentCommand().execute("//div[@class='transition']", null, ds);
		new captureEntirePageScreenShotCommand().execute("Sub trending is open", "Sub_Trending_open", ds);
		new clickCommand().execute("//div[@class='hamburger open']", null, ds);
		new captureEntirePageScreenShotCommand().execute("Sub trending is closed", "Sub_Trending_closed", ds);
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
