package commands;

import com.thoughtworks.selenium.*;

import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;

public class waitForPageToLoadCommand extends SeleniumWowCommand<String,String>{

	public String _target;
	public String _value;
	public PrinterOutput po;

	
	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing waitForPageToLoad Command ...");
		
		if (t==null){
			ds.waitForPageToLoad("15000");
		}
		else{
			try{
				ds.waitForPageToLoad(t);
			}catch(SeleniumException e){
				if (e.getMessage().contains("Timed out")){
					throw new SeleniumWowCommandException(ds.getLocation()+" is taking longer to load");
				}
			}
		}
		new pauseCommand().execute("1000", null, ds);
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
