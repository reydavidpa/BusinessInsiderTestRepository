package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class checkSearchOpenCloseCommand extends SeleniumWowCommand<String,String> {

	public String _target;
	public String _value;
	
	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing checkSearchOpenClose command ... ");
		
		new clickCommand().execute("//span[@class='svg sprites search-icon']", null, ds);
		new clickCommand().execute("//form[@class='search-field']/input", null, ds);
		new verifyElementPresentCommand().execute("//form[@class='search-field']/input", null, ds);
		new captureEntirePageScreenShotCommand().execute("Search Field is open", "Search_field_open", ds);
		new clickCommand().execute("//a[@class='close-x']", null, ds);
		new captureEntirePageScreenShotCommand().execute("Search Field is closed", "Search_field_closed", ds);
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
