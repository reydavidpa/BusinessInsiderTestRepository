package commands;

import helpers.GlobalHelper;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;

public class openCommand extends SeleniumWowCommand<String,String>{

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing open command ...");
		System.out.println("target (URL) is " + t);
		
		try{
			ds.open(t);
		}catch(SeleniumException e){
			if (e.getMessage().contains("Timed out")){
				throw new SeleniumWowCommandException(t+" is not opening");
			}
		}
		GlobalHelper.pause(10000);
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
