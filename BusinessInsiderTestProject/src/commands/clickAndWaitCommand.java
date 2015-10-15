package commands;

import helpers.GlobalHelper;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class clickAndWaitCommand extends SeleniumWowCommand<String,String>{

	public String _target;
	public String _value;
	boolean output_exists;
	public PrinterOutput po;

	public clickAndWaitCommand(){
	}

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing click and wait command ... Click " + t + " and wait");
		ds.click(t);
		GlobalHelper.pause(3000);
	}

	public PrinterOutput get_output(){
		return(null);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(false);
	}
}
