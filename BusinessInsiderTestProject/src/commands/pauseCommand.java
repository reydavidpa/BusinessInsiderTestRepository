package commands;

import helpers.GlobalHelper;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;


public class pauseCommand extends SeleniumWowCommand<String,String>{

	public PrinterOutput po;

	public void execute(String t, String v, DefaultSelenium ds){
		
		if (t == null || t.length() == 0){
			GlobalHelper.pause(8000);
		}else{
			GlobalHelper.pause(Integer.parseInt(t));
		}
	}

	public PrinterOutput get_output(){
		return(po);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(false);
	}
}
