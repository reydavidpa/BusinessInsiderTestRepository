package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;


public class typeCommand extends SeleniumWowCommand<String,String>{
	public String _target;
	public String _value;
	public PrinterOutput po;

	public typeCommand(){
	}

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing type command ... " + v);
		System.out.println("Target is " + t);
		System.out.println("Value is " + v);
		ds.type(t, v);
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
